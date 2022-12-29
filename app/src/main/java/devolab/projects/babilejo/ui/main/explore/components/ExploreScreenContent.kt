package devolab.projects.babilejo.ui.main.explore.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.ui.authentication.components.AuthProgressBar
import devolab.projects.babilejo.ui.main.explore.ExploreViewModel

@Composable
fun ExploreScreenContent(viewModel: ExploreViewModel = hiltViewModel(),
) = with(viewModel) {
    val context = LocalContext.current

    when(currentPosition){
        is Resource.Error -> {
            print(currentPosition.message.toString())
            Toast.makeText(context,currentPosition.message.toString(),Toast.LENGTH_SHORT).show()
        }
        is Resource.Loading -> {
            AuthProgressBar()
        }
        is Resource.Success -> {
            currentPosition.data?.let { location ->
                ExploreMapView(location = location, lastKnownPosition = lastKnownPosition)
            }
        }
    }
}