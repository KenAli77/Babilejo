package devolab.projects.babilejo.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import devolab.projects.babilejo.navigation.Graph.MAIN_ROUTE
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.comment.CommentScreen
import devolab.projects.babilejo.ui.main.explore.ExploreScreen
import devolab.projects.babilejo.ui.main.home.HomeScreen
import devolab.projects.babilejo.ui.main.newPost.NewPostScreen
import devolab.projects.babilejo.ui.main.messages.MessagesScreen
import devolab.projects.babilejo.ui.main.profile.ProfileScreen
import devolab.projects.babilejo.ui.main.profile.UserProfileScreen
import devolab.projects.babilejo.ui.main.settings.SettingsScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    userAuthViewModel: UserAuthViewModel

) {

    navigation(
        startDestination = Screens.Home.route,
        route = MAIN_ROUTE
    )
    {
        composable(
            route = Screens.Home.route
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MAIN_ROUTE)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)

            HomeScreen(navController, authViewModel = userAuthViewModel,mainViewModel)
        }

        composable(Screens.Explore.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MAIN_ROUTE)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)
            ExploreScreen(navController,mainViewModel)
        }
        composable(Screens.Messages.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MAIN_ROUTE)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)
            MessagesScreen(navController,mainViewModel)
        }

        composable(Screens.Profile.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MAIN_ROUTE)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)
            ProfileScreen(navController,mainViewModel)
        }

        composable(Screens.Settings.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MAIN_ROUTE)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)
            SettingsScreen(navController,mainViewModel)
        }

        composable(Screens.NewPost.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MAIN_ROUTE)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)
            NewPostScreen(navHostController = navController,mainViewModel)
        }

        composable(Screens.Comment.route){ backStackEntry->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MAIN_ROUTE)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)

            CommentScreen(mainViewModel,navController)
        }

        composable(Screens.UserProfile.route){ backStackEntry->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MAIN_ROUTE)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)

            UserProfileScreen(mainViewModel = mainViewModel, navController = navController)
        }


    }
}