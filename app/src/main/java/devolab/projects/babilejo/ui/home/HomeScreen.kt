package devolab.projects.babilejo.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel

@Composable
fun HomeScreen(viewModel: UserAuthViewModel, navController: NavHostController){

    Surface(color = Yellow.copy(0.1f), modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp, vertical = 100.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = "Welcome", color = Color.White, fontSize = 50.sp)

        }

    }


}