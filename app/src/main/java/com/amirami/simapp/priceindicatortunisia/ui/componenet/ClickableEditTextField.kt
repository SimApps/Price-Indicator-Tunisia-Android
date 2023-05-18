package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ClickableEditTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text : String,
    errorValue : String?,
    label : String,
    readOnly: Boolean,
    onClick : () -> Unit,
    onValueChange: (String) -> Unit
) {

    Box(modifier = modifier.height(IntrinsicSize.Min)) {
        EditTextField(
            modifier = modifier,

            text = text,
            errorvalue = errorValue,
            label = label,
            onValueChange = {
                onValueChange(it)
            },
            readOnly = true,
            enabled = readOnly,
            showLeadingIcon = false,
            showTrailingIcon = false,
            leadingIcon = Icons.Default.List,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )


        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = readOnly) { onClick() },
            color = Color.Transparent,
        ) {}
    }

}