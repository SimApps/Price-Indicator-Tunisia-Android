@file:OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)

package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.core.StringFormatting
import com.amirami.simapp.priceindicatortunisia.utils.CustomModifiers.customWidth
import com.amirami.simapp.priceindicatortunisia.utils.Functions

@Composable
fun AnimatedText(
    modifier: Modifier = Modifier,
    label : String,
    count: Double,
    onValueChange: (String) -> Unit,

    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var oldCount by remember {
        mutableStateOf(count)
    }
    SideEffect {
        oldCount = count
    }
    Row(modifier = modifier) {
        val countString = count.toString()
        val oldCountString = oldCount.toString()
        for(i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]
            val char = if(oldChar == newChar) {
                oldCountString[i]
            } else {
                countString[i]
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } with slideOutVertically { -it }
                }, label = ""
            ) { char ->

             /*   OutlinedTextField(
                    value = char.toString(),
                    onValueChange = {
                        onValueChange(it)
                                    },
                   // modifier = Modifier.customWidth(LocalConfiguration.current, 0.50f),
                    label = { Text(label) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions{
                        keyboardController?.hide()
                    }
                )*/
                 Text(
                    text = char.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}