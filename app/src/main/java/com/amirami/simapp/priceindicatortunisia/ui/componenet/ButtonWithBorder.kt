package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ButtonWithBorder(
    modifier: Modifier,
    onClicks: () -> Unit,
    text: String,
) {
    Button(
        modifier = modifier,
        onClick = {
            onClicks()
            //your onclick code

            // LaunchedEffect(key1 = true) {

            // }
        },
      //  border = BorderStroke(1.dp, Color.Red),
      //  colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
    ) {
        Text(text = text)
    }
}