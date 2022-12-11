package devolab.projects.babilejo.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?

    suspend fun getLocationUpdates(): Flow<Location>

}