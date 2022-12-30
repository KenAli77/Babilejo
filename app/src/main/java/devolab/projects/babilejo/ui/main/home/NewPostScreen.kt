package devolab.projects.babilejo.ui.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.skydoves.landscapist.glide.GlideImage
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.main.UserProfileViewModel
import devolab.projects.babilejo.ui.main.explore.LocationViewModel
import devolab.projects.babilejo.ui.theme.Yellow
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@Composable
fun NewPostScreen(navHostController: NavHostController, locationViewModel: LocationViewModel) {

    val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
    var user by remember { mutableStateOf(User()) }
    var userName by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var uid by remember { mutableStateOf("") }
    var caption by remember {
        mutableStateOf("")
    }

    userProfileViewModel.userDataState.data?.let {
        user = it
    }

    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
        }

    val mediaPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    )

    fun pickPhoto() {
        if (!mediaPermissionState.allPermissionsGranted) {
            coroutineScope.launch {
                mediaPermissionState.launchMultiplePermissionRequest()
            }
        }
        launcher.launch("image/*")
    }


    imageUri?.let {
        bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            ImageDecoder.decodeBitmap(source)
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var focusRequested by remember { mutableStateOf(false) }


    user.userName?.let { userName = it }
    user.photoUrl?.let { photoUrl = it }
    user.uid?.let { uid = it }

    BackHandler(true) {
        navHostController.popBackStack()
    }

    LaunchedEffect(focusRequested) {
        focusRequester.requestFocus()
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (
            topBar, imageView, actionBar, header, textField, box
        ) = createRefs()

        TopActionBar(
            { navHostController.navigateUp() },
            modifier = Modifier.constrainAs(topBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        NewPostHeader(
            userName = userName,
            photoUrl = photoUrl,
            location = locationViewModel.locality,
            modifier = Modifier.constrainAs(header) {
                top.linkTo(topBar.bottom)
                start.linkTo(parent.start)
            }

        )

        TextFieldLarge(
            text = caption,
            onValueChange = { caption = it },
            keyboardController = keyboardController,
            modifier = Modifier
                .constrainAs(textField) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(imageView.top)
                }
                .focusRequester(focusRequester)

        )



        bitmap?.let {
            ImageContainer(image = it, modifier = Modifier.constrainAs(imageView) {
                top.linkTo(textField.bottom)
                bottom.linkTo(actionBar.top)
            })
        } ?: Box(
            Modifier
                .clickable(
                    indication = null,
                    onClick = { focusRequested = true },
                    interactionSource = remember { MutableInteractionSource() })
                .fillMaxHeight(0.6f)
                .constrainAs(box) {
                    top.linkTo(textField.bottom)
                    bottom.linkTo(actionBar.top)
                }
        ) {

        }

        NewPostActionBar(
            onAddPhoto = { pickPhoto() },
            modifier = Modifier.constrainAs(actionBar) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldLarge(
    text: String,
    onValueChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier
) {

    BasicTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 10.dp, start = 10.dp)
            .focusable(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        textStyle = MaterialTheme.typography.body1,
        cursorBrush = Brush.linearGradient(listOf(Color.Black, Yellow)),


        )
}

@Composable
fun NewPostHeader(
    userName: String,
    photoUrl: String,
    location: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .height(65.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        GlideImage(
            imageModel = photoUrl, contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.height(60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = userName, fontWeight = FontWeight.Bold)
            Text(text = location)
        }


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
            .height(60.dp)
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
fun ImageContainer(image: Bitmap, modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        GlideImage(
            imageModel = image,
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun TopActionBar(onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
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
                modifier = Modifier.size(30.dp)
            )
        }

    }
}