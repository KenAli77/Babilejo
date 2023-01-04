package devolab.projects.babilejo.domain.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.util.toLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "LocationUpdateService"

@AndroidEntryPoint
class LocationUpdateService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var locationTracker: DefaultLocationTracker

    @Inject
    lateinit var mainRepo: MainRepositoryImpl

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e(TAG, "onStartCommand")


        mainViewModel.liveLocation?.let {

            scope.launch {
                mainRepo.updateUserLocation(it.toLocation())

            }
        }


        return START_STICKY
    }


}