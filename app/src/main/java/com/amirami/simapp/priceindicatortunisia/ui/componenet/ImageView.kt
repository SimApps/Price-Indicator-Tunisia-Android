package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImageView(resId : Int) {

    val image = painterResource(resId)

    // Image is a pre-defined composable that lays out and draws a given [ImageBitmap].

    // You can think of Modifiers as implementations of the decorators pattern that are
    // used to modify the composable that its applied to. In this example, we configure the
    // Image composable to have a height of 200 dp.
    Image(
        painter = image,
        contentDescription = null,
        modifier = Modifier
            .sizeIn(maxHeight = 200.dp)
            .fillMaxWidth()
    )
}