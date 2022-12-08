package devolab.projects.babilejo.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import devolab.projects.babilejo.ui.authentication.LoginScreen
import devolab.projects.babilejo.ui.authentication.SignUpScreen
import devolab.projects.babilejo.ui.main.HomeScreen
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    userViewModel: UserAuthViewModel = viewModel(),
) {

    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val scaffoldState = rememberScaffoldState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        LOGIN_ROUTE -> bottomBarState.value = false
        MAIN_ROUTE -> bottomBarState.value = true
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            when (bottomBarState.value) {
                true -> BottomNavBar(navController = navController, bottomBarState)
                false -> {}
            }
        },
        backgroundColor = Yellow,

        ) {

        NavHost(
            navController = navController,
            startDestination = LOGIN_ROUTE,
            route = ROOT_ROUTE,
            modifier = Modifier.padding(it)
        )
        {

            loginNavGraph(
                navController = navController,
                bottomBarState,
                userViewModel,
                scaffoldState,

            )

            mainNavGraph(
                navController = navController,
                bottomBarState,
                userViewModel,
                scaffoldState
            )

        }
    }

}

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    userViewModel: UserAuthViewModel,
    scaffoldState: ScaffoldState,
) {

    navigation(startDestination = Screens.Login.route, route = LOGIN_ROUTE)
    {


        composable(route = Screens.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = userViewModel,

            )
            bottomBarState.value = false
        }
        composable(route = Screens.Signup.route) {
            SignUpScreen(
                navController = navController,
                viewModel = userViewModel,
                scaffoldState = scaffoldState,
            )
            bottomBarState.value = false
        }
    }
}

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    userViewModel: UserAuthViewModel,
    scaffoldState: ScaffoldState
) {

    navigation(startDestination = Screens.Home.route, route = MAIN_ROUTE)
    {

        composable(
            route = Screens.Home.route
        ) {
            HomeScreen( userViewModel,navController)
            bottomBarState.value = true
        }


    }
}