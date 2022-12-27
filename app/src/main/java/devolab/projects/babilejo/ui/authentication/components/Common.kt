package devolab.projects.babilejo.ui.authentication.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devolab.projects.babilejo.R
import devolab.projects.babilejo.ui.theme.Blue
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.ui.theme.quicksand

@Composable
fun InputField(
    input: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "email",
    icon: ImageVector = Icons.Rounded.Email,
    type: KeyboardType = KeyboardType.Email,
    password: Boolean = false
) {

    TextField(
        value = input,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,

                )

        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = Color.Black
        ),
        label = { Text(text = placeholder, color = Color.DarkGray.copy(0.3f)) },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = type,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (password) PasswordVisualTransformation() else {
            VisualTransformation.None
        }

    )

}

@Preview
@Composable
fun Logo(modifier: Modifier = Modifier, color: Color = Color.White) {

    Text(
        text = "Babilejo",
        fontFamily = quicksand,
        color = color,
        fontWeight = FontWeight.Bold,
        fontSize = 60.sp,
        modifier = modifier
    )
}


@Composable
fun SignUpRow(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Text(
            text = "New here?",
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray.copy(0.5f),
            fontFamily = quicksand
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Sign up",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = quicksand,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Preview
@Composable
fun RegularButton(
    modifier: Modifier = Modifier,
    text: String = "button",
    textColor: Color = Color.White,
    backgroundColor: Color = Yellow,
    onClick: () -> Unit = {},
    leadingIcon: Painter? = null
) {

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.clickable { onClick() },
        elevation = 0.3.dp,

        ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {


            ButtonText(
                text = text.lowercase(),
                color = textColor,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }


    }
}

@Preview
@Composable
fun GoogleAuthButton(
    modifier: Modifier = Modifier,
    text: String = "button",
    textColor: Color = Color.Blue,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit = {},
) {

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.clickable { onClick() },
        elevation = 0.3.dp,

        ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "google",
                modifier = Modifier.size(25.dp)

            )

            ButtonText(
                text = text.lowercase(),
                color = textColor,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }


    }
}

@Composable
fun ButtonText(text: String, modifier: Modifier = Modifier, color: Color = Color.White) {
    Text(
        text = text,
        fontWeight = FontWeight.Normal,
        fontFamily = quicksand,
        fontSize = 18.sp,
        color = color,
        modifier = modifier
    )
}

@Composable
fun Heading(text: String, modifier: Modifier = Modifier, color: Color = Color.White) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        fontFamily = quicksand,
        modifier = modifier.paddingFromBaseline(top = 16.dp),
        color = color
    )
}

@Composable
fun TermsAndCo(
    modifier: Modifier = Modifier,
    termsAndCo: () -> Unit = {},
    privacyPolicy: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "By signing up, you agree to our", fontSize = 12.sp)
            Text(
                text = "Terms & Conditions",
                fontWeight = FontWeight.Bold,
                color = Blue,
                fontSize = 12.sp,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clickable { termsAndCo() }
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "and", fontSize = 12.sp)
            Text(
                text = "Privacy Policy",
                fontWeight = FontWeight.Bold,
                color = Blue,
                maxLines = 1,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clickable { privacyPolicy() }

            )
        }


    }
}

@Composable
fun ProgressBar() {
    Log.e("ProgressBar","Composing")
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(color = Yellow)
    }

    Log.e("ProgressBar","Composed")

}