package devolab.projects.babilejo.ui.viewmodel

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.MainActivity
import devolab.projects.babilejo.data.model.AuthState
import devolab.projects.babilejo.domain.LoginUser
import devolab.projects.babilejo.domain.SignUp
import devolab.projects.babilejo.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val loginUser: LoginUser,
    private val signUpUser: SignUp,
) : ViewModel() {

    var loginState by mutableStateOf(AuthState())

    var googleLoginState by mutableStateOf(AuthState())

    var signUpState by mutableStateOf(AuthState())
        private set

    var googleLoginLauncher = MutableLiveData<ActivityResultLauncher<Intent>>()
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    fun logInUser(email: String, password: String) =
        viewModelScope.launch {

            val result = loginUser(email, password)
            loginState = loginState.copy(
                loading = true,
                error = null,
                data = null,
                success = false
            )

            loginState = when (result) {
                is Resource.Error -> {
                    loginState.copy(
                        loading = false,
                        success = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {
                    loginState.copy(
                        loading = true,
                        success = false,
                        error = null
                    )
                }
                is Resource.Success -> {

                    loginState.copy(
                        data = result.data,
                        success = true,
                        loading = false,
                        error = null,
                        uid = result.data?.user?.uid
                    )
                }
            }
        }


    fun createUser(
        email: String,
        password: String,
        userName: String,
        confirmPassword: String
    ) = viewModelScope.launch {

        val result = signUpUser(
            email = email,
            password = password,
            userName = userName,
            confirmPassword = confirmPassword
        )

        when (result) {
            is Resource.Error -> {
                signUpState = signUpState.copy(
                    data = null,
                    error = result.message,
                    loading = false,
                    success = false,
                    uid = null
                )
            }
            is Resource.Loading -> {
                signUpState = signUpState.copy(
                    data = null,
                    loading = true,
                    error = null,
                    success = false,
                )
            }
            is Resource.Success -> {
                signUpState = signUpState.copy(
                    data = result.data,
                    success = true,
                    loading = false,
                    error = null,
                    uid = result.data?.user?.uid
                )
            }
        }
    }

    fun resetLoginState() {
        loginState = loginState.copy(
            data = null,
            error = null,
            success = false
        )
    }

    fun resetSignUpState() {
        signUpState = signUpState.copy(
            data = null,
            error = null,
            success = false
        )
    }

    fun googleLogin() = viewModelScope.launch {
        googleSignInClient.value?.signInIntent.also {
            googleLoginLauncher.value?.launch(it)
        }
    }

}





