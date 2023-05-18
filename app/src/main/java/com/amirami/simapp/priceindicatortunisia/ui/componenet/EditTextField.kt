package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditTextField(
    modifier: Modifier,
    showLeadingIcon : Boolean = false,
    showTrailingIcon : Boolean = true,
    text: String,
    errorvalue: String?,
    label: String,
    leadingIcon: ImageVector = Icons.Default.Home,
    imeAction: ImeAction,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    onKeyboardActions: () -> Unit = { }
    ) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column() {
        val context = LocalContext.current
        val isFocused by rememberSaveable { mutableStateOf(false) }
if(showLeadingIcon)
        OutlinedTextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },

            readOnly = readOnly,
            enabled = enabled,
            modifier = modifier,
            label = { Text(label, maxLines = 1) },
            trailingIcon = {
                AnimatedVisibility(
                    visible = text.isNotEmpty() && enabled && showTrailingIcon,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {


                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "cd"
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            isError = errorvalue != null,
            leadingIcon = {
                if(showLeadingIcon) Icon(
                    leadingIcon,
                    contentDescription = null,
                    //tint = Color.Black
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                autoCorrect = true,
                imeAction = imeAction
            )
        )


        else
    OutlinedTextField(
        value = text,
        onValueChange = {
            onValueChange(it)
        },

        readOnly = readOnly,
        enabled = enabled,
        modifier = modifier,
        label = { Text(label, maxLines = 1) },
        trailingIcon = {
            AnimatedVisibility(
                visible = text.isNotEmpty() && enabled && showTrailingIcon,
                enter = fadeIn(),
                exit = fadeOut()
            ) {


                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "cd"
                    )
                }
            }
        },
        shape = MaterialTheme.shapes.medium,
        singleLine = true,
        isError = errorvalue != null,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            autoCorrect = true,
            imeAction = imeAction
        )
    )


        if (errorvalue != null) {
            Text(
                text = errorvalue,
                color = MaterialTheme.colorScheme.error,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}