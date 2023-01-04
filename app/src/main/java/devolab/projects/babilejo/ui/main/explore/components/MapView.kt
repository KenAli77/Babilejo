package devolab.projects.babilejo.ui.main.explore.components

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Javascript
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import devolab.projects.babilejo.R
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@Composable
fun MainMapView(currentLocation: Location, lastKnownLocation: Location) {
    Log.e("location", "${lastKnownLocation.latitude} ${lastKnownLocation.longitude}")

}