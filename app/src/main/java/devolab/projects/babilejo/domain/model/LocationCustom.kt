package devolab.projects.babilejo.domain.model

data class LocationCustom(
    val latitude: Double?= null,
    val longitude: Double? = null,
    val altitude: Double? = null,
    val accuracy: Float? = null,
    val bearing: Float? = null,
    val speed: Float? = null,
    val time: Long? = null
)
