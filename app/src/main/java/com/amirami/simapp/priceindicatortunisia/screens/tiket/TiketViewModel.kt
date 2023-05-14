package com.amirami.simapp.priceindicatortunisia.screens.tiket

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class TiketViewModel @Inject constructor() : ViewModel() {

    var text by mutableStateOf("")
        private set

    fun ontext(inputtext: String) {
        text = inputtext
    }
}