package com.amirami.simapp.priceindicatortunisia.ui.componenet

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDialogViewModel

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
                .background(Color.LightGray)
        ) {
            states.forEach { text->
                androidx.compose.material.Text(
                    text = text.text,
                    color = Color.White,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable {
                            onSelectionChange(text)
                            productDetailDialogViewModel.ontoggleBtnSelectedStatesStatesChanged(text.text)
                        }
                        .background(
                            if (text == selectedOption) {
                                Color.Magenta
                            } else {
                                Color.LightGray
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

