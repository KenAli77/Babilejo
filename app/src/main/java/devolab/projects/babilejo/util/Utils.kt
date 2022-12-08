package devolab.projects.babilejo.util

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User


typealias OneTapLoginResponse = Resource<BeginSignInResult>
typealias LoginWithGoogleResponse = Resource<Boolean>
typealias AuthResponse = Resource<AuthResult>
typealias SignOutResponse = Resource<Boolean>
typealias RevokeAccessResponse = Resource<Boolean>
typealias UserDataResponse = Resource<User>

fun FirebaseUser.toUser() = User(
    uid = uid,
    displayName,
    userEmail = email,
    photoUrl = photoUrl?.toString(),
    created = FieldValue.serverTimestamp()
)

