package devolab.projects.babilejo.ui.authentication

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@Composable
fun GoogleLogin(
    viewModel: UserAuthViewModel = hiltViewModel(),
    navigateToHomeScreen: (signedIn: Boolean) -> Unit
) {
    val context = LocalContext.current
    when(val signInWithGoogleResponse = viewModel.signInWithGoogleResponse) {
        is Resource.Loading -> ProgressBar()
        is Resource.Success -> signInWithGoogleResponse.data?.let { signedIn ->
            LaunchedEffect(signedIn) {
                navigateToHomeScreen(signedIn)
            }
        }
        is Resource.Error -> LaunchedEffect(Unit) {
            print(signInWithGoogleResponse.message.toString())
            Toast.makeText(context,signInWithGoogleResponse.message, Toast.LENGTH_LONG).show()
        }
    }
}