package com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel


class ProductDetailDialogViewModel(): ViewModel() {

    var prodDetailDialogVisibilityStates by mutableStateOf(false)
    fun onprodDetailDialogVisibilityStatesChanged(dialogvisibilityState: Boolean) {
        prodDetailDialogVisibilityStates = dialogvisibilityState
    }


    var prodDetailDialogStates by mutableStateOf(ProductModel())
    fun onprodDetailDialogStatesChanged(product: ProductModel) {
        prodDetailDialogStates = product
    }


    var prodDetailDialogInshopingStates by mutableStateOf(ProductModel())
    fun onprodDetailDialogInshopingStatesChanged(product: ProductModel) {
        prodDetailDialogInshopingStates = product
    }


    var toggleBtnSelectedStates by mutableStateOf("Prix")
    fun ontoggleBtnSelectedStatesStatesChanged(toggleBtnSelected: String) {
        toggleBtnSelectedStates = toggleBtnSelected
    }
}