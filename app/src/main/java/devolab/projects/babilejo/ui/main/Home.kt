package devolab.projects.babilejo.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devolab.projects.babilejo.R
import devolab.projects.babilejo.ui.authentication.*
import devolab.projects.babilejo.ui.navigation.Screens
import devolab.projects.babilejo.ui.theme.Blue
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.ui.theme.quicksand
import devolab.projects.babilejo.ui.viewmodel.UserAuthViewModel

@Composable
fun HomeScreen(viewModel: UserAuthViewModel){

    Surface(color = Yellow.copy(0.1f), modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp, vertical = 100.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = "Welcome ${viewModel.googleLoginState.data?.user?.email}")

        }


    }


}