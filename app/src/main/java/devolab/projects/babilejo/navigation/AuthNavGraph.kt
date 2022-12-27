package devolab.projects.babilejo.navigation

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import devolab.projects.babilejo.navigation.Graph.AUTH_ROUTE
import devolab.projects.babilejo.ui.authentication.LoginScreen
import devolab.projects.babilejo.ui.authentication.SignUpScreen
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    userViewModel: UserAuthViewModel,
) {

    navigation(startDestination = Screens.Login.route, route = AUTH_ROUTE)
    {


        composable(route = Screens.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = userViewModel,

                )
        }
        composable(route = Screens.Signup.route) {
            SignUpScreen(
                navController = navController,
                viewModel = userViewModel,
            )
        }
    }
}