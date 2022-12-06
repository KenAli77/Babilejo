package devolab.projects.babilejo.data.repository.interfaces

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import devolab.projects.babilejo.util.Resource

interface UserDataRepository {

    suspend fun createNewUser(
        userName: String,
        userEmailAddress: String,
        userLoginPassword: String,
        confirmPassword:String
    ): Resource<AuthResult>

    suspend fun loginUser(email: String, password: String): Resource<AuthResult>
    suspend fun googleLogin(account: GoogleSignInAccount): Resource<AuthResult>

    suspend fun logOutUser()

}