package devolab.projects.babilejo.ui.authentication

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import devolab.projects.babilejo.R
import devolab.projects.babilejo.navigation.AuthScreens
import devolab.projects.babilejo.navigation.Graph
import devolab.projects.babilejo.ui.theme.Blue
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.ui.theme.quicksand
import devolab.projects.babilejo.ui.authentication.components.*


@Composable
fun LoginScreen(
    viewModel: UserAuthViewModel,
    navController: NavHostController = rememberNavController(), ) {

    LoginContent(viewModel = viewModel, navController = navController)
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credentials =
                        viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                    val googleIdToken = credentials.googleIdToken
                    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                    viewModel.signInWithGoogle(googleCredentials)
                } catch (it: ApiException) {
                    print(it)
                }
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    OneTapSignIn(
        launch = {
            launch(it)
        }
    )

    GoogleLogin(
        navigateToHomeScreen = { signedIn ->
            if (signedIn) {
                navController.navigate(Graph.MAIN_ROUTE)
            }
        }
    )

    EmailLogin(navController= navController, viewModel = viewModel)

}

@Composable
fun LoginContent(
    viewModel: UserAuthViewModel,
    navController: NavHostController = rememberNavController(
    ),
) {
    var eMail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface(color = Yellow, modifier = Modifier.fillMaxSize()) {


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            CircularProgressIndicator(
                color = Yellow,
                strokeWidth = 5.dp,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp, vertical = 100.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Logo(modifier = Modifier.align(Start))
            Spacer(modifier = Modifier.height(80.dp))

            InputField(
                input = eMail,
                placeholder = stringResource(R.string.email),
                icon = Icons.Rounded.Mail,
                onValueChange = { eMail = it },
                type = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(5.dp))
            InputField(
                input = password,
                placeholder = stringResource(R.string.password),
                icon = Icons.Rounded.Lock,
                onValueChange = { password = it },
                password = true,
                type = KeyboardType.Password
            )

            Text(
                text = stringResource(R.string.password_recover),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 5.dp)
                    .clickable { },
                color = Color.Red.copy(0.5f),
                fontFamily = quicksand,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(50.dp))
            RegularButton(
                text = stringResource(R.string.login),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.logInUser(email = eMail, password = password)
                    eMail = ""
                    password = ""
                },
                backgroundColor = Blue
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.or),
                fontSize = 18.sp,
                color = Color.DarkGray.copy(0.7f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            GoogleAuthButton(
                text = stringResource(R.string.google_login),
                modifier = Modifier.fillMaxWidth(),
                textColor = Color.DarkGray.copy(0.5f),
                onClick = { viewModel.oneTapSignIn() },
            )
            Spacer(modifier = Modifier.height(10.dp))
            SignUpRow(onClick = { navController.navigate(AuthScreens.Signup.route) })

        }


    }

}


