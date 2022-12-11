package devolab.projects.babilejo.ui.main

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import devolab.projects.babilejo.navigation.BottomNavBar
import devolab.projects.babilejo.navigation.MainNavGraph
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()){

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) {
        MainNavGraph(navController = navController)
    }

}