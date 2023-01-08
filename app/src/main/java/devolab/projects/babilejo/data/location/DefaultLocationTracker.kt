package devolab.projects.babilejo.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import devolab.projects.babilejo.domain.location.LocationTracker
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.util.LocationResponse
import devolab.projects.babilejo.util.getAddress
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultLocationTracker @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application,
    private val placesClient: PlacesClient,
    private val geocoder: Geocoder

) : LocationTracker {

    companion object {
        const val TAG = "DefaultLocationTracker"
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)


    private val locationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    private val isNetworkEnabled =
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    private lateinit var locationRequest: LocationRequest
    private var location = MutableStateFlow<Location?>(null)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {

        val hasPermissionCoarseLocation = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasPermissionFineLocation = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


        if (!hasPermissionCoarseLocation || !hasPermissionFineLocation || !isGpsEnabled) {
            return null
        }

        return suspendCancellableCoroutine { cont ->

            fusedLocationProviderClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        scope.launch {
                            location.emit(result)
                        }
                        Log.e(TAG, location.value.toString())

                        cont.resume(result)
                    } else {
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }

                addOnSuccessListener {
                    cont.resume(result)

                }

                addOnFailureListener {
                    cont.resume(null)
                    Log.e("Failed to get location", it.message.toString())
                }
                addOnCanceledListener {
                    cont.cancel()
                    Log.e("cancelled", "LocationCustom fetch cancelled")
                }
            }

        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLocationUpdates(): LocationResponse = callbackFlow {

        val hasPermissionCoarseLocation = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasPermissionFineLocation = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        var locationCallback: LocationCallback? = null
        if (hasPermissionCoarseLocation && hasPermissionFineLocation && (isGpsEnabled || isNetworkEnabled)) {

            locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000,
            ).build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    // Update the user's location
                    result.locations.lastOrNull()?.let { loc ->
                        launch {
                            location.emit(loc)
                            Log.e(TAG, location.value.toString())
                            send(Resource.Success(loc))
                        }
                    }

                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            ).addOnFailureListener {
                launch { send(Resource.Error(it.message.toString())) }
            }


        } else {
            send(Resource.Error("permissions not granted"))
        }

        awaitClose {

            locationCallback?.let { fusedLocationProviderClient.removeLocationUpdates(it) }
            channel.close()
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentPlace(): Resource<FindCurrentPlaceResponse> {

        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        return suspendCancellableCoroutine { cont ->
            val result = placesClient.findCurrentPlace(request)
            result.addOnFailureListener {
                cont.resume(Resource.Error(it.message.toString()))
            }
            result.addOnSuccessListener {
                cont.resume(Resource.Success(it))
            }
            result.addOnCanceledListener {
                cont.cancel()
            }

        }

    }

    @OptIn(FlowPreview::class)
    override suspend fun getCurrentAddress(): String {
        return suspendCancellableCoroutine { cont ->
            Log.e(TAG, location.value.toString())

            scope.launch {

                location.collect {

                    it?.let { loc ->

                        Log.e(TAG, location.toString())

                        geocoder.getAddress(
                            loc.latitude,
                            loc.longitude
                        ) { addresses: android.location.Address? ->

                            addresses?.let {
                                val address = it.thoroughfare
                                val city = it.locality
                                val state = it.adminArea
                                val country = it.countryName
                                val postalCode = it.postalCode
                                val knownName = it.featureName

                                Log.e(TAG, "state: $state")
                                Log.e(TAG, "country: $country")
                                Log.e(TAG, "knownName: $knownName")
                                Log.e(TAG, "city: $city")
                                Log.e(TAG, "postalCode: $postalCode")
                                cont.resume(address)
                            }

                        }

                    }

                }

            }

        }
    }

}