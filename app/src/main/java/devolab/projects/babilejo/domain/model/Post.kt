package devolab.projects.babilejo.domain.model

import android.net.Uri

data class Post(
    val id:String?=null,
    val uid:String? = null,
    var location: devolab.projects.babilejo.domain.model.Location? = null,
    var caption:String? = null,
    var comments:List<Comment>?=null,
    var photoUrl: String? = null,
    val timeStamp:Long? = null,
    val userName: String? = null,
    val userPhotoUrl: String? = null,
    val likes:Int? = null,
    val shares:Int? = null
)
