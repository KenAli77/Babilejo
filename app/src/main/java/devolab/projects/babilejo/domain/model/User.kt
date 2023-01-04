package devolab.projects.babilejo.domain.model


data class User(
    val uid:String?=null,
    val userName:String?=null,
    val displayName:String?=null,
    val userEmail:String?=null,
    val photoUrl:String?=null,
    val location: Location?=null,
    val online:Boolean? = null,
    val lastOnline: Long? = null,
)
