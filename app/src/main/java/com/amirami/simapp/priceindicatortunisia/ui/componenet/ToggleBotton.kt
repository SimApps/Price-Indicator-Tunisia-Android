package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.amirami.simapp.priceindicatortunisia.R

@Composable
fun ToggleBotton(
    selectedOption : String,
    onSelectionChange : (String) -> Unit
) {
    val context= LocalContext.current
    val states = listOf(
        ToggleButtonOption(context.getString(R.string.Prix),null)  ,
        ToggleButtonOption(context.getString(R.string.Promotions),null),
        ToggleButtonOption(context.getString(R.string.Bonus),null),
    )




        Row(
            modifier= Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically,
        ) {
            states.forEach { text->
                AssistChip(
                    onClick = {
                        onSelectionChange(text.text)
                    },
                    label = { Text(text =text.text) },
                    leadingIcon = {
                        AnimatedVisibility(visible = text.text == selectedOption) {
                            Icon(
                                Icons.Filled.Done,
                                contentDescription = "filter by ",
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    }
                )

            }
        }

}



data class ToggleButtonOption(
    val text: String,
    val iconRes: Int?,
)

