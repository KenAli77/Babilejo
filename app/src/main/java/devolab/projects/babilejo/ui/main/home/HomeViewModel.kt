package devolab.projects.babilejo.ui.main.home

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
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.home.state.HomeState
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repo: MainRepositoryImpl,val mainViewModel: MainViewModel) : ViewModel() {

    var state by mutableStateOf(HomeState())

    var location by mutableStateOf(mainViewModel.locationState)

    init {
        getPosts()
    }


    private fun getPosts() = viewModelScope.launch {
        val result = repo.getPosts()
        state = state.copy(
            loading = true
        )
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

                    state = state.copy(
                        loading = false,
                        error = null,
                        data = posts
                    )
                }
            }
        }
    }


    override fun onCleared() {
        // remove observer
        super.onCleared()
    }



}