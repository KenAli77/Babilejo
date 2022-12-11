package devolab.projects.babilejo.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
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
import devolab.projects.babilejo.domain.location.LocationTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultLocationTracker @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application

):LocationTracker {

    companion object{
        const val TAG = "DefaultLocationTracker"
    }

    private val locationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    private lateinit var locationRequest:LocationRequest
    private var location: Location? = null


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
                    Log.e("cancelled","Location fetch cancelled")
                }
            }

        }
    }

    override suspend fun getLocationUpdates(): Flow<Location> = flow {

            while (true) {

                val hasPermissionCoarseLocation = ContextCompat.checkSelfPermission(
                    application, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                val hasPermissionFineLocation = ContextCompat.checkSelfPermission(
                    application, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermissionCoarseLocation && hasPermissionFineLocation) {

                    locationRequest = LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        1000,
                    ).build()

                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        object : LocationCallback() {
                            override fun onLocationResult(result: LocationResult) {
                                super.onLocationResult(result)
                                // Update the user's location
                                result.lastLocation?.let {
                                    location = it

                                }

                            }
                        },
                        Looper.getMainLooper()
                    ).addOnFailureListener {
                        Log.e(TAG, it.message.toString())
                    }

                    location?.let {
                        emit(it)
                    }
                }
            }
    }.flowOn(Dispatchers.IO)
}