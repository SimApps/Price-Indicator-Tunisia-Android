package com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel

class ProductDetailDialogViewModel() : ViewModel(), Parcelable {

    var prodDetailDialogVisibilityStates by mutableStateOf(false)
    fun onprodDetailDialogVisibilityStatesChanged(dialogvisibilityState: Boolean) {
        prodDetailDialogVisibilityStates = dialogvisibilityState
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

    companion object CREATOR : Parcelable.Creator<ProductDetailDialogViewModel> {
        override fun createFromParcel(parcel: Parcel): ProductDetailDialogViewModel {
            return ProductDetailDialogViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductDetailDialogViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
