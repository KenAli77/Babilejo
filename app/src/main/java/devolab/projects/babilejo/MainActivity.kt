package devolab.projects.babilejo

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import devolab.projects.babilejo.navigation.RootNavGraph
import devolab.projects.babilejo.ui.theme.BabilejoTheme
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.ui.main.MainViewModel
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {

            val userAuthViewModel = hiltViewModel<UserAuthViewModel>()
            BabilejoTheme {
                navController = rememberNavController()

                RootNavGraph(
                    navController = navController,
                    userViewModel = userAuthViewModel,
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

