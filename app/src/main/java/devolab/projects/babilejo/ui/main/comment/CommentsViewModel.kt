package devolab.projects.babilejo.ui.main.comment

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.model.Comment
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.main.comment.state.CommentsState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(val repo: MainRepositoryImpl) : ViewModel() {

    private val tag = "CommentsViewModel"

    var postId by mutableStateOf("")
        private set

    var state = MutableStateFlow(CommentsState())

    fun postComment(comment: String, postId: String, user: User) = viewModelScope.launch {

        val commentItem = generateCommentItem(user, comment)

        repo.postComment(comment = commentItem, postId = postId)

    }

    private fun generateCommentItem(user: User, comment: String): Comment {
        return Comment(
            id = UUID.randomUUID().toString(),
            text = comment,
            userName = user.userName,
            userPhotoUrl = user.photoUrl,
            uid = user.uid,
            timeStamp = System.currentTimeMillis()
        )
    }

    fun getCommentsUpdate() = viewModelScope.launch {
        state.emit(
            state.value.copy(
                loading = true,
                error = null,
                comments = null
            )
        )
        repo.getPostUpdates(postId).onCompletion { cancel() }.collect { result ->

            when (result) {
                is Resource.Error -> {
                    Log.e(tag, "could not fetch comments: ${result.message}")
                    state.emit(
                        state.value.copy(
                            comments = null,
                            error = result.message,
                            loading = false
                        )
                    )
                }
                is Resource.Loading -> {

                    state.emit(
                        state.value.copy(
                            loading = true
                        )
                    )
                }
                is Resource.Success -> {
                    result.data?.let { post ->

                        post.comments?.let { list ->

                            Log.e(tag, "comments ${list.size}")

                            state.emit(
                                state.value.copy(
                                    comments = list,
                                    error = null,
                                    loading = false

                                )
                            )


                        }

                    }
                }
            }

        }
    }

    fun addPostId(id: String) {
        postId = id
    }


}