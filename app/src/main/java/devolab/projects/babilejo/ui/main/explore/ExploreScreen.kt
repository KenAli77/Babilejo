package devolab.projects.babilejo.ui.main.explore

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import devolab.projects.babilejo.domain.model.LocationCustom
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.explore.components.ExploreMapView

@Composable
fun ExploreScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val viewModel = hiltViewModel<ExploreViewModel>()

    val currentPosition by mainViewModel.liveLocation.collectAsState()

    val selectedLocation = mainViewModel.selectedLocation

    val lifecycleOwner = LocalLifecycleOwner.current

    with(viewModel) {

        getUserUpdates(mainViewModel)

        val context = LocalContext.current

        LaunchedEffect(key1 = currentPosition) {


        }

//        if (currentPosition.loading) {
//            AuthProgressBar()
//        }

        ExploreMapView(
            location = currentPosition,
            users = usersData,
            selectedLocation = selectedLocation
        )


    }


    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                mainViewModel.selectLocation(LocationCustom())
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

    }


}



