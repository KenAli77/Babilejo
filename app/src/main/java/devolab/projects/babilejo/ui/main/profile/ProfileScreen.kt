package devolab.projects.babilejo.ui.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.profile.components.ProfilePageHeader

@Composable
fun ProfileScreen(navController: NavHostController, mainViewModel: MainViewModel) {

    val user = mainViewModel.userData
    val status = mainViewModel.userStatus


    Surface(modifier = Modifier.fillMaxSize()) {
        user?.let {


            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                ProfilePageHeader(user = user, onlineStatus = status)


            }
        }
    }
}