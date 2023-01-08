package devolab.projects.babilejo.ui.main.home

import android.Manifest
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import devolab.projects.babilejo.R
import devolab.projects.babilejo.domain.location.LocationUpdateService
import devolab.projects.babilejo.navigation.Screens
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.authentication.components.LogoutDialog
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.home.components.FeedBanner
import devolab.projects.babilejo.ui.main.home.components.HomeTopBar
import devolab.projects.babilejo.ui.main.home.components.Post
import devolab.projects.babilejo.ui.theme.Yellow

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: UserAuthViewModel,
    mainViewModel: MainViewModel
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val lazyListState = rememberLazyListState()

    val viewModel = hiltViewModel<HomeViewModel>()

    val state = viewModel.state

    val context = LocalContext.current

    val location by mainViewModel.liveLocation.collectAsState()

    val currentUser = mainViewModel.userData

    val filters = mainViewModel.filters


    location?.let { loc ->
        currentUser?.let { user ->
            viewModel.getPosts(loc, user)
        }
    }

    val openDialog = remember {
        mutableStateOf(false)
    }

    var openFilters by remember { mutableStateOf(false) }

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
        context.stopService(Intent(context, LocationUpdateService::class.java))

    })
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Yellow.copy(0.5f)),
    ) {

        val (topBar, lazyList, filtersView, loadingBar) = createRefs()

        HomeTopBar(

            lazyListState = lazyListState,
            onPostClick = {
                navController.navigate(Screens.NewPost.route)
            },
            openFilters = {
                openFilters = !openFilters
            },
            modifier = Modifier.constrainAs(topBar) {
                top.linkTo(parent.top)
            }
        )
        if (state.loading) {

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_radar_animation))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.constrainAs(loadingBar) {
                    top.linkTo(topBar.bottom)
                    bottom.linkTo(parent.bottom)

                }
            )

        }

        if (lazyListState.isScrollInProgress) {
            openFilters = false
        }


        state.data?.let {

            if (openFilters) {
                FeedBanner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(filtersView) {
                            top.linkTo(topBar.bottom)

                        }
                        .animateContentSize(),
                    filters = {
                        mainViewModel.applyFilters(it)
                        Log.e("HomeScreen", "filters: $filters")

                    },
                    filterList = filters,
                )
            }
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.constrainAs(lazyList) {
                    if (openFilters) {
                        top.linkTo(filtersView.bottom)
                    } else {
                        top.linkTo(topBar.bottom)
                    }
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }) {


                items(it) { item ->
                    var liked by remember {
                        mutableStateOf(false)
                    }
                    item.likes?.let { likes ->
                        liked = likes.contains(mainViewModel.userData)
                    }
                    Log.e("HomeScreen", "Post ${item.userName} liked: $liked")


                    Post(
                        post = item,
                        locality = item.place ?: "",
                        onShare = {},
                        onLookUp = {
                            mainViewModel.selectLocation(item.location)
                            navController.navigate(Screens.Explore.route)
                        },
                        onLike = {
                            item.id?.let {

                                mainViewModel.likePost(item.id)
                            }
                        },
                        onComment = {
                            mainViewModel.selectPost(item)
                            navController.navigate(Screens.Comment.route)
                        },
                        liked = liked
                    )
                }
            }

        }

    }

    if (locationPermissionState.allPermissionsGranted) {
        context.startService(Intent(context, LocationUpdateService::class.java))
    }
}