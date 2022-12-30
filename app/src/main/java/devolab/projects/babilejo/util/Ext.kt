package devolab.projects.babilejo.util

import android.location.Location
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


fun getTimeAgo(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val timeDifference = currentTime - timestamp
    val seconds = timeDifference / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    val hoursString = if(hours<2)"hour" else "hours"
    return when {
        seconds < 60 -> "just now"
        minutes < 60 -> "$minutes minutes ago"
        hours < 2 -> "$hours hour ago"
        hours < 24 -> "$hours $hoursString ago"
        else -> "$days days ago"
    }
}

// this function is necessary to store the location data to firebase and retrieve it
// it transforms the location object to a custom location object with no parameters constructors
fun Location.toLocation(): devolab.projects.babilejo.domain.model.Location {
    return devolab.projects.babilejo.domain.model.Location(
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        accuracy = accuracy,
        bearing = bearing,
        speed = speed,
        time = time

    )
}