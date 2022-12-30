package devolab.projects.babilejo.domain.model

data class Post(
    val id:String?=null,
    val uid:String? = null,
    var location: devolab.projects.babilejo.domain.model.Location? = null,
    var caption:String? = null,
    var comments:List<Comment>?=null,
    var photoUrl:String? = null,
    val timeStamp:Long? = null,
)
