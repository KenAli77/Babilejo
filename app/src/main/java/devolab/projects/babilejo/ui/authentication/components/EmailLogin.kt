package devolab.projects.babilejo.ui.authentication

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.navigation.Screens
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@Composable
fun EmailLogin(
    viewModel: UserAuthViewModel = hiltViewModel(),
    navController:NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    when (val authResponse = viewModel.loginState) {
        is Resource.Loading -> ProgressBar()
        is Resource.Success -> authResponse.data?.let {
            LaunchedEffect(it) {
                navController.navigate(Screens.Home.route)
            }
        }
        is Resource.Error -> LaunchedEffect(Unit) {
            print(authResponse.message.toString())
            Toast.makeText(context,authResponse.message, Toast.LENGTH_LONG).show()
        }
    }
}