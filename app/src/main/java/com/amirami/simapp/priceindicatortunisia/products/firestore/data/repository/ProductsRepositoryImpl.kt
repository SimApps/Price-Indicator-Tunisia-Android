package com.amirami.simapp.priceindicatortunisia.products.firestore.data.repository

import com.amirami.simapp.priceindicatortunisia.core.Constants.ID
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.*
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.AddProductResponse
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.DeleteProductResponse
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Source
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProductsRepositoryImpl @Inject constructor(
    @Named("prod")   private val collectionRef: CollectionReference
): ProductsRepository {

     override fun getProductFromFirestore(searchtype:String, searchtext:String) = callbackFlow {
          val source: Source = Source.CACHE

         trySend(Loading).isSuccess

          val snapshotListener = collectionRef. whereEqualTo(searchtype,
            if(searchtype ==ID) Functions.removeLeadingZeroes(searchtext) else searchtext
        )  /*.orderBy(TITLE)*/.addSnapshotListener { snapshot, e ->
              try {
                  val response = if (snapshot != null && !snapshot.isEmpty) {
                      Success(snapshot.toObjects(ProductModel::class.java))
                  } else {
                      Failure(searchtext +if(searchtype ==ID)"  Introuvable!" else " Introuvable")

                  }
                  trySend(response).isSuccess
              } catch (e: Exception) {
                  Failure(e.message ?: e.toString())
              }

        }

        awaitClose {
            snapshotListener.remove()
        }


    }

    override suspend fun addProductToFirestore(product: ProductModel, id: String): AddProductResponse = try {

        collectionRef.document(id).set(product).await()
        Success(true)
    } catch (e: Exception) {
        Failure(e.message?:"Erreur add Product")
    }






    override suspend fun deleteProductFromFirestore(id: String) : DeleteProductResponse = try {
        collectionRef.document(id).delete().await()
        Success(true)
    } catch (e: Exception) {
        Failure(e.message?:"Erreur Delete Product")
    }
}