package com.amirami.simapp.priceindicatortunisia.products.firestore.repository

import kotlinx.coroutines.flow.Flow
import com.amirami.simapp.priceindicatortunisia.domain.model.Response

interface ProductsNamesRepository {
    fun getBooksFromFirestore(): Flow<Response<ArrayList<String>>>

    fun addBookToFirestore(title: String, author: String): Flow<Response<Void?>>

    fun deleteBookFromFirestore(bookId: String): Flow<Response<Void?>>
}