package devolab.projects.babilejo.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.domain.repository.MainRepository
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.util.*
import devolab.projects.babilejo.util.USERS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInClient: GoogleSignInClient,
    private val db: FirebaseFirestore,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : MainRepository {
    override val displayName: String
        get() = auth.currentUser?.displayName.toString()
    override val photoUrl: String
        get() = auth.currentUser?.photoUrl.toString()

    private val userCollection = Firebase.firestore.collection("users")

    override val currentUserId = auth.currentUser?.uid

    override suspend fun getUserData(uid: String?): UserDataResponse {
        return try {
            val user = db.collection(USERS).document(uid!!).get().await()

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

    override suspend fun addPost(post: Post, imageUri: Uri?) {
        withContext(Dispatchers.IO) {
            try {
                imageUri?.let {
                    val photoId = UUID.randomUUID()
                    val photoRef = storage.reference.child("photos/${photoId}")

                    val result = photoRef.putFile(imageUri).await()
                    if (result.task.isSuccessful) {
                        val photoUrl = photoRef.downloadUrl.result
                        post.id?.let { postId ->
                            post.photoUrl = photoUrl.toString()

                            firestore.collection("posts_global").document(postId)
                                .set(post).await()

                        }
                    }

                } ?: post.id?.let {
                    firestore.collection("posts_global").document(it)
                        .set(post).await()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                print(e.message.toString())
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


}