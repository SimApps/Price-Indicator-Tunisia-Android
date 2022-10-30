package com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel

class ProductTypesDialogViewModel() : ViewModel(), Parcelable {

    var prodTypesDialogVisibilityStates by mutableStateOf(false)
    fun onprodTypesDialogVisibilityStatesChanged(dialogvisibilityState: Boolean) {
        prodTypesDialogVisibilityStates = dialogvisibilityState
    }

    var prodTypesDialogTitleStates by mutableStateOf("")
    var prodTypesDialogContentStates by mutableStateOf("")
    // var prodTypesDialogTitleStates by mutableStateOf(ProductModel())

    fun onprodTypesDialogStatesChanged(title: String, content : String) {
        prodTypesDialogTitleStates = title
        prodTypesDialogContentStates = content
    }

    var prodDetailDialogInshopingStates by mutableStateOf(ProductModel())
    fun onprodDetailDialogInshopingStatesChanged(product: ProductModel) {
        prodDetailDialogInshopingStates = product
    }

    var toggleBtnSelectedStates by mutableStateOf("Prix")

    constructor(parcel: Parcel) : this() {
    }

    fun ontoggleBtnSelectedStatesStatesChanged(toggleBtnSelected: String) {
        toggleBtnSelectedStates = toggleBtnSelected
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductTypesDialogViewModel> {
        override fun createFromParcel(parcel: Parcel): ProductTypesDialogViewModel {
            return ProductTypesDialogViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductTypesDialogViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
