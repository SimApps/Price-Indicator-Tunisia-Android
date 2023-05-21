package com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.repository.FidCardRoomBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarCodeViewModel @Inject constructor(
    private val fidCardRoomBaseRepository: FidCardRoomBaseRepository
) : ViewModel() {

    var showDeleteFidCard by mutableStateOf(false)
    fun onShowDeleteFidCardChanged(value: Boolean) {
        showDeleteFidCard = value
    }


    var showAddFidCard by mutableStateOf(false)
    fun onShowAddFidCardChanged(value: Boolean) {
        showAddFidCard = value
    }
    var fidCardBarCodeInfo by mutableStateOf(FidCardEntity())
        private set
    fun onfidCardInfo(fidCardBarCodeInf: FidCardEntity) {
        fidCardBarCodeInfo = fidCardBarCodeInf
    }

    //var fidCardByValue by mutableStateOf(FidCardEntity())
    fun getFidCardByValue(value: FidCardEntity) = viewModelScope.launch {
        fidCardRoomBaseRepository.getByValue(value.value).collect { fidCrd ->
            fidCardBarCodeInfo = fidCrd ?: value
        }
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