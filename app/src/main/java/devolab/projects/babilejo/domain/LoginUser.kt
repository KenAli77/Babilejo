package devolab.projects.babilejo.domain

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthResult
import devolab.projects.babilejo.R
import devolab.projects.babilejo.data.repository.interfaces.UserDataRepository
import devolab.projects.babilejo.util.Resource
import javax.inject.Inject

class LoginUser @Inject constructor(
    private val userDataRepository: UserDataRepository
) {



    suspend operator fun invoke(email: String, password: String): Resource<AuthResult> {
        return userDataRepository.loginUser(email = email, password = password)
    }

    suspend operator fun invoke(account: GoogleSignInAccount):Resource<AuthResult>{

        return userDataRepository.googleLogin(account)
    }
}