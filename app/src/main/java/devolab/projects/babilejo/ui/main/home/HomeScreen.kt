package devolab.projects.babilejo.ui.main.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.authentication.components.LogoutDialog
import devolab.projects.babilejo.ui.main.UserProfileViewModel

@Composable
fun HomeScreen(navController: NavHostController, authViewModel: UserAuthViewModel) {
    val context = LocalContext.current

    val profileViewModel = hiltViewModel<UserProfileViewModel>()

    val userName = remember { mutableStateOf("") }

    LaunchedEffect(key1 = profileViewModel.userDataState.data) {
        profileViewModel.userDataState.data?.let {
            userName.value = it.userEmail.toString()
        }
    }

    val openDialog = remember {
        mutableStateOf(false)
    }

    BackHandler(true) {

        Toast.makeText(context, "log out?", Toast.LENGTH_SHORT).show()
        authViewModel.logOut()
        openDialog.value = true


    }

    LogoutDialog(openDialog = openDialog, logout = { navController.navigateUp() })
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = userName.value, fontSize = 50.sp, color = Color.Black)
        }
    }
}