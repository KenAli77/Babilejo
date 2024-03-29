package devolab.projects.babilejo.domain.repository

import com.google.firebase.auth.AuthCredential
import devolab.projects.babilejo.util.AuthResponse
import devolab.projects.babilejo.util.LoginWithGoogleResponse
import devolab.projects.babilejo.util.OneTapLoginResponse
import kotlinx.coroutines.flow.Flow

interface UserAuthRepository {

    suspend fun oneTapSignInWithGoogle(): OneTapLoginResponse

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): AuthResponse

    suspend fun emailSignUp(
        userName: String,
        userEmailAddress: String,
        userLoginPassword: String,
        confirmPassword:String
    ): AuthResponse

    suspend fun emailLogin(email: String, password: String): AuthResponse

    suspend fun logOut()

    fun isUserAuthenticated(): Flow<Boolean>



}