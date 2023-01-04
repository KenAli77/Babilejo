package devolab.projects.babilejo.ui.main.explore

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.authentication.components.AuthProgressBar
import devolab.projects.babilejo.ui.main.explore.components.ExploreMapView

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ExploreScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<ExploreViewModel>()

    with(viewModel){

        val context = LocalContext.current

        LaunchedEffect(key1 = currentPosition.error) {
            currentPosition.error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        if (currentPosition.loading) {
            AuthProgressBar()
        }

                ExploreMapView(location = location, users = usersData)


    }


}



