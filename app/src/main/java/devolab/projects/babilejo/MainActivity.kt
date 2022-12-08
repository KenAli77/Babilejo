package devolab.projects.babilejo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import devolab.projects.babilejo.navigation.RootNavGraph
import devolab.projects.babilejo.ui.theme.BabilejoTheme
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import javax.inject.Inject

const val REQUEST_ONE_TAP = 0

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private lateinit var navController: NavHostController

    @Inject
    lateinit var userAuthViewModel: UserAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            BabilejoTheme {
                navController = rememberNavController()

                RootNavGraph(
                    navController = navController,
                    userViewModel = userAuthViewModel
                )
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

