package devolab.projects.babilejo.ui.main.explore

import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.repository.MainRepository
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.explore.state.ExploreState
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val mainViewModel: MainViewModel,
    private val app: Application,
) : ViewModel() {

    var currentPosition by mutableStateOf(MainViewModel.LocationState())
        private set

    var location = mainViewModel.liveLocation

    var lastKnownPosition by mutableStateOf(Location(""))

    var state by mutableStateOf(ExploreState())
        private set


    fun getLocalityFromLocation(location: devolab.projects.babilejo.domain.model.Location): String {
        val geocoder = Geocoder(app, Locale.ENGLISH)
        val address = geocoder.getFromLocation(location.latitude!!, location.longitude!!, 1)

        return address?.get(0)?.locality.toString()
    }

    fun fetchPosition(){

        state = state.copy(
            loading = true
        )

        currentPosition.error?.let {
            state = state.copy(
                error = it,
                loading = false,
                currentLocation = null
            )
        }
        currentPosition.location?.let { location->
            state = state.copy(
                currentLocation = location.toLocation(),
                lastKnownLocation = lastKnownPosition.toLocation(),
                error = null,
                loading = false

            )
        }


    }


}