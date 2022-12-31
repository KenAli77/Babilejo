package devolab.projects.babilejo.ui.main.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.navigation.Screens
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.authentication.components.AuthProgressBar
import devolab.projects.babilejo.ui.authentication.components.LogoutDialog
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.explore.LocationViewModel
import devolab.projects.babilejo.ui.main.home.components.HomeTopBar
import devolab.projects.babilejo.ui.main.home.components.Post
import devolab.projects.babilejo.ui.theme.LightYellow
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.util.TOP_BAR_HEIGHT
import devolab.projects.babilejo.util.isScrolled

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController, authViewModel: UserAuthViewModel) {
    val context = LocalContext.current

    val mainViewModel = hiltViewModel<MainViewModel>()
    val locationViewModel = hiltViewModel<LocationViewModel>()

    val lazyListState = rememberLazyListState()

    val state = mainViewModel.feedState

    val padding by animateDpAsState(targetValue = if (lazyListState.isScrolled) 0.dp else TOP_BAR_HEIGHT)

    val openDialog = remember {
        mutableStateOf(false)
    }

    BackHandler(true) {

        openDialog.value = true

    }

    LogoutDialog(openDialog = openDialog, logout = {
        authViewModel.logOut()
        navController.navigateUp()
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Yellow.copy(0.5f)),
    ) {
        if (state.loading) {
            AuthProgressBar()
        }

        state.data?.let {
            LazyColumn(state = lazyListState, modifier = Modifier.padding(top = 65.dp)) {

                items(it) { item ->
                    val locality = item.location?.let { loc ->
                        locationViewModel.getLocalityFromLocation(loc)
                    }
                    Post(post = item, locality = locality?:"")
                }
            }
        }

        HomeTopBar(
            lazyListState = lazyListState,
            onPostClick = { navController.navigate(Screens.NewPost.route) })


    }
}