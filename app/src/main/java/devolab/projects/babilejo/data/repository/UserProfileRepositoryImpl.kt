package devolab.projects.babilejo.data.repository

import android.util.Log
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.domain.repository.UserProfileRepository
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.util.*
import devolab.projects.babilejo.util.USERS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInClient: GoogleSignInClient,
    private val db: FirebaseFirestore
) : UserProfileRepository {
    override val displayName: String
        get() = auth.currentUser?.displayName.toString()
    override val photoUrl: String
        get() = auth.currentUser?.photoUrl.toString()

    override suspend fun getUserData(): UserDataResponse {
        return try {
            val user = db.collection(USERS).document(auth.currentUser!!.uid).get().await()

            Log.e("user data",user.toObject<User>()?.userName.toString())
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


}