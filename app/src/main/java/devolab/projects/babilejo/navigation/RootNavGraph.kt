package devolab.projects.babilejo.navigation

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import devolab.projects.babilejo.navigation.Graph.AUTH_ROUTE
import devolab.projects.babilejo.navigation.Graph.MAIN_ROUTE
import devolab.projects.babilejo.navigation.Graph.ROOT_ROUTE
import devolab.projects.babilejo.ui.main.MainScreen
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.main.explore.ExploreViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RootNavGraph(
    navController: NavHostController,
    userViewModel: UserAuthViewModel = viewModel(),
    exploreViewModel: ExploreViewModel = viewModel()
) {
    val startDestination =  if (userViewModel.isUserAuthenticated()) MAIN_ROUTE else AUTH_ROUTE


    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        )

    )

    if (!locationPermissionState.allPermissionsGranted) {
        coroutineScope.launch {
            locationPermissionState.launchMultiplePermissionRequest()

        }
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarState = currentRoute != Screens.Login.route && currentRoute != Screens.Signup.route

    Scaffold(bottomBar = {
        if (bottomBarState) {
            BottomNavBar(
                navController = navController
            )
        }
    }) {

        val paddingValues =
            if (bottomBarState) {
                it
            } else {
                PaddingValues(0.dp)
            }
        NavHost(
            navController = navController,
            startDestination = AUTH_ROUTE,
            route = ROOT_ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {
            authNavGraph(
                navController = navController, userViewModel = userViewModel
            )

            mainNavGraph(
                exploreViewModel = exploreViewModel,
                userAuthViewModel = userViewModel,
                navController = navController
            )


        }

    }


}

