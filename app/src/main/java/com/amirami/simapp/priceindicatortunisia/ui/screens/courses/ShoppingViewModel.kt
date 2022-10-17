package com.amirami.simapp.priceindicatortunisia.ui.screens.courses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import javax.inject.Inject





class ShoppingViewModel @Inject constructor() : ViewModel() {

    var quantity by mutableStateOf(0.0)
        private set

    fun onquantityChange(inputtext: Double) {
        quantity = inputtext
    }
}