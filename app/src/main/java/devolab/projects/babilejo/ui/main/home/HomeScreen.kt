package devolab.projects.babilejo.ui.main.home

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import devolab.projects.babilejo.navigation.Screens
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.authentication.components.AuthProgressBar
import devolab.projects.babilejo.ui.authentication.components.LogoutDialog
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.explore.ExploreViewModel
import devolab.projects.babilejo.ui.main.home.components.HomeTopBar
import devolab.projects.babilejo.ui.main.home.components.Post
import devolab.projects.babilejo.ui.theme.Yellow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavHostController, authViewModel: UserAuthViewModel) {


    val lifecycleOwner = LocalLifecycleOwner.current

    val lazyListState = rememberLazyListState()

    val viewModel = hiltViewModel<HomeViewModel>()

    val state = viewModel.state


    val openDialog = remember {
        mutableStateOf(false)
    }

    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        )

    )


    BackHandler(true) {

        openDialog.value = true

    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && !locationPermissionState.allPermissionsGranted) {
                locationPermissionState.launchMultiplePermissionRequest()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

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
            AuthProgressBar(modifier = Modifier.align(Alignment.Center))
        }

        state.data?.let {
            LazyColumn(state = lazyListState, modifier = Modifier.padding(top = 65.dp)) {


                items(it) { item ->

                    Post(post = item, locality = item.place ?: "")
                }
            }
        }

        HomeTopBar(

            lazyListState = lazyListState,
            onPostClick = { navController.navigate(Screens.NewPost.route) }
        )

    }
}