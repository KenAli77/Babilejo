package devolab.projects.babilejo.ui.main.explore.components

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import devolab.projects.babilejo.R
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.main.explore.ExploreViewModel
import devolab.projects.babilejo.ui.theme.PearlWhite
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.util.bitmapFromVector
import kotlinx.coroutines.launch

const val TAG = "ExploreMapView"

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ExploreMapView(location: Location?, users: SnapshotStateList<User>) {

    val context = LocalContext.current

    with(context) {

        location?.let { location ->

            val currentLocation = LatLng(
                location.latitude,
                location.longitude
            )
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLocation, 14f)
            }

            val scope = rememberCoroutineScope()

            val uiSettings by remember {
                mutableStateOf(
                    MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = false
                    )
                )
            }
            val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

            Surface(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        properties = properties,
                        uiSettings = uiSettings,
                        cameraPositionState = cameraPositionState,

                        ) {

                        users.forEach { user ->
                            user.location?.let { userLocation ->

                                if (userLocation.latitude != null && userLocation.longitude != null) {
                                    val markerState = rememberMarkerState(
                                        position = LatLng(
                                            userLocation.latitude,
                                            userLocation.longitude
                                        )
                                    )

                                    Marker(
                                        state = markerState,
                                        icon = bitmapFromVector(R.drawable.ic_smiley_online),
                                        flat = true,
                                        anchor = Offset(0.5f, 0.5f)
                                    )
                                }
                            }


                        }

                    }

                    MyLocationButton(
                        boxScope = this,
                        modifier = Modifier,
                        onClick = {
                            scope.launch {
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newLatLng(currentLocation)
                                )
                            }
                        }
                    )


                }
            }
        }

    }
}

@Composable
fun MyLocationButton(
    modifier: Modifier = Modifier,
    boxScope: BoxScope,
    onClick: () -> Unit = {}
) {

    val scope = rememberCoroutineScope()

    boxScope.apply {
        IconButton(onClick = {
            scope.launch {
                onClick()
            }
        }, modifier = modifier.align(Alignment.BottomEnd)) {

            Surface(
                modifier = Modifier
                    .padding(10.dp),
                color = PearlWhite,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Rounded.MyLocation,
                    contentDescription = null,
                    tint = Yellow,
                    modifier = Modifier.padding(10.dp),
                )

            }

        }

    }


}

