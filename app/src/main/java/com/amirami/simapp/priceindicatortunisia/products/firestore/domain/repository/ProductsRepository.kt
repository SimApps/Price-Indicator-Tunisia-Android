package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository

import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProductFromFirestore(searchtype: String, searchtext: String): Flow<Response<MutableList<ProductModel>>>

    fun addProductToFirestore(title: String, author: String): Flow<Response<Void?>>

    fun addProductsToFirestore(title: String, author: String): Flow<Response<Void?>>

    fun deleteProductFromFirestore(bookId: String): Flow<Response<Void?>>
}
