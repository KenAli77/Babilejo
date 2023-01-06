package devolab.projects.babilejo.util

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import java.text.SimpleDateFormat
import java.util.*

fun getTimestampedFileName(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-DD_HH-mm-ss", Locale.getDefault())
    val date = Date(timestamp)
    return "IMG_${dateFormat.format(date)}"
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}