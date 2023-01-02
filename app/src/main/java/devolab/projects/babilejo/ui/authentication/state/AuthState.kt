package devolab.projects.babilejo.ui.authentication.state

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthResult


data class AuthState(
    val success:Boolean = false,
    val loading:Boolean = false,
    val error: String? = null,
    val authData:AuthResult? = null,
    val oneTapData:BeginSignInResult? = null,
)
