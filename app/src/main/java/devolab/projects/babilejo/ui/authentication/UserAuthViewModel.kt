package devolab.projects.babilejo.ui.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.UserAuthRepositoryImpl
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.util.AuthResponse
import devolab.projects.babilejo.util.LoginWithGoogleResponse
import devolab.projects.babilejo.util.OneTapLoginResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val repo: UserAuthRepositoryImpl,
    val oneTapClient: SignInClient
) : ViewModel() {

    var loginState by mutableStateOf<AuthResponse>(Resource.Success(null))
        private set

    var googleLoginState by mutableStateOf<OneTapLoginResponse>(Resource.Success(null))
        private set

    var signInWithGoogleResponse by mutableStateOf<LoginWithGoogleResponse>(Resource.Success(false))
        private set

    var signUpState by mutableStateOf<AuthResponse>(Resource.Success(null))
        private set

    fun logInUser(email: String, password: String) =
        viewModelScope.launch {
            loginState = Resource.Loading()
            loginState = repo.emailLogin(email, password)

        }


    fun createUser(
        email: String,
        password: String,
        userName: String,
        confirmPassword: String
    ) = viewModelScope.launch {

        loginState = Resource.Loading()
        loginState = repo.emailSignUp(userEmailAddress = email,
            userLoginPassword = password,
            userName = userName,
            confirmPassword = confirmPassword)

    }

    fun oneTapSignIn() = viewModelScope.launch {
        googleLoginState = Resource.Loading()
        googleLoginState = repo.oneTapSignInWithGoogle()

    }

    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        signInWithGoogleResponse = Resource.Loading()
        signInWithGoogleResponse = repo.firebaseSignInWithGoogle(googleCredential)
    }

}





