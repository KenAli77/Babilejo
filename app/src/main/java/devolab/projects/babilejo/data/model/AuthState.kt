package devolab.projects.babilejo.data.model

import com.google.firebase.auth.AuthResult

data class AuthState (
    val data: AuthResult? = null,
    val loading: Boolean = false,
    val success:Boolean = false,
    val error: String? = null,
    val uid:String? = null
)
