package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.repository

import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import kotlinx.coroutines.flow.Flow

typealias AddListProductNameResponse = Response<Boolean>
typealias DeleteProductNameResponse = Response<Boolean>
interface ProductsNamesRemoteRepository {
    fun getProductsNamesBareCodeFromFirestore(): Flow<Response<Map<String, String>>>


    suspend   fun addListProductsNamesToFirestore(prodNameList : Map<String, String>): AddListProductNameResponse

    suspend fun deleteProductNameFromFirestore(bareCode: String): DeleteProductNameResponse
}