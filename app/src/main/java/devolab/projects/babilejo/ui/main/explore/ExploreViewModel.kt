package devolab.projects.babilejo.ui.main.explore

import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.domain.repository.MainRepository
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.explore.state.ExploreState
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val app: Application,
) : ViewModel() {

    private val TAG = "ExploreViewModel"

    var usersData = mutableStateListOf<User>()

    var state by mutableStateOf(ExploreState())
        private set

    fun getUserUpdates(mainViewModel:MainViewModel) {

        viewModelScope.launch {

            mainViewModel.getUserUpdates().onCompletion {
                cancel()
            }.collect {

                when (it) {
                    is Resource.Error -> {
                        it.message?.let { error ->
                            Log.e(TAG, error)
                        }

                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        it.data?.let {
                            Log.e(TAG, it.toString())
                            usersData = it.toMutableStateList()
                        }

                    }
                }

            }
        }

    }

}