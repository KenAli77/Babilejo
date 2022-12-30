package devolab.projects.babilejo.domain.model

import com.google.type.LatLng

data class Conversation(
    val id: String?=null,
    val type: String?=null,
    val userIds: List<String>?=null,
    val name: String?=null,
    val location:LatLng?=null
)
