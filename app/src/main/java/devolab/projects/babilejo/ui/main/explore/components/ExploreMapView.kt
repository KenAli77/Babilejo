package devolab.projects.babilejo.ui.main.explore.components

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun ExploreMapView(location: Location, lastKnownPosition: Location) {

    val position = rememberSaveable {
        CameraPosition(
            LatLng(location.latitude, location.longitude),
            14.0f,
            0f,
            0f
        )
    }
    val positionState = rememberCameraPositionState(
        init = {
            CameraPosition.fromLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 14f
            )
        },

        )

    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(zoomControlsEnabled = false),
                cameraPositionState = positionState,
                onMapLoaded = {
                    scope.launch {
                        positionState.move(
                            update = CameraUpdateFactory.newCameraPosition(
                                CameraPosition(
                                    LatLng(location.latitude, location.longitude),
                                    14.0f,
                                    0f,
                                    0f
                                )
                            )
                        )
                    }
                },


                ) {

            }

        }
    }
}