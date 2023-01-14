package devolab.projects.babilejo.ui.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.main.profile.state.ProfileScreenState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val repo: MainRepositoryImpl) : ViewModel() {


    var state by mutableStateOf(ProfileScreenState())


    init {
        getUserPosts()
    }

    fun getUserPosts(uid:String?=null) = viewModelScope.launch {


        val result = uid?.let { repo.getUserPosts(it) }?:repo.getUserPosts()

        when (result) {
            is Resource.Error -> {
                result.message?.let {
                    state = state.copy(
                        posts = null,
                        loading = false,
                        error = it
                    )
                }
            }
            is Resource.Loading -> {
                state = state.copy(
                    loading = true
                )
            }
            is Resource.Success -> {
                result.data?.let {
                    state = state.copy(
                        posts = it,
                        loading = false,
                        error = null
                    )
                }

            }
        }

    }

}