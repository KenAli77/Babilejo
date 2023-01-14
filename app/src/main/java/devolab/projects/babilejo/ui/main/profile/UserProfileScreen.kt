package devolab.projects.babilejo.ui.main.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.navigation.Screens
import devolab.projects.babilejo.ui.authentication.components.Logo
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.profile.components.ProfilePageHeader
import devolab.projects.babilejo.ui.main.profile.components.ProfilePager
import devolab.projects.babilejo.ui.main.profile.components.ProfileTopBar
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.util.TOP_BAR_HEIGHT
import devolab.projects.babilejo.util.TOP_BAR_ICON_SIZE

@Composable
fun UserProfileScreen(navController: NavHostController, mainViewModel: MainViewModel) {

    mainViewModel.getUserOnlineStatus()

    val userData = mainViewModel.userData
    val status = mainViewModel.userStatus

    val viewModel = hiltViewModel<ProfileViewModel>()

    viewModel.getUserPosts(userData?.uid)

    BackHandler(true) {
        navController.navigateUp()
        navController.popBackStack(Screens.UserProfile.route,true)
    }

    val state = viewModel.state

    Surface(modifier = Modifier.fillMaxSize()) {
        userData?.let { user->

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                UserProfileTopBar(onNavBack = {navController.navigateUp()})

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


@Composable
fun UserProfileTopBar(modifier: Modifier = Modifier, onNavBack: () -> Unit = {},onMessage:()->Unit = {}) {

    TopAppBar(modifier = modifier.height(height = TOP_BAR_HEIGHT), backgroundColor = Yellow,) {



        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {

            IconButton(onClick = { onNavBack() }) {

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(TOP_BAR_ICON_SIZE),
                    tint = Color.White
                )
            }
            IconButton(onClick = { onMessage() }) {

                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = null,
                    modifier = Modifier
                        .size(TOP_BAR_ICON_SIZE),
                    tint = Color.White
                )
            }


        }

    }

}