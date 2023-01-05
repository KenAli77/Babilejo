package devolab.projects.babilejo.ui.main.comment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.navigation.Screens
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.comment.components.CommentActionBar
import devolab.projects.babilejo.ui.main.comment.components.CommentTopBar
import devolab.projects.babilejo.ui.main.comment.components.Comments

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentScreen(mainViewModel: MainViewModel, navHostController: NavHostController) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val user = mainViewModel.userData

    BackHandler(true) {
        navHostController.navigateUp()
        navHostController.popBackStack(Screens.Comment.route, true)
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout() {
            val (topBar, comments, actionBar) = createRefs()

            CommentTopBar(
                onNavBack = { navHostController.navigateUp() },
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                })
            Comments(modifier = Modifier.constrainAs(comments) {
                top.linkTo(topBar.bottom)
                bottom.linkTo(actionBar.top)
            })
            CommentActionBar(
                modifier = Modifier.constrainAs(actionBar) {
                    bottom.linkTo(parent.bottom)

                },
                keyboardController = keyboardController,
                user = user ?: User()
            )
        }


    }

}