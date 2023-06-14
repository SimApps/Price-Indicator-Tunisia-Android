package com.amirami.simapp.priceindicatortunisia.cartefidelite.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel

@Composable
fun GeneratedBarcodeImageScreen(
    barCodeViewModel: BarCodeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = barCodeViewModel.fidCardBarCodeInfo.name)
        Spacer(modifier = Modifier.height(20.dp))
        barCodeViewModel.barCodeBitmap?.asImageBitmap()?.let { it ->
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
