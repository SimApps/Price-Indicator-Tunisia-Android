package com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview

import androidx.compose.foundation.clickable
import com.amirami.simapp.priceindicatortunisia.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun IconsInSearchView(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onScanClick: () -> Unit = {},
   // onClearClick: (() -> Unit)? = null,
    onFocusChanged: (FocusState) -> Unit = {},
    onValueChanged: (String) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.barcode_scanner))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = false,
        iterations = Int.MAX_VALUE
    )
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it) },
        value = value,
        label =  { Text(text = label) } ,
        onValueChange = {query ->
            onValueChanged(query)
        },
        placeholder = { Text(text = label)},
        leadingIcon = {
            Icon(
                imageVector =  Icons.Default.Search,
                contentDescription = ""
            )
        },

        trailingIcon ={
            if(value.isNotEmpty())
            Icon(
                imageVector =  Icons.Default.Close,
                contentDescription = "",
                Modifier.clickable { onClearClick()}
            )
            else  LottieAnimation(
                modifier= Modifier
                    .clickable {
                        onScanClick()
                    }
                    .size(40.dp),
                composition = composition,
                progress = { progress }
            )
        },
        keyboardActions = KeyboardActions(onSearch = {
            onDoneActionClick()
        }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        )
    )
}