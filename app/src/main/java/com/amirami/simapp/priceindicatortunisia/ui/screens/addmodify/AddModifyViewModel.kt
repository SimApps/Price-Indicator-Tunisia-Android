package com.amirami.simapp.priceindicatortunisia.ui.screens.addmodify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddModifyViewModel @Inject constructor(
) : ViewModel() {

    var editTextValue by mutableStateOf<String>("")
        private set

    fun onEditTextValue(text: String) {
        editTextValue = text

    }





    var editTextValueX by mutableStateOf<String>("")
        private set

    fun onEditTextValueX(text: String) {
        editTextValueX = text

    }
}