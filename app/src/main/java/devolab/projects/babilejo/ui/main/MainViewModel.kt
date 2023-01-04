package devolab.projects.babilejo.ui.main

import android.app.Application
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.ui.main.home.state.HomeState
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.util.UserDataResponse
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MainRepositoryImpl,
    private val locationTracker: DefaultLocationTracker,
    private val app: Application,
) : ViewModel() {

    data class LocationState(
        val location: Location? = null,
        val loading: Boolean = false,
        val error: String? = null
    )

    var locationState by mutableStateOf(LocationState())
        private set

    val liveLocation = MutableLiveData<Location>()

    var lastKnownPosition by mutableStateOf(Location(""))

    var userDataState by mutableStateOf<UserDataResponse>(Resource.Success(null))


    var place by mutableStateOf("")

    init {
        getUserData()
        getLastKnownPosition()
        getPositionUpdates()
        getPLace()
    }


    private fun getUserData() = viewModelScope.launch {
        userDataState = Resource.Loading()
        userDataState = repo.getUserData()
    }


    private fun getPositionUpdates() = viewModelScope.launch {
        locationTracker.getLocationUpdates().onCompletion {
            cancel()
        }.collect { result ->
            locationState = locationState.copy(
                loading = true
            )

            when (result) {

                is Resource.Error -> {
                    Log.e("MainViewModel","location: ${result.message}")

                    locationState = locationState.copy(
                        location = null,
                        error = result.message,
                        loading = false
                    )
                }
                is Resource.Loading -> {
                    locationState = locationState.copy(
                        loading = true,
                    )
                }
                is Resource.Success -> {
                    Log.e("MainViewModel","location: ${result.data}")
                    locationState = locationState.copy(
                        location = result.data,
                        loading = false,
                        error = null
                    )

                    liveLocation.postValue(result.data)
                }
            }

        }

    }

    private fun getLastKnownPosition() = viewModelScope.launch {
        val location = locationTracker.getCurrentLocation()

        location?.let {
            lastKnownPosition = it
        }

    }


    fun getPLace() = viewModelScope.launch {
        val result = locationTracker.getCurrentPlace()

        when (result) {
            is Resource.Error -> {
                Log.e("error fetching place", result.message.toString())
            }
            is Resource.Loading -> {

                Log.e("Place", "Loading...")
            }
            is Resource.Success -> {
                result.data?.let {

                    val placeResult = it.placeLikelihoods[0].place

                    placeResult.name?.let {

                        place = it
                        Log.e("place", it)

                    }

                }


            }
        }
    }

}