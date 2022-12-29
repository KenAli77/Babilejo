package devolab.projects.babilejo.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import devolab.projects.babilejo.domain.model.User

fun FirebaseUser.toUser() = User(
    uid = uid,
    displayName = displayName,
    userEmail = email,
    photoUrl = photoUrl?.toString(),
)

val TOP_BAR_HEIGHT = 65.dp

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0