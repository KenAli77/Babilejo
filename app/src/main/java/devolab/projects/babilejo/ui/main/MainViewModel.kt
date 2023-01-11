package devolab.projects.babilejo.ui.main

import android.location.Location
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.filters.PostCategory
import devolab.projects.babilejo.domain.model.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MainRepositoryImpl,
    private val locationTracker: DefaultLocationTracker,

) : ViewModel() {

    private val TAG = "MainViewModel"

    data class LocationState(
        val location: Location? = null,
        val loading: Boolean = false,
        val error: String? = null
    )

    var locationState by mutableStateOf(LocationState())
        private set

    var selectedPost by mutableStateOf(Post())
        private set

    var liveLocation = MutableStateFlow<Location?>(null)
        private set
    var lastKnownPosition by mutableStateOf(Location(""))

    var users = mutableStateListOf<User>()

    var userData by mutableStateOf<User?>(null)

    var place by mutableStateOf<String?>(null)

    var filters = mutableSetOf<PostCategory>()
        private set

    var userStatus by mutableStateOf(OnlineStatus())
        private set

    var selectedLocation by mutableStateOf(LocationCustom())
    private set


    fun applyFilters(items: Set<PostCategory>) {
        filters = items as MutableSet<PostCategory>
    }

    init {
        getLastKnownPosition()
        getPositionUpdates()
        getPLace()
        getUserUpdates()
        getUserData()

        Log.e(TAG, "user: ${userData?.userName}")

    }

    fun getUserData() = viewModelScope.launch {
        userData = null

        val result = repo.getUserData()

        when (result) {
            is Resource.Error -> {

            }
            is Resource.Loading -> {

            }
            is Resource.Success -> {
                result.data?.let {
                    userData = it
                }
            }
        }


    }

    fun getUserUpdates() = repo.getUserUpdates()

    private fun getPositionUpdates() = viewModelScope.launch {
        locationTracker.getLocationUpdates().onCompletion {
            cancel()
        }.collect { result ->
            locationState = locationState.copy(
                loading = true
            )

            when (result) {

                is Resource.Error -> {
                    Log.e(TAG, "location: ${result.message}")

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

                    result.data?.let { data ->
                        Log.e(TAG, "current location: ${result.data}")
                        locationState = locationState.copy(
                            location = data,
                            loading = false,
                            error = null
                        )

                        liveLocation.emit(data)


                    }

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
        /*
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

         */
        val result = locationTracker.getCurrentAddress()

        place = result
    }

    fun selectPost(post: Post) {
        selectedPost = post
    }

    fun likePost(id: String) = viewModelScope.launch {

        repo.likePost(id,userData)
    }

    fun selectLocation(location: LocationCustom?) {

        selectedLocation = location!!

    }


}