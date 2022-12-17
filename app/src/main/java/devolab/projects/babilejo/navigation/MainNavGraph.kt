package devolab.projects.babilejo.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import devolab.projects.babilejo.navigation.Graph.MAIN_ROUTE
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.main.MainScreen
import devolab.projects.babilejo.ui.main.explore.ExploreScreen
import devolab.projects.babilejo.ui.main.explore.ExploreViewModel
import devolab.projects.babilejo.ui.main.home.HomeScreen
import devolab.projects.babilejo.ui.main.messages.MessagesScreen
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    exploreViewModel: ExploreViewModel,

) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    )

    if (locationPermissionState.allPermissionsGranted) {

    } else {

        coroutineScope.launch {
            locationPermissionState.launchMultiplePermissionRequest()

        }

    }

    NavHost(
        navController = navController,
        startDestination = BottomBarScreens.Home.route,
        route = MAIN_ROUTE
    )
    {

        composable(
            route = BottomBarScreens.Home.route
        ) {
            HomeScreen(navController)
        }

        composable(BottomBarScreens.Explore.route) {
            ExploreScreen(navController,exploreViewModel)
        }
        composable(BottomBarScreens.Messages.route) {
            MessagesScreen(navController)
        }


    }
}