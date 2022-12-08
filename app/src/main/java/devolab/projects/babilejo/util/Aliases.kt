package devolab.projects.babilejo.util

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthResult
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User


typealias OneTapLoginResponse = Resource<BeginSignInResult>
typealias LoginWithGoogleResponse = Resource<Boolean>
typealias AuthResponse = Resource<AuthResult>
typealias SignOutResponse = Resource<Boolean>
typealias RevokeAccessResponse = Resource<Boolean>
typealias UserDataResponse = Resource<User>

