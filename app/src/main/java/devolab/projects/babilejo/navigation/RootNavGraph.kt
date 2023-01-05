package devolab.projects.babilejo.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import devolab.projects.babilejo.navigation.Graph.AUTH_ROUTE
import devolab.projects.babilejo.navigation.Graph.MAIN_ROUTE
import devolab.projects.babilejo.navigation.Graph.ROOT_ROUTE
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.explore.ExploreViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RootNavGraph(
    navController: NavHostController,
    userViewModel: UserAuthViewModel = viewModel(),

    ) {


    var bottomBarState by remember { mutableStateOf(false) }

    var startDestination by remember { mutableStateOf<String?>(null) }

    userViewModel.isUserAuthenticated?.let { authenticated ->
        startDestination = if (authenticated) {
            MAIN_ROUTE
        } else {
            AUTH_ROUTE
        }
    }

    Log.e("rootNav", "composing")
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Log.e("bottomBar", bottomBarState.toString())
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(key1 = currentRoute) {
        currentRoute?.let {
            bottomBarState =
                it != Screens.Login.route &&
                        it != Screens.Signup.route &&
                        it != Screens.NewPost.route &&
                        it != Screens.Comment.route
        }
    }

    Log.e("bottomBar", bottomBarState.toString())
    Scaffold(bottomBar = {
        if (bottomBarState) {
            BottomNavBar(

                navController = navController
            )
            Log.e("bottomBar", bottomBarState.toString())
        }
    }) {

        val paddingValues = if (bottomBarState) {
            it
        } else {
            PaddingValues(0.dp)
        }
        startDestination?.let { dest ->
            NavHost(
                navController = navController,
                startDestination = dest,
                route = ROOT_ROUTE,
                modifier = Modifier.padding(paddingValues)
            ) {

                authNavGraph(
                    navController = navController, userViewModel = userViewModel,
                )

                mainNavGraph(
                    userAuthViewModel = userViewModel,
                    navController = navController
                )


            }
        } ?: SplashScreen()

    }

}

