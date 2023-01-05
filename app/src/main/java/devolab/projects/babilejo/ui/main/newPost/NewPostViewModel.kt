package devolab.projects.babilejo.ui.main.newPost

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.model.Location
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.newPost.state.NewPostState
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val repo: MainRepositoryImpl
) : ViewModel() {


    var state by mutableStateOf(NewPostState())

    init {
    }

    fun addPost(
        caption: String?,
        photoUrl: Uri?,
        imageBitmap: Bitmap? = null,
        user: User,
        currentLocation: android.location.Location,
        place: String
    ) =
        viewModelScope.launch {

            val postId = UUID.randomUUID().toString()

            user.uid?.let {
                Log.e("newPost", "user: ${user.userName}")

                Log.e("newPost", "location: $currentLocation")

                val post = Post(
                    id = postId,
                    uid = user.uid,
                    location = currentLocation.toLocation(),
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