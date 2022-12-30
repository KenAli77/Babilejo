package devolab.projects.babilejo.domain.model

import com.google.type.LatLng

data class Message(
    val id: String?=null,
    val senderId: String?=null,
    val conversationId: String?=null,
    val text: String?=null,
    val timestamp: Long?=null,
)
