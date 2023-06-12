package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository

import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import kotlinx.coroutines.flow.Flow

typealias AddProductNameResponse = Response<Boolean>
typealias AddListProductNameResponse = Response<Boolean>
typealias DeleteProductNameResponse = Response<Boolean>
interface ProductsNamesRepository {
    fun getProductsNamesBareCodeFromFirestore(): Flow<Response< Map<String, String>>>

    suspend  fun addProductNameToFirestore(prodName : String): AddProductNameResponse

    suspend   fun addListProductsNamesToFirestore(prodNameList : ArrayList<String>): AddListProductNameResponse
    suspend   fun addListProductsNamesBareCodeToFirestore(prodNameList : Map<String, String>): AddListProductNameResponse

    suspend fun deleteProductNameFromFirestore(bookId: String): DeleteProductNameResponse
}