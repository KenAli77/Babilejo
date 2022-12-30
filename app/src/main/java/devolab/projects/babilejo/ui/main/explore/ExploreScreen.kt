package devolab.projects.babilejo.ui.main.explore

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.main.explore.components.ExploreScreenContent

@Composable
fun ExploreScreen(navController: NavHostController, viewModel: LocationViewModel = hiltViewModel()) =
    with(viewModel) {

        ExploreScreenContent(viewModel = viewModel)

    }