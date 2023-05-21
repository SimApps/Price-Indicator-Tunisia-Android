package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository

import kotlinx.coroutines.flow.Flow

typealias AddProductNameResponse = Response<Boolean>
typealias AddListProductNameResponse = Response<Boolean>
typealias DeleteProductNameResponse = Response<Boolean>
interface ProductsNamesRepository {
    fun getProductsNamesFromFirestore(): Flow<Response<ArrayList<String>>>

    suspend  fun addProductNameToFirestore(prodName : String): AddProductNameResponse

    suspend   fun addListProductsNamesToFirestore(prodNameList : ArrayList<String>): AddListProductNameResponse

    suspend fun deleteProductNameFromFirestore(bookId: String): DeleteProductNameResponse
}