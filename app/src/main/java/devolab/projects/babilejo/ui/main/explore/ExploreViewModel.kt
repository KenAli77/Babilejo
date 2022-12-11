package devolab.projects.babilejo.ui.main.explore

import android.location.Location
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val locationTracker: DefaultLocationTracker,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    var currentPosition by mutableStateOf(Location(""))
        private set

    var lastKnownPosition by mutableStateOf(Location(""))


    init {
        viewModelScope.launch {
            getPositionUpdates()
            getLastKnownPosition()
        }
    }

    private fun getPositionUpdates() = viewModelScope.launch {
        locationTracker.getLocationUpdates().collect {
            currentPosition = it
        }

    }

    private fun getLastKnownPosition() = viewModelScope.launch {
        val location = locationTracker.getCurrentLocation()

        location?.let {
            lastKnownPosition = it
        }

    }
}