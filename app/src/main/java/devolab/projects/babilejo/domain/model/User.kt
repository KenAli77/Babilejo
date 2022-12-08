package devolab.projects.babilejo.domain.model

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp

data class User(
    val uid:String?=null,
    val userName:String?=null,
    val displayName:String?=null,
    val userEmail:String?=null,
    val photoUrl:String?=null,
    @ServerTimestamp
    val created: FieldValue? = null

)
