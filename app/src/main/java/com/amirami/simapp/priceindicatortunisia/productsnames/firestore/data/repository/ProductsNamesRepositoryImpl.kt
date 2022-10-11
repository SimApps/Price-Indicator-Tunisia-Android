package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.model.Book
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.*
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository
import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_DOCUMENT
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ERREUR_CONNECTION
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsNamesRepositoryImpl @Inject constructor(
    private val booksRef: CollectionReference
): ProductsNamesRepository {
    override fun getProductsNamesFromFirestore() = callbackFlow {
        trySend(Loading).isSuccess
        val snapshotListener = booksRef.document(PRODUCTS_LIST_NAMES_DOCUMENT) /*.orderBy(TITLE)*/.addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                if(snapshot.exists()) {
                    Error("")
                    Success(snapshot.get(Constants.PRODUCTS_LIST_NAMES_ARRAYS) as ArrayList<String>)
                }
                else  Error(ERREUR_CONNECTION)
            } else Error(e?.message ?: e.toString())

            trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun addProductNameToFirestore(title: String, author: String) = flow {
        try {
            emit(Loading)
            val id = booksRef.document().id
            val book = Book(
                PRODUCTS_LIST_NAMES_ARRAYS=title
               // id = id,
               // title = title,
               // author = author
            )
            val addition = booksRef.document(id).set(book).await()
            emit(Success(addition))
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }

    override fun addProductsNamesToFirestore(title: String, author: String): Flow<Response<Void?>> {
        TODO("Not yet implemented")
    }

    override fun deleteBookFromFirestore(bookId: String) = flow {
        try {
            emit(Loading)
            val deletion = booksRef.document(bookId).delete().await()
            emit(Success(deletion))
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }
}