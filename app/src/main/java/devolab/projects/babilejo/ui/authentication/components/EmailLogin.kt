package devolab.projects.babilejo.ui.authentication.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.theme.Yellow

@Composable
fun EmailLogin(
    viewModel: UserAuthViewModel = hiltViewModel(),
    navigateHome:() -> Unit
) {
    Log.e("EmailLogin","composed")
    val context = LocalContext.current
    when (val authResponse = viewModel.loginState) {
        is Resource.Loading -> //
        {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(color = Yellow)
            }
            Log.e("EmailLogin","Loading..") }
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