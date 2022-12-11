package devolab.projects.babilejo.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import devolab.projects.babilejo.navigation.Graph.AUTH_ROUTE
import devolab.projects.babilejo.navigation.Graph.MAIN_ROUTE
import devolab.projects.babilejo.navigation.Graph.ROOT_ROUTE
import devolab.projects.babilejo.ui.main.MainScreen
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    userViewModel: UserAuthViewModel = viewModel(),
) {
    val startDestination = if (userViewModel.isUserAuthenticated()) MAIN_ROUTE else AUTH_ROUTE

    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = ROOT_ROUTE, )
    {
        authNavGraph(
            navController = navController,
            userViewModel = userViewModel
        )

        composable(route = MAIN_ROUTE) {
            MainScreen()
        }

    }


}

