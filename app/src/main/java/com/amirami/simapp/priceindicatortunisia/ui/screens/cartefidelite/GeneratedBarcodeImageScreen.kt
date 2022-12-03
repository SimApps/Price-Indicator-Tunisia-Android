package com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel

@Composable
fun GeneratedBarcodeImageScreen(barCodeViewModel: BarCodeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            //  .background(androidx.compose.ui.graphics.Color.White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = barCodeViewModel.fidCardBarCodeInfo.name)
        Spacer(modifier = Modifier.height(20.dp))
        barCodeViewModel.fidCardBitmap?.asImageBitmap()?.let { it ->
            Image(
                bitmap = it,
                contentDescription = "Generate BarCode Image"
                //   modifier = Modifier.size(250.dp)
            )
        }
        Spacer(modifier = Modifier.height(9.dp))
        Text(text = barCodeViewModel.fidCardBarCodeInfo.value)
    }
}
