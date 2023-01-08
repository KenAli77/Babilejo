package devolab.projects.babilejo.domain.model

import devolab.projects.babilejo.domain.filters.PostCategory
import devolab.projects.babilejo.domain.filters.PostType

data class Post(
    val id: String? = null,
    val uid: String? = null,
    var location: LocationCustom? = null,
    var caption: String? = null,
    var comments: List<Comment>? = null,
    var photoUrl: String? = null,
    val timeStamp: Long? = null,
    val userName: String? = null,
    val userPhotoUrl: String? = null,
    val likes: List<User>? = null,
    val shares: Int? = null,
    val place: String? = null,
    val category:PostCategory?= null,
    val type: PostType? = null,
    val going: List<User>? = null,
)
