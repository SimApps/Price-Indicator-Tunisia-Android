package com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import javax.inject.Inject


class BarCodeViewModel @Inject constructor() : ViewModel() {
    var showAddFidCard by mutableStateOf(false)
    fun onShowAddFidCardChanged(value: Boolean) {
        showAddFidCard = value
    }
    var fidCardBarCodeInfo by mutableStateOf(FidCardEntity(name = "",value=  "",barecodeformat= -1,barecodetype= -1))
        private set
    fun onfidCardInfo(fidCardBarCodeInf: FidCardEntity) {
        fidCardBarCodeInfo = fidCardBarCodeInf
    }

    var sendBarCodeTo by mutableStateOf("")
        private set
    fun onsendBarCodeTo(sendBarCodeto: String) {
        sendBarCodeTo = sendBarCodeto
    }


    var BarCodeValue by mutableStateOf("")
        private set
    fun onBarCodeValue(barCodeValue: String) {
        BarCodeValue = barCodeValue
    }

    var flashState by mutableStateOf(true)
        private set
    fun onflashState(flashstate: Boolean) {
        flashState = flashstate
    }

    var fidCardAction by mutableStateOf("")
        private set
    fun onfidCardActionInfo(fidCardActionInf: String) {
        fidCardAction = fidCardActionInf
    }


    var barCodeBitmap by mutableStateOf<Bitmap?>(null)
        private set
    fun generateBarCodeBitmap(barCodBitmap: Bitmap) {
        barCodeBitmap= barCodBitmap
    }

}