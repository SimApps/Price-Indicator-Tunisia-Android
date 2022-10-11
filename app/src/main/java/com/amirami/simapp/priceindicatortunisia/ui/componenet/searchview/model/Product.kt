package com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.model

import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.utils.AutoCompleteEntity
import java.util.*


data class Product(
    val name: String,
//    val phoneNumber: String,
 //   val email: String
) : AutoCompleteEntity {
    override fun filter(query: String): Boolean {
        return name.toLowerCase(Locale.getDefault()).startsWith(query.toLowerCase(Locale.getDefault()))
    }
}



