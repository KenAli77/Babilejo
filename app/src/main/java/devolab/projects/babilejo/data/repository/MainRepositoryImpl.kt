package devolab.projects.babilejo.data.repository

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import devolab.projects.babilejo.domain.model.*
import devolab.projects.babilejo.domain.repository.MainRepository
import devolab.projects.babilejo.util.*
import devolab.projects.babilejo.util.USERS
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume

class MainRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInClient: GoogleSignInClient,
    private val db: FirebaseFirestore,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : MainRepository {
    private val tag = "MainRepositoryImpl"
    override val displayName: String
        get() = auth.currentUser?.displayName.toString()
    override val photoUrl: String
        get() = auth.currentUser?.photoUrl.toString()

    private val userCollection = Firebase.firestore.collection("users")
    private val postsCollection = Firebase.firestore.collection("posts_global")

    override val currentUser = auth.currentUser

    override suspend fun getUserData(uid: String?): UserDataResponse {


        val userId = uid ?: currentUser?.uid
        return try {
            val user = db.collection(USERS).document(userId!!).get().await()

            Log.e("user data", user.toObject<User>()?.userName.toString())
            Resource.Success(user.toObject<User>())
        } catch (e: Exception) {
            e.printStackTrace()
            print(e.message.toString())
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun signOut(): SignOutResponse {
        return try {
            oneTapClient.signOut()
            auth.signOut()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            print(e.message.toString())
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun revokeAccess(): RevokeAccessResponse {
        return try {
            auth.currentUser?.apply {
                db.collection(USERS).document(uid).delete().await()
                signInClient.revokeAccess().await()
                oneTapClient.signOut().await()
                delete().await()
            }
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            print(e.message.toString())
            Resource.Error(e.message.toString())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun addPost(post: Post, imageBitmap: Bitmap?): Resource<Void> =
        suspendCancellableCoroutine { cont ->

            imageBitmap?.let { bitmap ->
                val photoId = UUID.randomUUID()
                val photoRef = storage.reference.child("photos/${photoId}")

                val outputStream = ByteArrayOutputStream()

                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

                outputStream.writeTo(outputStream)

                val inputStream = ByteArrayInputStream(outputStream.toByteArray())

                val result = photoRef.putStream(inputStream)

                result.addOnCompleteListener { imageLoading ->

                    if (imageLoading.isSuccessful) {
                        val photoUrl = photoRef.downloadUrl

                        photoUrl.addOnCompleteListener {

                            if (it.isSuccessful) {
                                post.photoUrl = it.result.toString()

                                val addPostDocument =
                                    firestore.collection("posts_global").document(post.id!!)
                                        .set(post)

                                addPostDocument.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        cont.resume(Resource.Success(it.result)) { cause ->
                                            Resource.Error(cause.message!!, null)
                                        }
                                    }

                                    if (it.isCanceled) {
                                        cont.cancel(it.exception)
                                    }
                                }

                            } else {
                                cont.cancel(it.exception)
                            }

                        }

                    }
                }

                outputStream.close()
                inputStream.close()

            } ?: post.id?.let {
                Log.e("postId", it)
                val addPostDocument = firestore.collection("posts_global").document(it)
                    .set(post)

                addPostDocument.addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        cont.resume(Resource.Success(result.result)) { cause ->
                            Resource.Error(cause.message!!, null)
                        }
                    }
                }
            }


        }

    override suspend fun postComment(comment: Comment, postId: String): Resource<Void> {
        return suspendCancellableCoroutine { cont ->

            firestore.collection("posts_global").document(postId)
                .update("comments", FieldValue.arrayUnion(comment)).addOnCompleteListener {

                    if (it.isSuccessful) {
                        cont.resume(Resource.Success(it.result))
                    }

                    it.exception?.let { e ->
                        cont.resume(Resource.Error<Void>(e.message.toString()))
                    }

                    if (it.isCanceled) {
                        cont.cancel()
                    }

                }

        }

    }

    override suspend fun getPosts(): MutableLiveData<Resource<QuerySnapshot>> {
        val result = MutableLiveData<Resource<QuerySnapshot>>()

        firestore.collection("posts_global").addSnapshotListener { value, error ->

            error?.let {
                it.printStackTrace()
                Log.e("error fetching posts", it.message.toString())
                result.value = Resource.Error(it.message.toString())
            }

            value?.let {
                result.value = Resource.Success(it)

            }

        }
        return result
    }

    override suspend fun updateUserLocation(location: LocationCustom): Resource<Void> =
        suspendCancellableCoroutine { cont ->

            try {
                currentUser?.let {

                    userCollection.document(it.uid).update("location", location)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                cont.resume(value = Resource.Success(task.result))

                            }

                            task.exception?.let {

                                cont.resume(value = Resource.Error(it.message.toString()))

                            }

                            if (task.isCanceled) {

                                cont.cancel()

                            }

                        }

                }

            } catch (e: Exception) {

                e.printStackTrace()
                Log.e("Error updating location", e.message.toString())
                cont.resume(Resource.Error(e.message.toString()))

            }

        }

    override suspend fun getPostUpdates(postId: String): Flow<Resource<Post>> = callbackFlow {
        val listener = postsCollection.document(postId).addSnapshotListener { value, error ->

            error?.let {
                it.printStackTrace()
                launch {
                    send(Resource.Error(it.message.toString()))
                    close(it)
                }
            }

            value?.let {
                val post = it.toObject<Post>()
                launch {
                    Log.e(tag, "${post?.comments?.size}")
                    send(Resource.Success(post))
                }
            }

        }

        awaitClose {
            listener.remove()
            channel.close()
        }
    }

    override fun getUserUpdates(): Flow<Resource<List<User>>> = callbackFlow {
        val snapshotListener = userCollection.addSnapshotListener { value, error ->

            error?.let {
                it.printStackTrace()
                launch { send(Resource.Error(it.message.toString())) }
            }

            value?.let {

                val users = it.toObjects<User>()

                launch { send(Resource.Success(users)) }

            }

        }

        awaitClose {
            snapshotListener.remove()
            channel.close()

        }
    }

    override suspend fun getUserOnlineStatus(uid: String?): Resource<OnlineStatus> =
        suspendCancellableCoroutine { cont ->

            val userId = uid ?: currentUser?.uid
            userId?.let {
                firestore.collection("online_status").document(it).get()
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            val status = task.result.toObject<OnlineStatus>()
                            cont.resume(Resource.Success(status))

                        }

                        task.exception?.let { e ->
                            e.printStackTrace()
                            cont.resume(Resource.Error(e.message.toString()))
                        }

                    }
            }


        }

    override suspend fun likePost(postId: String,user:User?): Resource<Void> {
        return suspendCancellableCoroutine { cont ->
            user?.let {


                firestore.collection("posts_global").document(postId)
                    .update("likes", FieldValue.arrayUnion(user)).addOnCompleteListener {
                        if(it.isSuccessful) {
                            cont.resume(Resource.Success(it.result))
                        }
                        it.exception?.let {
                            it.printStackTrace()
                            cont.resume(Resource.Error(it.message.toString()))
                        }
                    }

            } ?: cont.resume(Resource.Error("$tag Failed to like post: User data is not available"))

        }
    }


}