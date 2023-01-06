package devolab.projects.babilejo.ui.main.comment.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.main.newPost.components.BasicTextFieldCustom
import devolab.projects.babilejo.ui.theme.Yellow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentActionBar(
    modifier: Modifier = Modifier,
    user: User,
    keyboardController: SoftwareKeyboardController?,
    onComment: (String) -> Unit = {}
) {

    val context = LocalContext.current

    var hintVisible by remember { mutableStateOf(true) }

    var text by remember { mutableStateOf("") }


    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    LaunchedEffect(key1 = text) {
        hintVisible = text.isEmpty()
    }

    Surface(modifier = modifier.fillMaxWidth(), color = Yellow) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 5.dp)
        ) {
            val (profileImage, textField, post) = createRefs()
            CoilImage(
                imageModel = { user.photoUrl },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .constrainAs(profileImage) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
                imageOptions = ImageOptions(
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                )
            )

            BasicTextFieldCustom(
                text = text,
                onValueChange = { text = it },
                keyboardController = keyboardController,
                hint = "type a comment..",
                modifier = Modifier
                    .constrainAs(textField) {
                        start.linkTo(profileImage.end, 10.dp)
                        end.linkTo(post.start, 5.dp)
                        bottom.linkTo(profileImage.bottom)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                       // height = Dimension.fillToConstraints
                    },
                focusRequester = focusRequester,
                focusManager = focusManager


            )

            IconButton(onClick = {
                if (!hintVisible) {
                    onComment(text)
                    text = ""
                    Toast.makeText(context, "posting..", Toast.LENGTH_SHORT).show()
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }, modifier = Modifier
                .constrainAs(post) {
                    end.linkTo(parent.end)
                    bottom.linkTo(profileImage.bottom)
                    top.linkTo(profileImage.top)

                }) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = null,
                    tint = if (!hintVisible) Color.DarkGray else Color.DarkGray.copy(0.5f),
                    modifier = Modifier.size(25.dp)
                )
            }
        }


    }


}