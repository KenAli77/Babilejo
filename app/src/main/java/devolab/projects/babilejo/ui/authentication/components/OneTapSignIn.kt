package devolab.projects.babilejo.ui.authentication

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@Composable
fun OneTapSignIn(
    viewModel: UserAuthViewModel = hiltViewModel(),
    launch: (result: BeginSignInResult) -> Unit
) {
    val context = LocalContext.current
    when (val oneTapSignInResponse = viewModel.googleLoginState) {
        is Resource.Loading -> ProgressBar()
        is Resource.Success -> oneTapSignInResponse.data?.let {
            LaunchedEffect(it) {
                launch(it)
            }
        }
        is Resource.Error -> LaunchedEffect(Unit) {
            print(oneTapSignInResponse.message.toString())
            Toast.makeText(context,oneTapSignInResponse.message,Toast.LENGTH_LONG).show()
        }
    }
}