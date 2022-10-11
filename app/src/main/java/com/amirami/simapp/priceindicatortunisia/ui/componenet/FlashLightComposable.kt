package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel

@Composable
fun FlashLightComposable(barCodeViewModel: BarCodeViewModel){
    //Flashlight state(Boolean): TRUE or FALSE


    //Full Screen UI BOX composable
    Box(
        modifier = Modifier
            .padding(12.dp)
            .size(50.dp)
        // .background(Color.Black),
        //contentAlignment = Alignment.Center,
    ){
        //RENDER background image dynamically depending on state
        Icon(painter = if (barCodeViewModel.flashState) painterResource(id = R.drawable.ic_flash_on_indicator) else painterResource(
            id = R.drawable.ic_flash_off_indicator
        ), contentDescription = "power-off", modifier = Modifier
            .size(45.dp)
            .clickable {
                //TOGGLE FLASHLIGHT
                if(barCodeViewModel.flashState) barCodeViewModel.onflashState(false)
                else barCodeViewModel.onflashState(true)



            }
            ,  tint = MaterialTheme.colorScheme.surfaceTint)

    }

}
