package devolab.projects.babilejo.ui.main.newPost.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.util.TOP_BAR_HEIGHT


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BasicTextFieldCustom(
    text: String,
    onValueChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier,
    hint: String = "Type in something..",
    focusRequester: FocusRequester = FocusRequester(),
    focusManager: FocusManager
) {

    var isHintVisible by remember { mutableStateOf(true) }

    var interactionSource = remember { MutableInteractionSource() }


    LaunchedEffect(key1 = text) {
        isHintVisible = text.isEmpty()
    }

    Box(
        modifier = modifier
    ) {

        BasicTextField(
            value = text,
            onValueChange = { onValueChange(it) },
            modifier = modifier
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            textStyle = MaterialTheme.typography.body1,
            cursorBrush = Brush.linearGradient(listOf(Color.Black, Yellow)),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            }


        )
        if (isHintVisible) {
            Text(
                text = hint,
                color = Color.DarkGray.copy(0.7f),
                modifier = modifier.align(Alignment.CenterStart)
            )
        }


    }

}

@Composable
fun NewPostHeader(
    userName: String,
    photoUrl: String,
    location: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CoilImage(
            imageModel = { photoUrl },
            imageOptions = ImageOptions(
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            ),
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp),
        )
        Column(
            modifier = Modifier.height(60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = userName, fontWeight = FontWeight.Bold)
            location?.let {
                Text(text = it)

            } ?: LoadingBanner()
        }


    }
}

@Composable
fun LoadingBanner() {
    Row (verticalAlignment = Alignment.CenterVertically){
        val color = Color.DarkGray.copy(0.6f)
        Text(text = "Fetching location..", color = color)
        CircularProgressIndicator(color = color, modifier = Modifier.size(10.dp), strokeWidth = 2.dp)
    }
}

@Composable
fun NewPostActionBar(
    onAddPhoto: () -> Unit = {},
    onTakePhoto: () -> Unit = {},
    onTakeVideo: () -> Unit = {},
    onPost: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(TOP_BAR_HEIGHT)
            .background(Yellow)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
        ) {

            IconButton(onClick = { onTakePhoto() }) {
                Icon(
                    imageVector = Icons.Rounded.AddAPhoto,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }

            IconButton(onClick = { onTakeVideo() }) {
                Icon(
                    imageVector = Icons.Rounded.Videocam,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }

            IconButton(onClick = { onAddPhoto() }) {
                Icon(
                    imageVector = Icons.Rounded.PhotoLibrary,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }

        }

        IconButton(onClick = { onPost() }) {
            Icon(
                imageVector = Icons.Rounded.Check, contentDescription = null,
                tint = Color.DarkGray

            )

        }
    }
}

@Composable
fun ImageContainer(
    image: Bitmap,
    modifier: Modifier = Modifier,
    onRemove: () -> Unit,
    onEdit: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 5.dp)
            .clip(RoundedCornerShape(5.dp))
    ) {

        CoilImage(
            imageModel = { image },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = Modifier.fillMaxSize()
        )

        MediaActionBar(
            onEdit = { onEdit() },
            onRemove = { onRemove() },
            modifier = modifier.align(Alignment.TopEnd)
        )

    }

}

@Composable
fun TopActionBar(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp)
            .background(Yellow),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        IconButton(onClick = { onClick() }) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
        }

    }
}


@Composable
fun MediaActionBar(onEdit: () -> Unit, onRemove: () -> Unit, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.End)
    ) {

        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .height(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    role = Role.Button,
                    enabled = true,
                    onClick = { onEdit() },
                    indication = rememberRipple(bounded = false)
                ),
            color = Color.Black.copy(0.7f)

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 10.dp, start = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Edit",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold

                )
            }
        }

        Surface(
            modifier = Modifier
                .clip(CircleShape)
                .size(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    role = Role.Button,
                    enabled = true,
                    onClick = { onRemove() },
                    indication = rememberRipple(bounded = false, radius = 16.dp)
                ),
            color = Color.Black.copy(0.7f)

        ) {
            Icon(imageVector = Icons.Rounded.Close, contentDescription = null, tint = Color.White)
        }


    }

}