package devolab.projects.babilejo.ui.main

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import devolab.projects.babilejo.navigation.BottomNavBar
import devolab.projects.babilejo.navigation.MainNavGraph
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.main.explore.ExploreViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()){

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) {
        val exploreViewModel = hiltViewModel<ExploreViewModel>()

        MainNavGraph(navController = navController, exploreViewModel)
    }

}