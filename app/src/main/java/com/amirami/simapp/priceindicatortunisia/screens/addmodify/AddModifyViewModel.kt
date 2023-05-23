package com.amirami.simapp.priceindicatortunisia.screens.addmodify

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

    var unitExpanded by mutableStateOf(false)
        private set
    fun onUnitExpandedChange(text: Boolean) {
        unitExpanded = text
    }


    var magasinValue by mutableStateOf("")
        private set
    fun onMagasinValue(text: String) {
        magasinValue = text
    }



    var unitselected by mutableStateOf("")
    private set
    fun onUnitselectedChange(text: String) {
        unitselected = text
    }

    var mesureUnitList by mutableStateOf<List<String>>(listOf())
        private set

    fun onmesureUnitListValue(text: List<String>) {
        mesureUnitList = text.toList()
    }

    init {
        viewModelScope.launch {
            delay(3000)
            onmesureUnitListValue(listOf("L", "ml", "cl", "Kg", "g", "m", "cm", "Rouleaux", "Pièces", "Oeufs", "watt"))
            //  names.value = listOf("Jordan", "Leonard")
        }
    }








    var putproductRemarqueInfoValue by mutableStateOf<String>("")
        private set
    fun onPutproductRemarqueInfoValue(magasin:String,promotion:String,bonusSurCartFid:String) {
        magasinValue = magasin
        putproductRemarqueInfoValue = promotion
        putproductRemarqueInfoValue = bonusSurCartFid
    }

}
