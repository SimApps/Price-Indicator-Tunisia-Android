package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.repository

import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import kotlinx.coroutines.flow.Flow

typealias UpdateListProductNameResponse = Response<Boolean>
interface ProductsNamesRemoteRepository {
    fun getProductsNamesBareCodeFromFirestore(): Flow<Response<Map<String, String>>>


    suspend   fun updateListProductsNamesToFirestore(prodNameList : Map<String, String>): UpdateListProductNameResponse

}