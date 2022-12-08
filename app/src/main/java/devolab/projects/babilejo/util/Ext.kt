package devolab.projects.babilejo.util

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import devolab.projects.babilejo.domain.model.User

fun FirebaseUser.toUser() = User(
    uid = uid,
    displayName,
    userEmail = email,
    photoUrl = photoUrl?.toString(),
    created = FieldValue.serverTimestamp()
)

