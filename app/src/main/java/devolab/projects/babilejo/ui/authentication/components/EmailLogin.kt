package devolab.projects.babilejo.ui.authentication.components

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@Composable
fun EmailLogin(
    viewModel: UserAuthViewModel = hiltViewModel(),
    navigateHome:() -> Unit
) {
    Log.e("EmailLogin","composed")
    val context = LocalContext.current
    when (val authResponse = viewModel.loginState) {
        is Resource.Loading ->
        { AuthProgressBar() }
        is Resource.Success -> authResponse.data?.let {
            LaunchedEffect(it) {
                navigateHome()
            }
        }
        is Resource.Error -> LaunchedEffect(Unit) {
            print(authResponse.message.toString())
            Toast.makeText(context,authResponse.message, Toast.LENGTH_LONG).show()
        }
    }
}