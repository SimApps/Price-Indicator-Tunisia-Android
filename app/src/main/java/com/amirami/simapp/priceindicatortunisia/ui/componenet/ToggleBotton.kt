package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel

@Composable
fun ToggleBotton(productDetailDialogViewModel: ProductDetailDialogViewModel) {
    val context= LocalContext.current
    val states = listOf(
        ToggleButtonOption(context.getString(R.string.Prix),null)  ,
        ToggleButtonOption(context.getString(R.string.Promotions),null),
        ToggleButtonOption(context.getString(R.string.Bonus),null),
    )

    var selectedOption by remember {
        mutableStateOf(states[0])
    }
    val onSelectionChange = { text: ToggleButtonOption ->
        selectedOption = text
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier

    ) {
        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
        ) {
            states.forEach { text->
                Text(
                    text = text.text,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable {
                            onSelectionChange(text)
                            productDetailDialogViewModel.ontoggleBtnSelectedStatesStatesChanged(text.text)
                        }
                        .background(
                            if (text == selectedOption) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSecondary
                            }
                        )
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp,
                        )

                )
            }
        }
    }
}



data class ToggleButtonOption(
    val text: String,
    val iconRes: Int?,
)

