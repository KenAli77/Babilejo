package devolab.projects.babilejo.ui.main.newPost

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.newPost.state.NewPostState
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val mainViewModel: MainViewModel,
    private val repo: MainRepositoryImpl
) : ViewModel() {

    val currentLocation by mutableStateOf(mainViewModel.locationState.location)
    val user by mutableStateOf(mainViewModel.userDataState.data)
    var place by mutableStateOf(mainViewModel.place)
        private set

    var state by mutableStateOf(NewPostState())

    init {
        mainViewModel.getPLace()
    }

    fun addPost(
        caption: String?,
        photoUrl: Uri?,
        imageBitmap: Bitmap? = null,
    ) =
        viewModelScope.launch {

            val postId = UUID.randomUUID().toString()
            user?.let { user ->
                Log.e("newPost", "user: ${user.userName}")
                currentLocation?.let { location ->
                    Log.e("newPost", "location: $location")

                    val post = Post(
                        id = postId,
                        uid = user.uid,
                        location = location.toLocation(),
                        caption = caption,
                        photoUrl = photoUrl?.toString(),
                        timeStamp = System.currentTimeMillis(),
                        comments = null,
                        userPhotoUrl = user.photoUrl,
                        userName = user.userName,
                        place = place
                    )
                    val result = repo.addPost(post, imageBitmap)

                    when (result) {
                        is Resource.Error -> {
                            state = state.copy(
                                success = false,
                                loading = false,
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(
                                loading = true,
                                success = false,
                                error = null
                            )
                        }
                        is Resource.Success -> {
                            state = state.copy(
                                success = true,
                                loading = false,
                                error = null
                            )
                        }
                    }
                }


            }

        }

}