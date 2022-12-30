package devolab.projects.babilejo.domain.model

data class Comment(
    val id:String?=null,
    val uid:String?=null,
    val postId:String?=null,
    val text:String?=null,
    val likes:Int?=null,
    val timeStamp:Long?=null,
)
