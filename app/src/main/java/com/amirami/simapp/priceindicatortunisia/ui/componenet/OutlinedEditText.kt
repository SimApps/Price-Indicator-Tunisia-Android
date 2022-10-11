package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun OutlinedEditText(text : String,labl : Int,onValueChange: () -> Unit) {
    val context = LocalContext.current

    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange() },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(context.getString(labl)) },
    )
}