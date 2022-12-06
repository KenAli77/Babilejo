package devolab.projects.babilejo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import devolab.projects.babilejo.domain.LoginUser
import devolab.projects.babilejo.ui.authentication.LoginScreen
import devolab.projects.babilejo.ui.navigation.RootNavGraph
import devolab.projects.babilejo.ui.theme.BabilejoTheme
import devolab.projects.babilejo.ui.viewmodel.UserAuthViewModel
import devolab.projects.babilejo.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val REQUEST_CODE_SIGN_IN = 0

@AndroidEntryPoint
class MainActivity @Inject constructor(
    private val loginUser: LoginUser
) : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var googleLoginResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var signInClient: GoogleSignInClient

    private lateinit var userAuthViewModel: UserAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webclient_id))
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(this, options)

        userAuthViewModel.googleSignInClient.value = signInClient

        googleLoginResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == REQUEST_CODE_SIGN_IN) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                    account?.let {
                        googleAuthForFirebase(it)
                    }
                }
            }
        userAuthViewModel.googleLoginLauncher.value = googleLoginResultLauncher

        setContent {
            BabilejoTheme {
                navController = rememberNavController()
                userAuthViewModel = hiltViewModel<UserAuthViewModel>()

                RootNavGraph(navController = navController, userViewModel = userAuthViewModel)
            }
        }


    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        lifecycleScope.launch {
            val result = loginUser(account)

            userAuthViewModel.apply {
                loginState = when (result) {

                    is Resource.Error -> {
                        loginState.copy(
                            loading = false,
                            success = false,
                            error = result.message
                        )
                    }
                    is Resource.Loading -> {
                        loginState.copy(
                            loading = true,
                            success = false,
                            error = null
                        )
                    }
                    is Resource.Success -> {

                        loginState.copy(
                            data = result.data,
                            success = true,
                            loading = false,
                            error = null,
                            uid = result.data?.user?.uid
                        )
                    }
                }
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

