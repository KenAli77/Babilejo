package devolab.projects.babilejo.ui.main.newPost

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.*
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.authentication.components.BasicAlertDialog
import devolab.projects.babilejo.ui.main.newPost.components.*
import devolab.projects.babilejo.util.getBitmapFromUri
import devolab.projects.babilejo.util.getImageUri
import devolab.projects.babilejo.util.isPermanentlyDenied
import devolab.projects.babilejo.util.openAppSystemSettings
import java.util.*

const val TAG = "NewPostScreen"

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@Composable
fun NewPostScreen(navHostController: NavHostController) {

    val viewModel = hiltViewModel<NewPostViewModel>()

    var user by remember { mutableStateOf(User()) }
    var userName by remember { mutableStateOf("") }
    var userPhotoUrl by remember { mutableStateOf("") }
    var uid by remember { mutableStateOf("") }
    var caption by remember {
        mutableStateOf<String?>(null)
    }
    var hintVisibile by remember { mutableStateOf(true) }
    viewModel.user?.let {
        user = it
    }

    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasImage by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val openDialog = remember {
        mutableStateOf(false)
    }

    var dialogText by remember { mutableStateOf("") }

    BasicAlertDialog(
        openDialog = openDialog,
        onAction = { context.openAppSystemSettings() },
        text = dialogText,
        title = "Permissions are needed",
        actionText = "go to settings"
    )

    val pickPhotoLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            hasImage = uri != null

        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { success ->
            hasImage = false
            hasImage = success
            Log.e(TAG, "image taken: $success")

        }

    val mediaPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    )

    val cameraPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA

        )
    )

    cameraPermissionState.permissions.forEach { perm ->
        when (perm.permission) {
            Manifest.permission.CAMERA -> {
                when {
                    perm.status.isGranted -> {
                        Log.e(TAG, "${perm.permission} permission granted")

                    }
                    perm.status.shouldShowRationale -> {
                        dialogText = "Camera permissions is needed" + "to access the camera"
                    }
                    perm.isPermanentlyDenied() -> {
                        dialogText = "Camera permissions was peremanently " +
                                "denied. You can enable it in the app" +
                                "settings."

                    }
                }
            }
        }

    }

    mediaPermissionState.permissions.forEach { perm ->
        when (perm.permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                when {
                    perm.status.isGranted -> {
                        Log.e(TAG, "${perm.permission} permission granted")

                    }
                    perm.status.shouldShowRationale -> {
                        dialogText = "Media permissions are needed" + "to access device media"
                    }
                    perm.isPermanentlyDenied() -> {
                        dialogText = "Media permissions were peremanently" +
                                "denied. You can enable it in the app" +
                                "settings."

                    }
                }
            }
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                when {
                    perm.status.isGranted -> {
                        Log.e(TAG, "${perm.permission} permission granted")
                    }
                    perm.status.shouldShowRationale -> {
                        dialogText = "Media permissions are needed" + "to access device media"
                    }
                    perm.isPermanentlyDenied() -> {
                        dialogText = "Media permissions were peremanently" +
                                "denied. You can enable it in the app" +
                                "settings."

                    }
                }
            }
        }

    }

    fun pickPhoto() {
        hasImage = false
        bitmap = null
        if (!mediaPermissionState.allPermissionsGranted) {
            imageUri?.let {
                bitmap = null
                imageUri = null

                mediaPermissionState.launchMultiplePermissionRequest()
            }
        }
        pickPhotoLauncher.launch("image/*")
    }

    fun takePhoto(uri: Uri) {
        hasImage = false
        bitmap = null
        if (cameraPermissionState.allPermissionsGranted) {

            takePictureLauncher.launch(uri)
        } else {
            // openDialog.value = true
            cameraPermissionState.launchMultiplePermissionRequest()
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val state = viewModel.state
    user.userName?.let { userName = it }
    user.photoUrl?.let { userPhotoUrl = it }
    user.uid?.let { uid = it }


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
            location = viewModel.place,
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
                    text = caption?:"",
                    onValueChange = { caption = it },
                    keyboardController = keyboardController,
                    modifier = Modifier
                        .constrainAs(textField) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    isHintVisible = hintVisibile

                )

                bitmap?.let {
                    ImageContainer(image = it, modifier = Modifier
                        .constrainAs(imageView) {
                            top.linkTo(textField.bottom, margin = 10.dp)
                        }
                        .padding(horizontal = 10.dp),
                        onRemove = {
                            bitmap = null
                            imageUri = null
                        },
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
            onPost = {
                if (caption!=null || imageUri != null) {
                    viewModel.addPost(
                        caption = caption,
                        photoUrl = imageUri,
                        imageBitmap = bitmap
                    )
                    Toast.makeText(context, "loading post..", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "the post is empty", Toast.LENGTH_SHORT).show()

                }
            },
            onTakePhoto = {

                imageUri = context.getImageUri()
                imageUri?.let {
                    Log.e(TAG, "uri:$it")
                    takePhoto(it)
                }

            }
        )

    }

    BackHandler(true) {
        navHostController.navigateUp()
        navHostController.popBackStack()
    }

    LaunchedEffect(key1 = hasImage, key2 = imageUri) {
        if (hasImage) {
            imageUri?.let {

                bitmap = context.getBitmapFromUri(it)

                Log.e(TAG, bitmap.toString())

            }

        }
    }

    LaunchedEffect(key1 = state) {
        if (state.success) {
            navHostController.navigateUp()
        }

        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

    }

    LaunchedEffect(key1 = caption){
        caption?.let { hintVisibile = true }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && !mediaPermissionState.allPermissionsGranted) {
                mediaPermissionState.launchMultiplePermissionRequest()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

    }


}
