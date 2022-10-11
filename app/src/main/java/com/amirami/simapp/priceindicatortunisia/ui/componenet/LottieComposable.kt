package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amirami.simapp.priceindicatortunisia.R

@Composable
fun LottieComposable(size: Dp, lottiefile :Int) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottiefile))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = true,
        iterations = Int.MAX_VALUE
    )

    LottieAnimation(
        modifier= Modifier.size(size),
        composition = composition,
        progress = { progress }
    )
}