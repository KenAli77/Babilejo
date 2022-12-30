package devolab.projects.babilejo.ui.main.explore

import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.repository.MainRepository
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationTracker: DefaultLocationTracker,
    private val mainRepository: MainRepository,
    private val app: Application,
) : ViewModel() {

    var currentPosition by mutableStateOf<Resource<Location>>(Resource.Loading())
        private set

    var lastKnownPosition by mutableStateOf(Location(""))

    var locality by mutableStateOf("")

    init {
        viewModelScope.launch {
            getPositionUpdates()
            getLastKnownPosition()

        }
    }


    private fun getPositionUpdates() = viewModelScope.launch {
        locationTracker.getLocationUpdates().collect { result ->

            currentPosition = when (result) {
                is Resource.Error -> {
                    Resource.Error(result.message.toString())
                }
                is Resource.Loading -> {
                    Resource.Loading()
                }
                is Resource.Success -> {
                    Resource.Success(result.data)
                }
            }

        }

    }

    private fun getLastKnownPosition() = viewModelScope.launch {
        val location = locationTracker.getCurrentLocation()

        location?.let {
            lastKnownPosition = it
            locality = getLocalityFromLocation(it.toLocation())
        }

    }

    fun getLocalityFromLocation(location: devolab.projects.babilejo.domain.model.Location): String {
        val geocoder = Geocoder(app, Locale.ENGLISH)
        val address = geocoder.getFromLocation(location.latitude!!, location.longitude!!, 1)

        return address?.get(0)?.locality.toString()
    }
}