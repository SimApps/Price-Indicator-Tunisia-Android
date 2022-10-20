package com.amirami.simapp.priceindicatortunisia.ui.screens.addmodify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddModifyViewModel @Inject constructor() : ViewModel() {

    var barcodeTextValue by mutableStateOf<String>("")
        private set

    fun onbarcodeTextValue(text: String) {
        barcodeTextValue = text
    }

    var nomProduitTextValue by mutableStateOf<String>("")
        private set
    fun onNomProduitTextValue(text: String) {
        nomProduitTextValue = text
    }



    var nomProduitArabeTextValue by mutableStateOf<String>("")
        private set
    fun onNomProduitTextArabeValue(text: String) {
        nomProduitArabeTextValue = text
    }


}
