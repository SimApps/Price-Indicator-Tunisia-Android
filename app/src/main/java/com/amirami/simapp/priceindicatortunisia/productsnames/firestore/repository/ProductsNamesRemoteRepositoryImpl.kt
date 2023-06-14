package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.repository

import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_ARRAYS
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_BARE_CODE_ARRAYS
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_BARE_CODE_DOCUMENT
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_DOCUMENT
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.Failure
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.Loading
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.Success
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ERREUR_CONNECTION
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProductsNamesRemoteRepositoryImpl @Inject constructor(
    @Named("Product_Name") private val collectionRef: CollectionReference): ProductsNamesRemoteRepository {


    override fun getProductsNamesBareCodeFromFirestore(): Flow<Response<Map<String, String>>> = callbackFlow {
        trySend(Loading).isSuccess
        val snapshotListener = collectionRef.document(PRODUCTS_LIST_NAMES_BARE_CODE_DOCUMENT)
            /*.orderBy(TITLE)*/.addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    if(snapshot.exists()) {
                      //  Failure("")
                        Success(snapshot.get(Constants.PRODUCTS_LIST_NAMES_BARE_CODE_ARRAYS) as Map<String, String>)
                    }
                    else  Failure(ERREUR_CONNECTION)
                } else Failure(e?.message ?: e.toString())

                trySend(response).isSuccess
            }

        awaitClose {
            snapshotListener.remove()
        }
    }






    override suspend fun addListProductsNamesToFirestore(prodNameList: Map<String, String>): AddListProductNameResponse = try{
        collectionRef.document(PRODUCTS_LIST_NAMES_BARE_CODE_DOCUMENT).update(PRODUCTS_LIST_NAMES_BARE_CODE_ARRAYS, prodNameList).await()
        Success(true)
    }catch (e: Exception) {
        Failure(e.message?:"Erreur Add List Product Name Barecode")
    }

    override suspend fun deleteProductNameFromFirestore(bareCode: String): DeleteProductNameResponse = try {
        collectionRef.document(PRODUCTS_LIST_NAMES_DOCUMENT).update(PRODUCTS_LIST_NAMES_ARRAYS, FieldValue.arrayRemove(bareCode)).await()
        Success(true)
    } catch (e: Exception) {
        Failure(e.message?:"Erreur Delete Product Name")
    }

}