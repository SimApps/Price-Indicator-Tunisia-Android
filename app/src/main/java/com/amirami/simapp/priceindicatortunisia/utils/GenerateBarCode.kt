package com.amirami.simapp.priceindicatortunisia.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.domain.model.FidCardEntity

fun generateBarCode(barcod: FidCardEntity, barCodeViewModel: BarCodeViewModel)/*: Bitmap*/ {


      val width = 700
      val height =  350
   // val width = 500
  //  val height = 150
         val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
         val codeWriter = MultiFormatWriter()
         try {
             val bitMatrix = codeWriter.encode(
                 barcod.value,
                 getBarcodeFormat(barcod.barecodeformat),  //BarcodeFormat.CODE_128,
                 width,
                 height
             )
             for (x in 0 until width) {
                 for (y in 0 until height) {
                     val color = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                     bitmap.setPixel(x, y, color)
                 }
             }
         } catch (e: WriterException) {
             Log.d("TAG", "generateBarCode: ${e.message}")
         }

         barCodeViewModel.generateBarCodeBitmap(bitmap)
      //   return bitmap



  /*
    implementation('com.journeyapps:zxing-android-embedded:4.3.0') { transitive = false }


  try {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(barecode.value,  getBarcodeFormat(barecode.barecodeformat),600 , 300)
        fidCardsScreenViewModel.onfidCardBitmap(bitmap)
    } catch (e: Exception) {
    }*/
}


fun getBarcodeFormat(barcodeFormat: Int): BarcodeFormat {

    return when (barcodeFormat) {
        1 -> BarcodeFormat.CODE_128
        2 -> BarcodeFormat.CODE_39
        4 -> BarcodeFormat.CODE_93
        8 -> BarcodeFormat.CODABAR
        16 -> BarcodeFormat.DATA_MATRIX
        32 -> BarcodeFormat.EAN_13
        64 -> BarcodeFormat.EAN_8
        128 -> BarcodeFormat.ITF
        256 -> BarcodeFormat.QR_CODE
        512 -> BarcodeFormat.UPC_A
        1024 -> BarcodeFormat.UPC_E
        //2048->BarcodeFormat.PDF417
        4096 -> BarcodeFormat.AZTEC
        else -> {
            BarcodeFormat.CODE_128
        }
    }


}
