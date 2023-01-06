package devolab.projects.babilejo.ui.main.comment

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.navigation.Screens
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.comment.components.CommentActionBar
import devolab.projects.babilejo.ui.main.comment.components.CommentTopBar
import devolab.projects.babilejo.ui.main.comment.components.Comments
import devolab.projects.babilejo.ui.theme.LightYellow
import devolab.projects.babilejo.util.TOP_BAR_HEIGHT
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentScreen(
    mainViewModel: MainViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController(),
) {
    val viewModel = hiltViewModel<CommentsViewModel>()

    val keyboardController = LocalSoftwareKeyboardController.current

    val user = mainViewModel.userData

    val post = mainViewModel.selectedPost

    viewModel.addPostId(post.id!!)

    viewModel.getCommentsUpdate()

    val state by viewModel.state.collectAsState()

    BackHandler(true) {
        navHostController.navigateUp()
        navHostController.popBackStack(Screens.Comment.route, true)
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout() {
            val (topBar, commentsList, actionBar, progressBar) = createRefs()

            CommentTopBar(
                onNavBack = { navHostController.navigateUp() },
                modifier = Modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                    }
                    .height(TOP_BAR_HEIGHT))
            if (state.loading) {

                CircularProgressIndicator(modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(topBar.bottom, 20.dp)
                    bottom.linkTo(actionBar.top)
                }, color = LightYellow)

            } else {
                state.comments?.let { comments ->
                    Log.e("CommentScreen","comments: ${comments.size}")
                    Comments(modifier = Modifier.constrainAs(commentsList) {
                        top.linkTo(topBar.bottom)
                        bottom.linkTo(actionBar.top)
                        height = Dimension.fillToConstraints
                    }, comments = comments)

                } ?: CircularProgressIndicator(modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(topBar.bottom, 20.dp)
                    bottom.linkTo(actionBar.top)
                }, color = LightYellow)

            }
            CommentActionBar(
                modifier = Modifier.constrainAs(actionBar) {

                    bottom.linkTo(parent.bottom)

                },
                keyboardController = keyboardController,
                user = user ?: User(),
                onComment = {
                    user?.let { user ->

                        viewModel.postComment(comment = it, post.id, user = user)

                    }
                }
            )
        }


    }

}