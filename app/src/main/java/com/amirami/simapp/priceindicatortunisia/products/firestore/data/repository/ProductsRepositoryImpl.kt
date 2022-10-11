package com.amirami.simapp.priceindicatortunisia.products.firestore.data.repository

import com.amirami.simapp.priceindicatortunisia.core.Constants.ID
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.model.Book
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.*
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Source
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProductsRepositoryImpl @Inject constructor(
    @Named("prod")   private val booksRef: CollectionReference
): ProductsRepository {

     override fun getProductFromFirestore(searchtype:String, searchtext:String) = callbackFlow {
          val source: Source = Source.CACHE

         trySend(Loading).isSuccess

          val snapshotListener = booksRef . whereEqualTo(searchtype,
            if(searchtype ==ID) Functions.removeLeadingZeroes(searchtext) else searchtext
        )  /*.orderBy(TITLE)*/.addSnapshotListener { snapshot, e ->

            val response = if (snapshot != null && !snapshot.isEmpty) {
                Success(snapshot.toObjects(ProductModel::class.java))
            } else {
                Error(e?.message ?: e.toString()+ if(searchtype ==ID)" ADD PROD HERE here" else " what")
            }
            trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }


    }

    override fun addProductToFirestore(title: String, author: String) = flow {
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

    override fun addProductsToFirestore(title: String, author: String): Flow<Response<Void?>> {
        TODO("Not yet implemented")
    }

    override fun deleteProductFromFirestore(bookId: String) = flow {
        try {
            emit(Loading)
            val deletion = booksRef.document(bookId).delete().await()
            emit(Success(deletion))
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }
}