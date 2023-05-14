package com.amirami.simapp.priceindicatortunisia.utils

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

object CustomModifiers {

    fun Modifier.detectTapGestures(
                         onPress : () -> Unit,
                          onDoubleTap : () -> Unit,
                          onLongPress : () -> Unit,
                          onTap : () -> Unit ):Modifier =
       pointerInput(Unit) {
            detectTapGestures(
                onPress = { onPress() },/* Called when the gesture starts */
                onDoubleTap = { onDoubleTap() },
                onLongPress = { onLongPress() },
                onTap = { onTap() } /* Called on Tap */
            )
        }


    fun Modifier.customWidth(
        configuration: Configuration,
        percentage : Float
        ):Modifier =
        requiredWidth(configuration.screenWidthDp.dp * percentage)
    }
