package com.amirami.simapp.priceindicatortunisia.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentBarecodeImageBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception


@AndroidEntryPoint
class ImageFidCarDialogFragment : Fragment(R.layout.fragment_barecode_image) {
    lateinit var _binding: FragmentBarecodeImageBinding

    val args : ImageFidCarDialogFragmentArgs by navArgs()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


            _binding=  FragmentBarecodeImageBinding.bind(view)


            fun barecodeformat(FidCard_CodeBarre_Type:String): BarcodeFormat {
                when (FidCard_CodeBarre_Type) {
                    "EAN_13" -> return BarcodeFormat.EAN_13
                    "QR_CODE" -> return BarcodeFormat.QR_CODE
                    "AZTEC" -> return BarcodeFormat.AZTEC
                    "CODABAR" -> return BarcodeFormat.CODABAR
                    "CODE_128" -> return BarcodeFormat.CODE_128
                    "CODE_39" -> return BarcodeFormat.CODE_39
                    "CODE_93" -> return BarcodeFormat.CODE_93
                    "DATA_MATRIX" -> return BarcodeFormat.DATA_MATRIX
                    "EAN_8" -> return BarcodeFormat.EAN_8
                    "ITF" -> return BarcodeFormat.ITF
                    "MAXICODE" -> return BarcodeFormat.MAXICODE
                    "PDF_417" -> return BarcodeFormat.PDF_417
                    "RSS_14" -> return BarcodeFormat.RSS_14
                    "RSS_EXPANDED" -> return BarcodeFormat.RSS_EXPANDED
                    "UPC_A" -> return BarcodeFormat.UPC_A
                    "UPC_E" -> return BarcodeFormat.UPC_E
                    "UPC_EAN_EXTENSION" -> return BarcodeFormat.UPC_EAN_EXTENSION
                    else -> return BarcodeFormat.EAN_13
                }
            }

            if(args.name!="Empty"){
                _binding.apply {
                    cardFidNameTxVw.text= args.name
                    cardFidvalueTxVw.text=args.value

                    try {
                        val barcodeEncoder = BarcodeEncoder()
                        val bitmap = barcodeEncoder.encodeBitmap(args.value, barecodeformat(args.type),600 , 300)
                        generatedBarecodeImvw.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                    }

                }
            }
    }
}
