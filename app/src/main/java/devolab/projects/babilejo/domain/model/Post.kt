package devolab.projects.babilejo.domain.model

import com.google.type.LatLng

data class Post(
    val id:String?=null,
    val uid:String? = null,
    val location:LatLng? = null,
    val caption:String? = null,
    val comments:List<Comment>?=null,
    val photoUrl:String? = null,
    val timeStamp:Long? = null,
)
