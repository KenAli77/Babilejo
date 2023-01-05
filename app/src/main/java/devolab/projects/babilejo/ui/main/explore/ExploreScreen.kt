package devolab.projects.babilejo.ui.main.explore

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.authentication.components.AuthProgressBar
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.explore.components.ExploreMapView

@Composable
fun ExploreScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val viewModel = hiltViewModel<ExploreViewModel>()

    val currentPosition by mainViewModel.liveLocation.collectAsState()


    with(viewModel) {

        getUserUpdates(mainViewModel)

        val context = LocalContext.current

        LaunchedEffect(key1 = currentPosition) {



        }

//        if (currentPosition.loading) {
//            AuthProgressBar()
//        }

        ExploreMapView(location = currentPosition, users = usersData)


    }


}



