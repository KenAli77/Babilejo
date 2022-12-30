package devolab.projects.babilejo.ui.main.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.navigation.Screens
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.authentication.components.LogoutDialog
import devolab.projects.babilejo.ui.main.UserProfileViewModel
import devolab.projects.babilejo.ui.main.home.components.HomeTopBar
import devolab.projects.babilejo.ui.main.home.components.Post
import devolab.projects.babilejo.ui.theme.DarkYellow
import devolab.projects.babilejo.ui.theme.LightYellow
import devolab.projects.babilejo.util.TOP_BAR_HEIGHT
import devolab.projects.babilejo.util.isScrolled

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController, authViewModel: UserAuthViewModel) {
    val context = LocalContext.current

    val profileViewModel = hiltViewModel<UserProfileViewModel>()

    val userName = remember { mutableStateOf("") }

    val lazyListState = rememberLazyListState()
    LaunchedEffect(key1 = profileViewModel.userDataState.data) {
        profileViewModel.userDataState.data?.let {
            userName.value = it.userEmail.toString()
        }
    }

    val padding by animateDpAsState(targetValue = if (lazyListState.isScrolled) 0.dp else TOP_BAR_HEIGHT)

    val openDialog = remember {
        mutableStateOf(false)
    }

    BackHandler(true) {

        Toast.makeText(context, "log out?", Toast.LENGTH_SHORT).show()
        openDialog.value = true


    }

    LogoutDialog(openDialog = openDialog, logout = {
        authViewModel.logOut()
        navController.navigateUp()
    })
    Surface(modifier = Modifier.fillMaxSize(), color = LightYellow) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            HomeTopBar(
                lazyListState = lazyListState,
                onPostClick = { navController.navigate(Screens.NewPost.route) })

            LazyColumn(state = lazyListState) {

                items(10) {
                    Post()
                }
            }
        }
    }
}