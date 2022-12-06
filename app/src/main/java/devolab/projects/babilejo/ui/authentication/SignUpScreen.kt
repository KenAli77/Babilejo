package devolab.projects.babilejo.ui.authentication

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import devolab.projects.babilejo.R
import devolab.projects.babilejo.ui.navigation.MAIN_ROUTE
import devolab.projects.babilejo.ui.theme.Blue
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.ui.theme.quicksand
import devolab.projects.babilejo.ui.viewmodel.UserAuthViewModel

@Composable
fun SignUpScreen(
    viewModel: UserAuthViewModel, navController: NavHostController = rememberNavController(
    ),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    val state = viewModel.signUpState
    val googleLogin = viewModel.loginState
    var userName by remember { mutableStateOf("") }
    var eMail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                it,
                null,
                SnackbarDuration.Short
            )

            viewModel.resetSignUpState()



        }

    }
    LaunchedEffect(key1 = state.success,key2 = googleLogin  ) {
        if (state.success || googleLogin.success) {
            navController.navigate(MAIN_ROUTE)
            Toast.makeText(context, "account created!", Toast.LENGTH_LONG).show()
        }


    }
    Surface(color = Yellow.copy(0.1f), modifier = Modifier.fillMaxSize()) {

        if (state.loading) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                CircularProgressIndicator(
                    color = Yellow,
                    strokeWidth = 5.dp,
                )
            }

        } else
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 50.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start,
            ) {

                Spacer(modifier = Modifier.height(80.dp))
                Heading(text = stringResource(R.string.signup), Modifier.padding(bottom = 40.dp))
                InputField(
                    input = userName,
                    onValueChange = { userName = it },
                    placeholder = "username",
                    icon = Icons.Rounded.Person,
                    type = KeyboardType.Text
                )

                InputField(
                    input = eMail,
                    placeholder = stringResource(R.string.email),
                    icon = Icons.Rounded.Mail,
                    onValueChange = { eMail = it },
                    type = KeyboardType.Email
                )
                InputField(
                    input = password,
                    placeholder = stringResource(R.string.password),
                    icon = Icons.Rounded.Lock,
                    onValueChange = { password = it },
                    password = true,
                    type = KeyboardType.Password
                )
                InputField(
                    input = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "confirm password",
                    icon = Icons.Rounded.Lock,
                    type = KeyboardType.Password,
                    true
                )

                Spacer(modifier = Modifier.height(20.dp))
                RegularButton(
                    text = stringResource(R.string.sign_up),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.createUser(
                            userName = userName,
                            email = eMail,
                            password = password,
                            confirmPassword = confirmPassword
                        )
                        password = ""
                        confirmPassword = ""

                    },
                    backgroundColor = Blue
                )
                Text(
                    text = stringResource(R.string.or),
                    fontSize = 18.sp,
                    color = Color.DarkGray.copy(0.7f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                GoogleAuthButton(
                    text = stringResource(R.string.google_login),
                    modifier = Modifier.fillMaxWidth(),
                    textColor = Color.DarkGray.copy(0.5f),
                    onClick = {viewModel.googleLogin()}
                )

                Spacer(modifier = Modifier.height(5.dp))

                TermsAndCo()

            }


    }

}