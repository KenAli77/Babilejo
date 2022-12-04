package devolab.projects.babilejo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import devolab.projects.babilejo.ui.authentication.LoginScreen
import devolab.projects.babilejo.ui.navigation.RootNavGraph
import devolab.projects.babilejo.ui.theme.BabilejoTheme
import devolab.projects.babilejo.ui.viewmodel.UserAuthViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController:NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BabilejoTheme {
                val userAuthViewModel = hiltViewModel<UserAuthViewModel>()
                navController = rememberNavController()

                RootNavGraph(navController = navController, userViewModel = userAuthViewModel)
              // LoginScreen(viewModel = userAuthViewModel, navController = navController)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BabilejoTheme {
        Greeting("Android")
    }
}