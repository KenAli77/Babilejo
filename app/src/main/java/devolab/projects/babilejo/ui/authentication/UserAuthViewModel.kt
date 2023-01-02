package devolab.projects.babilejo.ui.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.UserAuthRepositoryImpl
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.authentication.state.AuthState
import devolab.projects.babilejo.util.AuthResponse
import devolab.projects.babilejo.util.LoginWithGoogleResponse
import devolab.projects.babilejo.util.OneTapLoginResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val repo: UserAuthRepositoryImpl,
    val oneTapClient: SignInClient,
    private val userProfileRepo: MainRepositoryImpl
) : ViewModel() {

    var authState by mutableStateOf(AuthState())
        private set

    var signUpState by mutableStateOf(AuthState())

    fun logInUser(email: String, password: String) =
        viewModelScope.launch {
            authState = AuthState(loading = true)

            val result = repo.emailLogin(email, password)

            when (result) {
                is Resource.Error -> {
                    authState = authState.copy(
                        loading = false,
                        error = result.message,
                        success = false,
                        authData = null
                    )
                }
                is Resource.Loading -> {
                    authState = authState.copy(
                        success = false,
                        loading = true,
                        error = null,
                        authData = null
                    )
                }
                is Resource.Success -> {
                    result.data?.let {
                        authState = authState.copy(
                            success = true,
                            error = null,
                            loading = false,
                            authData = it
                        )
                    }
                }
            }

        }

    fun isUserAuthenticated(): Boolean {
        return repo.isUserAuthenticated()
    }

    fun createUser(
        email: String,
        password: String,
        userName: String,
        confirmPassword: String
    ) = viewModelScope.launch {
        viewModelScope.launch {
            signUpState = AuthState(loading = true)

            val result = repo.emailSignUp(
                userEmailAddress = email,
                userLoginPassword = password,
                userName = userName,
                confirmPassword = confirmPassword
            )

            when (result) {
                is Resource.Error -> {
                    signUpState = signUpState.copy(
                        loading = false,
                        error = result.message,
                        success = false,
                        authData = null
                    )
                }
                is Resource.Loading -> {
                    signUpState = signUpState.copy(
                        success = false,
                        loading = true,
                        error = null,
                        authData = null
                    )
                }
                is Resource.Success -> {
                    result.data?.let {
                        signUpState = signUpState.copy(
                            success = true,
                            error = null,
                            loading = false,
                            authData = it
                        )
                    }
                }
            }

        }

    }

    fun oneTapSignIn() = viewModelScope.launch {

        authState = AuthState(loading = true)

        val result = repo.oneTapSignInWithGoogle()

        when (result) {
            is Resource.Error -> {
                authState = authState.copy(
                    loading = false,
                    error = result.message,
                    success = false,
                    authData = null,
                    oneTapData = null
                )
            }
            is Resource.Loading -> {
                authState = authState.copy(
                    success = false,
                    loading = true,
                    error = null,
                    authData = null,
                    oneTapData = null
                )
            }
            is Resource.Success -> {
                result.data?.let {
                    authState = authState.copy(
                        success = true,
                        error = null,
                        loading = false,
                        authData = null,
                        oneTapData = it
                    )
                }
            }
        }

    }


    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {

        authState = AuthState(loading = true)

        val result = repo.firebaseSignInWithGoogle(googleCredential)

        when (result) {
            is Resource.Error -> {
                authState = authState.copy(
                    loading = false,
                    error = result.message,
                    success = false,
                    authData = null
                )
            }
            is Resource.Loading -> {
                authState = authState.copy(
                    success = false,
                    loading = true,
                    error = null,
                    authData = null
                )
            }
            is Resource.Success -> {
                result.data?.let {
                    authState = authState.copy(
                        success = true,
                        error = null,
                        loading = false,
                        authData = it,
                        oneTapData = null
                    )
                }
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            repo.logOut()
        }
        resetState()
    }

    fun resetState(){
        authState = authState.copy(
            success = false,
            authData = null,
            error = null,
            oneTapData = null
        )
        signUpState = signUpState.copy(
            success = false,
            authData = null,
            error = null,
            oneTapData = null
        )
    }
}





