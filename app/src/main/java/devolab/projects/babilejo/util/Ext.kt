package devolab.projects.babilejo.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider.getUriForFile
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.auth.FirebaseUser
import devolab.projects.babilejo.domain.model.User
import java.io.File

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

    val hoursString = if (hours < 2) "hour" else "hours"
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

fun Context.getImageUri(): Uri {

    val directory = File(cacheDir, "images")
    directory.mkdirs()

    val file = File.createTempFile(
        "selected_image_",
        ".jpg",
        directory
    )

    val authority = "$packageName.fileProvider"

    return getUriForFile(
        this,
        authority,
        file,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !status.isGranted && !status.shouldShowRationale
}


fun Context.openAppSystemSettings() {
    startActivity(Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    })
}

fun Context.getBitmapFromUri(uri: Uri): Bitmap =
    if (Build.VERSION.SDK_INT < 28) {
    MediaStore.Images.Media.getBitmap(contentResolver, uri)
} else {
    val source = ImageDecoder.createSource(contentResolver, uri)
    ImageDecoder.decodeBitmap(source)
    // BitmapFactory.decodeFile(imageUri!!.path)
}