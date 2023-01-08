package devolab.projects.babilejo.ui.main.explore.state

import devolab.projects.babilejo.domain.model.LocationCustom

data class ExploreState(
    val currentLocation: LocationCustom?=null,
    val lastKnownLocation: LocationCustom?=null,
    val error: String?=null,
    val loading: Boolean = false
)
