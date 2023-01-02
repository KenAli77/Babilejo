package devolab.projects.babilejo.ui.main.explore

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.authentication.components.AuthProgressBar
import devolab.projects.babilejo.ui.main.explore.components.ExploreMapView

@Composable
fun ExploreScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<ExploreViewModel>()

    val currentPosition = viewModel.currentPosition
    val lastKnownPosition = viewModel.lastKnownPosition

    val context = LocalContext.current

    LaunchedEffect(key1 = currentPosition.error) {
        Toast.makeText(context, "error fetching location", Toast.LENGTH_SHORT).show()
    }

    if (currentPosition.loading) {
        AuthProgressBar()
    }

    currentPosition.location?.let {
        ExploreMapView(location = it, lastKnownPosition = lastKnownPosition)
    }


}
