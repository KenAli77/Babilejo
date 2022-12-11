package devolab.projects.babilejo.ui.main.home.components

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Javascript
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@Composable
fun MainMapView(currentLocation: Location, lastKnownLocation: Location) {
    val positionState = rememberCameraPositionState(
        init = {
            CameraPosition(
                LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude),
                14f,
                0f,
                0f
            )
        }
    )

    val scope = rememberCoroutineScope()


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(zoomControlsEnabled = false),
        cameraPositionState = positionState,
        onMapLoaded = {
            scope.launch {
                positionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(currentLocation.latitude, currentLocation.longitude),
                            14.0f,
                            0f,
                            0f
                        )
                    )
                )
            }
        }

    ) {

    }
}