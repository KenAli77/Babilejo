package devolab.projects.babilejo.ui.main.explore.state

import devolab.projects.babilejo.domain.model.Location

data class ExploreState(
    val currentLocation: Location?=null,
    val lastKnownLocation: Location?=null,
    val error: String?=null,
    val loading: Boolean = false
)
