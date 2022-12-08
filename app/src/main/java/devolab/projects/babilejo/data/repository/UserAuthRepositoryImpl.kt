package devolab.projects.babilejo.data.repository

import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.repository.UserAuthRepository
import devolab.projects.babilejo.util.*
import devolab.projects.babilejo.util.SIGN_IN_REQUEST
import devolab.projects.babilejo.util.SIGN_UP_REQUEST
import devolab.projects.babilejo.util.USERS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class UserAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : UserAuthRepository {

    override val isUserAuthenticatedInFirebase: Boolean
        get() = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle(): OneTapLoginResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Resource.Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Resource.Success(signUpResult)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage.toString())
            }

        }
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): LoginWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFirestore()
            }
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage.toString())
        }
    }

    override suspend fun emailSignUp(
        userName: String,
        userEmailAddress: String,
        userLoginPassword: String,
        confirmPassword:String,
    ): AuthResponse {
        return try {

            if(confirmPassword!==userLoginPassword){
               return Resource.Error("passwords are not matching")

            }
            val registrationResult =
                auth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword)
                    .await()

            addUserToFirestore()

            Resource.Success(registrationResult)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun emailLogin(email: String, password: String): AuthResponse {
        return try {
            val loginResult = auth.signInWithEmailAndPassword(email, password).await()
            Log.e("login", "logged in user ${loginResult.user?.uid}")
            Resource.Success(loginResult)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    private suspend fun addUserToFirestore() {
        auth.currentUser?.apply {
            val user = toUser()
            db.collection(USERS).document(uid).set(user).await()
        }
    }

}