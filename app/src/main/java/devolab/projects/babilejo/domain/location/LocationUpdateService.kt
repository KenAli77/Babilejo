package devolab.projects.babilejo.domain.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class LocationUpdateService : Service() {
    private val TAG = "LocationUpdateService"

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var locationTracker: DefaultLocationTracker

    @Inject
    lateinit var mainRepo: MainRepositoryImpl


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")
        super.onCreate()
    }

    @OptIn(FlowPreview::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e(TAG, "onStartCommand")


        scope.launch(Dispatchers.IO) {
            val lastKnownLocation = locationTracker.getCurrentLocation()

            locationTracker.getLocationUpdates().debounce(300000).distinctUntilChanged()
                .onCompletion { cancel() }.collect {

                    it.data?.let {
                        lastKnownLocation?.let { currentLoc ->
                            if (currentLoc.distanceTo(it) >= 100f) {
                                scope.launch {

                                    mainRepo.updateUserLocation(it.toLocation())

                                }
                            }

                        }

                    }

                }

        }

        return START_STICKY
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}