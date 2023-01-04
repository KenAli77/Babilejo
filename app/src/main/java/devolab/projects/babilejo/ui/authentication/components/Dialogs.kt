package devolab.projects.babilejo.ui.authentication.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogoutDialog(openDialog: MutableState<Boolean>, logout: () -> Unit) {
    // Create a state variable to track the visibility of the alert dialog
    val showDialog by remember { mutableStateOf(openDialog) }

    // When the showDialog state variable is true, show the alert dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Log out", fontSize = 25.sp) },
            text = { Text("Are you sure you want to log out?", fontSize = 20.sp) },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "cancel", color = Color.Blue, modifier = Modifier.clickable {
                        showDialog.value = false
                    })
                    Text(text = "log out", color = Color.Red, modifier = Modifier.clickable {
                        logout()
                        showDialog.value = false
                    })

                }

            }
        )
    }

}

@Composable
fun BasicAlertDialog(
    openDialog: MutableState<Boolean>,
    onAction: () -> Unit,
    text: String,
    title: String,
    actionText:String,

) {
    // Create a state variable to track the visibility of the alert dialog
    val showDialog by remember { mutableStateOf(openDialog) }

    // When the showDialog state variable is true, show the alert dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(title, fontSize = 20.sp) },
            text = { Text(text, fontSize = 18.sp) },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "dismiss", color = Color.Blue, modifier = Modifier.clickable {
                        showDialog.value = false
                    })
                    Text(text = actionText, color = Color.Blue, modifier = Modifier.clickable {
                        onAction()
                        showDialog.value = false
                    })

                }

            }
        )
    }

}
