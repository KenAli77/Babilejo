package devolab.projects.babilejo.ui.authentication.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@Composable
fun Signup(
    viewModel: UserAuthViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit
) {
//    val context = LocalContext.current
//    when(val signInWithGoogleResponse = viewModel.signUpState) {
//        is Resource.Loading -> AuthProgressBar()
//        is Resource.Success -> {
//
//            viewModel.signUpState.data?.let {
//                LaunchedEffect(key1 = it){
//                    navigateToHomeScreen()
//                }
//            }
//
//        }
//        is Resource.Error -> LaunchedEffect(Unit) {
//            print(viewModel.signUpState.message.toString())
//            Toast.makeText(context,signInWithGoogleResponse.message, Toast.LENGTH_LONG).show()
//        }
//    }
}