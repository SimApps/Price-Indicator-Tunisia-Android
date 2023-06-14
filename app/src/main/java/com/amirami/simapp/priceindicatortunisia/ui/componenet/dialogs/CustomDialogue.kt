package com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amirami.simapp.priceindicatortunisia.settings.DialogueInfo


@Composable
fun CustomDialogue(
    dialogueInfo : DialogueInfo,
    onDismiss: () -> Unit,
    onClick : () -> Unit = {}
){
    val context = LocalContext.current

    Dialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onDismiss()

        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        ),
        content = {
            Card(

                elevation = CardDefaults.cardElevation(),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Text(
                        text = dialogueInfo.title,
                        textAlign = TextAlign.Center,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight


                        )
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                     Divider()
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                    Text(
                        text = dialogueInfo.msg,
                        textAlign = TextAlign.Justify,

                        )








                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Divider()
                    Spacer(modifier = Modifier.padding(top = 12.dp))

                    IconButton(
                        onClick =  onClick
                    ) {
                        Icon(
                            imageVector = dialogueInfo.imageVector,
                            contentDescription = ""//stringResource(id =   R.string.icn_search_back_content_description)
                        )
                    }
                    Spacer(modifier = Modifier.padding(top = 12.dp))

                }
            }

        }
    )
}