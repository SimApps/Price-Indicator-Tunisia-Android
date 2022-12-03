package com.amirami.simapp.priceindicatortunisia.ui.screens.addmodify

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddModifyViewModel @Inject constructor() : ViewModel() {
    var magasinValue by mutableStateOf<String>("")
        private set
    fun onMagasinValue(text: String) {
        magasinValue = text
    }



    val unitselected: MutableState<String> = mutableStateOf("")

    var mesureUnitList by mutableStateOf<List<String>>(listOf())
        private set

    fun onmesureUnitListValue(text: List<String>) {
        mesureUnitList = text.toList()
    }
//        addModifyViewModel.onmesureUnitListValue(context.resources.getStringArray(R.array.unit_of_mesur))

    init {
        viewModelScope.launch {
            delay(3000)
            onmesureUnitListValue(listOf("L", "ml", "cl", "Kg", "g", "m", "cm", "Rouleaux", "Pièces", "Oeufs", "watt"))
            //  names.value = listOf("Jordan", "Leonard")
        }
    }

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

    var marquesTextValue by mutableStateOf<String>("")
        private set
    fun onMarquesTextValue(text: String) {
        marquesTextValue = text
    }

    var marquesArabeTextValue by mutableStateOf<String>("")
        private set
    fun onMarquesArabeTextValue(text: String) {
        marquesArabeTextValue = text
    }

    var prodTypeTextValue by mutableStateOf<String>("")
        private set
    fun onprodTypeTextValue(text: String) {
        prodTypeTextValue = text
    }

    var prodSubTypeTextValue by mutableStateOf<String>("")
        private set
    fun onprodSubTypeTextValue(text: String) {
        prodSubTypeTextValue = text
    }

    var prodSubSubTypeTextValue by mutableStateOf<String>("")
        private set
    fun onprodSubSubTypeTextValue(text: String) {
        prodSubSubTypeTextValue = text
    }

    var siezeTextValue by mutableStateOf<String>("")
        private set
    fun onSiezeTextValue(text: String) {
        siezeTextValue = text
    }

    var unitTextValue by mutableStateOf<String>("")
        private set
    fun onUnitTextValue(text: String) {
        unitTextValue = text
    }

    var imageLinkTextValue by mutableStateOf<String>("")
        private set
    fun onimageLinkTextValue(text: String) {
        imageLinkTextValue = text
    }

    var prodDescriptionTextValue by mutableStateOf<String>("")
        private set
    fun onprodDescriptionTextValue(text: String) {
        prodDescriptionTextValue = text
    }

    var prodDescriptionARTextValue by mutableStateOf<String>("")
        private set
    fun onprodDescriptionARTextValue(text: String) {
        prodDescriptionARTextValue = text
    }

    var monoprixPriceTextValue by mutableStateOf<String>("")
        private set
    fun onmonoprixPriceTextValue(text: String) {
        monoprixPriceTextValue = text
    }

    var monoprixPromotionTextValue by mutableStateOf<String>("")
        private set
    fun onmonoprixPromotionTextValue(text: String) {
        monoprixPromotionTextValue = text
    }

    var monoprixBonusSurCartFidTextValue by mutableStateOf<String>("")
        private set
    fun onMonoprixBonusSurCartFidTextValue(text: String) {
        monoprixBonusSurCartFidTextValue = text
    }


    var mgPriceTextValue by mutableStateOf<String>("")
        private set
    fun onMgPriceTextValue(text: String) {
        mgPriceTextValue = text
    }

    var mgPromotionTextValue by mutableStateOf<String>("")
        private set
    fun onMgPromotionTextValue(text: String) {
        mgPromotionTextValue = text
    }


    var mgBonusSurCartFidTextValue by mutableStateOf<String>("")
        private set
    fun onMgBonusSurCartFidTextValue(text: String) {
        mgBonusSurCartFidTextValue = text
    }

    var carrefourPriceTextValue by mutableStateOf<String>("")
        private set
    fun onCarrefourPriceTextValue(text: String) {
        carrefourPriceTextValue = text
    }

    var carrefourPromotionTextValue by mutableStateOf<String>("")
        private set
    fun onCarrefouPromotionTextValue(text: String) {
        carrefourPromotionTextValue = text
    }


    var carrefourBonusSurCartFidTextValue by mutableStateOf<String>("")
        private set
    fun onCarrefouBonusSurCartFidTextValue(text: String) {
        carrefourBonusSurCartFidTextValue = text
    }

    var azizaPriceTextValue by mutableStateOf<String>("")
        private set
    fun onAzizaPriceTextValue(text: String) {
        azizaPriceTextValue = text
    }

    var azizaPromotionTextValue by mutableStateOf<String>("")
        private set
    fun onAzizaPromotionTextValue(text: String) {
        azizaPromotionTextValue = text
    }

    var azizaBonusSurCartFidTextValue by mutableStateOf<String>("")
        private set
    fun onAzizaBonusSurCartFidTextValue(text: String) {
        azizaBonusSurCartFidTextValue = text
    }

    var geantPriceTextValue by mutableStateOf<String>("")
        private set
    fun onGeantPriceTextValue(text: String) {
        geantPriceTextValue = text
    }

    var geantPromotionTextValue by mutableStateOf<String>("")
        private set
    fun onGeantPromotionTextValue(text: String) {
        geantPromotionTextValue = text
    }

    var geantBonusSurCartFidTextValue by mutableStateOf<String>("")
        private set
    fun onGeantBonusSurCartFidTextValue(text: String) {
        geantBonusSurCartFidTextValue = text
    }



    var putproductRemarqueInfoValue by mutableStateOf<String>("")
        private set
    fun onPutproductRemarqueInfoValue(magasin:String,promotion:String,bonusSurCartFid:String) {
        magasinValue = magasin
        putproductRemarqueInfoValue = promotion
        putproductRemarqueInfoValue = bonusSurCartFid
    }

}
