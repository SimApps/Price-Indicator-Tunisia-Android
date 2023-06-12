package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository

import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import kotlinx.coroutines.flow.Flow

typealias Products = List<ProductModel>
typealias GetProductResponse = Response<Products>
typealias AddProductResponse = Response<Boolean>
typealias DeleteProductResponse = Response<Boolean>

interface ProductsRepository {
    fun getProductFromFirestore(searchtype: String, searchtext: String): Flow<GetProductResponse>
    fun getAllProductFromFirestore(): Flow<GetProductResponse>

    suspend fun addProductToFirestore(product: ProductModel, id: String): AddProductResponse


    suspend fun deleteProductFromFirestore(id: String): DeleteProductResponse
}
