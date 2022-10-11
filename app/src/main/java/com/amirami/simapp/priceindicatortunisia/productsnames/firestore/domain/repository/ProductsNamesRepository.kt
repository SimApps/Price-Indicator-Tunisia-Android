package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository

import kotlinx.coroutines.flow.Flow
import com.amirami.simapp.priceindicatortunisia.domain.model.Response

interface ProductsNamesRepository {
    fun getProductsNamesFromFirestore(): Flow<Response<ArrayList<String>>>

    fun addProductNameToFirestore(title: String, author: String): Flow<Response<Void?>>

    fun addProductsNamesToFirestore(title: String, author: String): Flow<Response<Void?>>

    fun deleteBookFromFirestore(bookId: String): Flow<Response<Void?>>
}