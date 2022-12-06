package devolab.projects.babilejo.data.repository.implementation

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import devolab.projects.babilejo.data.model.User
import devolab.projects.babilejo.data.repository.interfaces.UserDataRepository
import devolab.projects.babilejo.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : UserDataRepository {

    private val fireStoreUserCollection = Firebase.firestore.collection("users")

    override suspend fun createNewUser(
        userName: String,
        userEmailAddress: String,
        userLoginPassword: String,
        confirmPassword:String,
    ): Resource<AuthResult> {
        return try {

            val registrationResult =
                auth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword)
                    .await()

            val userId = registrationResult.user?.uid!!
            val newUser = User(
                userName = userName,
                userEmail = userEmailAddress
            )
            fireStoreUserCollection.document(userId).set(newUser).await()

            Resource.Success(registrationResult)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.e("login", "logged in user ${result.user?.uid}")
            Resource.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun googleLogin(account: GoogleSignInAccount): Resource<AuthResult> {

        return try {
            val credentials = GoogleAuthProvider.getCredential(account.idToken,null)
            val result = auth.signInWithCredential(credentials).await()
            Log.e("login", "logged in user ${result.user?.uid}")
            Resource.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }


    override suspend fun logOutUser() {
        auth.signOut()
    }
}