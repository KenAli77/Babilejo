package devolab.projects.babilejo.ui.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.profile.components.ProfilePageHeader
import devolab.projects.babilejo.ui.main.profile.components.ProfilePager
import devolab.projects.babilejo.ui.main.profile.components.ProfileTopBar

@Composable
fun ProfileScreen(navController: NavHostController, mainViewModel: MainViewModel) {

    mainViewModel.getUserOnlineStatus()
    val userData = mainViewModel.userData
    val status = mainViewModel.userStatus

    val viewModel = hiltViewModel<ProfileViewModel>()

    val state = viewModel.state

    Surface(modifier = Modifier.fillMaxSize()) {
        userData?.let { user->

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                ProfileTopBar()

                ProfilePageHeader(
                    user = user,
                    onlineStatus = status,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
                )

                state.posts?.let {

                    ProfilePager(modifier = Modifier.padding(top = 10.dp),posts = it, user = user )

                }


            }
        }
    }
}