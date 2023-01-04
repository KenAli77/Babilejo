package devolab.projects.babilejo.data.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import devolab.projects.babilejo.domain.model.OnlineStatus
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.domain.repository.UserAuthRepository
import devolab.projects.babilejo.util.*
import devolab.projects.babilejo.util.SIGN_IN_REQUEST
import devolab.projects.babilejo.util.SIGN_UP_REQUEST
import devolab.projects.babilejo.util.USERS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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

    private val TAG = "UserAuthRepositoryImpl"

    override fun isUserAuthenticated(callback: (Boolean) -> Unit) {

        auth.addAuthStateListener {
            callback(it.currentUser != null)
        }

    }

    override suspend fun oneTapSignInWithGoogle(): OneTapLoginResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()

            updateUserOnlineStatus(true)

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

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): AuthResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                val user = auth.currentUser?.toUser()
                addUserToFirestore(user!!)
            }

            authResult.user?.let {
                updateUserOnlineStatus(true)
            }

            Resource.Success(authResult)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage.toString())
        }
    }

    override suspend fun emailSignUp(
        userName: String,
        userEmailAddress: String,
        userLoginPassword: String,
        confirmPassword: String,
    ): AuthResponse {
        return try {

            if (confirmPassword != userLoginPassword) {
                return Resource.Error("passwords are not matching")

            }
            if (userName.isEmpty() || userEmailAddress.isEmpty()) {
                return Resource.Error("please all required fields")
            }
            val registrationResult =
                auth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword)
                    .await()

            val user = User(
                uid = auth.currentUser?.uid,
                userName = userName,
                userEmail = userEmailAddress,
                displayName = auth.currentUser?.displayName,
                photoUrl = auth.currentUser?.photoUrl.toString()
            )
            addUserToFirestore(user)

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
            loginResult.user?.let {
                updateUserOnlineStatus(true)
            }
            Resource.Success(loginResult)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun logOut() {

        updateUserOnlineStatus(false)
        auth.signOut()
        oneTapClient.signOut()

    }

    private suspend fun updateUserOnlineStatus(active: Boolean) {

        withContext(Dispatchers.IO) {

            auth.currentUser?.let { user ->

                val doc = db.collection("online_status").document(user.uid)
                val docRef = doc.get().await()

                if (docRef.exists()) {
                    if (active) {

                        doc.update(
                            mapOf(
                                "online" to true,
                                "lastOnline" to null
                            )
                        ).await()

                    } else {
                        Log.e(TAG, "online status $active")
                        doc.update(
                            mapOf(
                                "online" to false,
                                "lastOnline" to System.currentTimeMillis()
                            )
                        )
                    }
                } else {

                    val status: OnlineStatus =
                        if (active) {
                            OnlineStatus(
                                online = true,
                                lastOnline = null
                            )

                        } else {

                            OnlineStatus(
                                online = false,
                                lastOnline = System.currentTimeMillis()
                            )

                        }

                    doc.set(status).await()
                }

            }

        }

    }


    private suspend fun addUserToFirestore(user: User) {
        auth.currentUser?.apply {

            db.collection(USERS).document(uid).set(user).await()
        }
    }

}