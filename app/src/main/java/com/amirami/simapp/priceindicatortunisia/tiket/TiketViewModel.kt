package com.amirami.simapp.priceindicatortunisia.tiket

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class TiketViewModel @Inject constructor() : ViewModel() {

    var text by mutableStateOf("")
        private set

    fun ontext(inputtext: String) {
        text = inputtext
    }
}