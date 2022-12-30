package devolab.projects.babilejo.ui.main.newPost

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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.main.MainViewModel
import devolab.projects.babilejo.ui.main.explore.LocationViewModel
import devolab.projects.babilejo.ui.main.newPost.components.*
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@Composable
fun NewPostScreen(navHostController: NavHostController, locationViewModel: LocationViewModel) {

    val mainViewModel = hiltViewModel<MainViewModel>()
    var user by remember { mutableStateOf(User()) }
    var userName by remember { mutableStateOf("") }
    var userPhotoUrl by remember { mutableStateOf("") }
    var uid by remember { mutableStateOf("") }
    var caption by remember {
        mutableStateOf("")
    }
    var postPhotoUrl by remember{ mutableStateOf("") }

    mainViewModel.userDataState.data?.let {
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
                imageUri?.let {
                    bitmap = null
                    imageUri = null
                }

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

    user.userName?.let { userName = it }
    user.photoUrl?.let { userPhotoUrl = it }
    user.uid?.let { uid = it }

    BackHandler(true) {
        navHostController.navigateUp()
        navHostController.popBackStack()
    }

    val scrollState = rememberScrollState()

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (
            topBar, imageView, actionBar, header, textField, postContainer
        ) = createRefs()

        TopActionBar(

            modifier = Modifier.constrainAs(topBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onClick = { navHostController.navigateUp() }
        )

        NewPostHeader(
            userName = userName,
            photoUrl = userPhotoUrl,
            location = locationViewModel.locality,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                }
                .padding(10.dp)

        )

        Column(modifier = Modifier
            .constrainAs(postContainer) {
                top.linkTo(header.bottom)
                //  bottom.linkTo(actionBar.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(bottom = 200.dp)
            .verticalScroll(scrollState, enabled = true)
        ) {
            ConstraintLayout() {

                TextFieldLarge(
                    text = caption,
                    onValueChange = { caption = it },
                    keyboardController = keyboardController,
                    modifier = Modifier
                        .constrainAs(textField) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }


                )

                bitmap?.let {
                    ImageContainer(image = it, modifier = Modifier
                        .constrainAs(imageView) {
                            top.linkTo(textField.bottom, margin = 10.dp)
                            //bottom.linkTo(actionBar.top)
                        }
                        .padding(horizontal = 10.dp),
                        onRemove = { bitmap = null },
                        onEdit = { pickPhoto() }
                    )
                }
            }
        }

        NewPostActionBar(
            onAddPhoto = { pickPhoto() },
            modifier = Modifier.constrainAs(actionBar) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onPost = {mainViewModel.addPost(
                caption = caption,
                photoUrl = imageUri,
                location = locationViewModel.lastKnownPosition,
            )}
        )

    }

}
