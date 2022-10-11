package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun Lottie(raw : Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(raw))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition,
        { progress },
        contentScale= ContentScale.FillBounds,
        modifier = Modifier.height(200.dp).width(200.dp)
    )
}