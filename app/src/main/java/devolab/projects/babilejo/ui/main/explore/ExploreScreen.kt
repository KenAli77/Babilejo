package devolab.projects.babilejo.ui.main.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.main.home.components.MainMapView

@Composable
fun ExploreScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<ExploreViewModel>()

    val currentPosition = viewModel.currentPosition
    val lastPosition = viewModel.lastKnownPosition

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            MainMapView(currentPosition,lastPosition)

        }
    }

}