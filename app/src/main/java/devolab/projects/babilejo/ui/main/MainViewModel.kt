package devolab.projects.babilejo.ui.main

import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.ui.main.home.state.FeedState
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.util.UserDataResponse
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MainRepositoryImpl,


    ) : ViewModel() {

    var userDataState by mutableStateOf<UserDataResponse>(Resource.Success(null))

    var feedState by mutableStateOf(FeedState())

    //  var usersState by mutableStateOf<>()


    init {
        getUserData()
        getPosts()
    }


    private fun getUserData() = viewModelScope.launch {
        userDataState = Resource.Loading()
        userDataState = repo.getUserData()
    }

    fun addPost(
        caption: String,
        photoUrl: Uri?,
        location: Location,
        imageUri: Uri? = null,
        userName: String,
        userPhotoUrl: String
    ) =
        viewModelScope.launch {

            val postId = UUID.randomUUID().toString()
            userDataState.data?.let {
                val post = Post(
                    id = postId,
                    uid = userDataState.data?.uid,
                    location = location.toLocation(),
                    caption = caption,
                    photoUrl = photoUrl?.toString(),
                    timeStamp = System.currentTimeMillis(),
                    comments = null,
                    userPhotoUrl = userPhotoUrl,
                    userName = userName
                )
                repo.addPost(post, imageUri)

            }

        }

    private fun getPosts() = viewModelScope.launch {
        val result = repo.getPosts()
        feedState = feedState.copy(
            loading = true
        )
        result.observeForever {
            when (it) {
                is Resource.Error -> {

                    feedState = feedState.copy(
                        loading = false,
                        data = null,
                        error = it.message
                    )

                }
                is Resource.Loading -> {
                    feedState = feedState.copy(
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
                        Log.e("postsSize", posts.size.toString())

                    }

                    feedState = feedState.copy(
                        loading = false,
                        error = null,
                        data = posts
                    )
                }
            }
        }
    }

    fun getUserFromId(id: String?): User {
        var user = User()
        viewModelScope.launch {
            when (val result = repo.getUserData(id)) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    TODO()
                }
                is Resource.Success -> {
                    result.data?.let {
                        user = it
                    }
                }
            }

        }
        return user
    }
}