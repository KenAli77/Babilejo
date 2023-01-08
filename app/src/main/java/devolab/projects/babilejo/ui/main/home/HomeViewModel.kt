package devolab.projects.babilejo.ui.main.home

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.main.home.state.HomeState
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MainRepositoryImpl,
) : ViewModel() {

    private val TAG = "HomeViewModel"

    var state by mutableStateOf(HomeState(loading = true))
        private set


    var range by mutableStateOf(800f)
        private set



    fun getPosts(currentLocation: Location, currentUser: User) = viewModelScope.launch {
        val result = repo.getPosts()

        result.observeForever {
            when (it) {
                is Resource.Error -> {

                    state = state.copy(
                        loading = false,
                        data = null,
                        error = it.message
                    )

                }
                is Resource.Loading -> {
                    state = state.copy(
                        loading = true,
                        error = null,
                        data = null,
                    )
                }
                is Resource.Success -> {
                    val posts = ArrayList<Post>()
                    it.data?.forEach { docs ->
                        posts.add(
                            docs.toObject<Post>()
                        )

                    }
                    var distance = 0f
                    val postFiltered = posts.filter { post ->

                        val postLocation = post.location?.toLocation()

                        Log.e(TAG, "post location: $postLocation")

                        Log.e(TAG, "current location: $currentLocation")

                        distance = postLocation!!.distanceTo(currentLocation)

                        Log.e(TAG, "distance: $distance")

                        distance <= range && post.uid != currentUser.uid

                    }
                    if (distance == 0f) {
                        state = state.copy(
                            loading = false,
                            error = "Could not load nearby posts",
                            data = null,
                        )


                    }

                    val postSorted = postFiltered.sortedByDescending { it.timeStamp }

                    state = state.copy(
                        loading = false,
                        error = null,
                        data = postSorted
                    )

                }
            }
        }
    }


}