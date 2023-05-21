package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.data.repository

import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_ARRAYS
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_COLLECTION
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_DOCUMENT
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.AddListProductNameResponse
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.AddProductNameResponse
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.DeleteProductNameResponse
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ERREUR_CONNECTION
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsNamesRepositoryImpl @Inject constructor(
    private val collectionRef: CollectionReference): ProductsNamesRepository {
    override fun getProductsNamesFromFirestore() = callbackFlow {
        trySend(Loading).isSuccess
        val snapshotListener = collectionRef.document(PRODUCTS_LIST_NAMES_DOCUMENT) /*.orderBy(TITLE)*/.addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                if(snapshot.exists()) {
                    Failure("")
                    Success(snapshot.get(Constants.PRODUCTS_LIST_NAMES_ARRAYS) as ArrayList<String>)
                }
                else  Failure(ERREUR_CONNECTION)
            } else Failure(e?.message ?: e.toString())

            trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addProductNameToFirestore(prodName:String) : AddProductNameResponse = try {
            collectionRef.document(PRODUCTS_LIST_NAMES_COLLECTION).update(PRODUCTS_LIST_NAMES_ARRAYS, FieldValue.arrayUnion(prodName)).await()
            Success(true)
        } catch (e: Exception) {
            Failure(e.message?:"Erreur Add Product Name")
        }


    override suspend fun addListProductsNamesToFirestore(prodNameList : ArrayList<String>): AddListProductNameResponse = try{
        collectionRef.document(PRODUCTS_LIST_NAMES_COLLECTION).update(PRODUCTS_LIST_NAMES_ARRAYS, prodNameList).await()
        Success(true)
    }catch (e: Exception) {
        Failure(e.message?:"Erreur Add List Product Name")
    }

    override suspend fun deleteProductNameFromFirestore(name: String): DeleteProductNameResponse = try {
        collectionRef.document(PRODUCTS_LIST_NAMES_COLLECTION).update(PRODUCTS_LIST_NAMES_ARRAYS, FieldValue.arrayRemove(name)).await()
        Success(true)
    } catch (e: Exception) {
        Failure(e.message?:"Erreur Delete Product Name")
    }

}