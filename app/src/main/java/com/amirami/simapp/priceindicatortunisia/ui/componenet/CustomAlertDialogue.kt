package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CustomAlertDialogue(
    title : String,
    msg : String,
    openDialog :Boolean =false,
    setDialogueVisibility : (Boolean) -> Unit,
    customAction :()-> Unit
) {

    val context = LocalContext.current

if (openDialog) {

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            setDialogueVisibility(false)
        },
        icon = { Icon(Icons.Filled.Warning, contentDescription = null) },
        title = {
            Text(text = title)
        },
        text = {
            Text(msg)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    customAction()
                    setDialogueVisibility(false)
                }
            ) {
                Text("Confirmer")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    setDialogueVisibility(false)
                }
            ) {
                Text("Exit")
            }
        }
    )
}
}