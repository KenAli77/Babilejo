package devolab.projects.babilejo.util

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.*

fun getTimestampedFileName(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-DD_HH-mm-ss", Locale.getDefault())
    val date = Date(timestamp)
    return "IMG_${dateFormat.format(date)}"
}

