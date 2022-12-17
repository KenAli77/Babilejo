package devolab.projects.babilejo.domain.location

import android.location.Location
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.util.LocationResponse
import kotlinx.coroutines.flow.Flow

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?

    suspend fun getLocationUpdates(): LocationResponse

}