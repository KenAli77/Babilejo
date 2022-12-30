package devolab.projects.babilejo.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import devolab.projects.babilejo.navigation.Graph.MAIN_ROUTE
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.main.explore.ExploreScreen
import devolab.projects.babilejo.ui.main.explore.LocationViewModel
import devolab.projects.babilejo.ui.main.home.HomeScreen
import devolab.projects.babilejo.ui.main.newPost.NewPostScreen
import devolab.projects.babilejo.ui.main.messages.MessagesScreen
import devolab.projects.babilejo.ui.main.profile.ProfileScreen
import devolab.projects.babilejo.ui.main.settings.SettingsScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    locationViewModel: LocationViewModel,
    userAuthViewModel: UserAuthViewModel

) {

    navigation(
        startDestination = Screens.Home.route,
        route = MAIN_ROUTE
    )
    {

        composable(
            route = Screens.Home.route
        ) {
            HomeScreen(navController, authViewModel = userAuthViewModel)
        }

        composable(Screens.Explore.route) {
            ExploreScreen(navController, locationViewModel)
        }
        composable(Screens.Messages.route) {
            MessagesScreen(navController)
        }

        composable(Screens.Profile.route) {
            ProfileScreen(navController)
        }

        composable(Screens.Settings.route) {
            SettingsScreen(navController)
        }

        composable(Screens.NewPost.route) {
            NewPostScreen(navHostController = navController,locationViewModel = locationViewModel)
        }


    }
}