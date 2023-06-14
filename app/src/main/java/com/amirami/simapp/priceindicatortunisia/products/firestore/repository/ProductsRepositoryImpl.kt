package com.amirami.simapp.priceindicatortunisia.products.firestore.repository

import com.amirami.simapp.priceindicatortunisia.core.Constants.ID
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.*
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProductsRepositoryImpl @Inject constructor(
    @Named("prod")   private val collectionRef: CollectionReference
): ProductsRepository {


     override fun getProductFromFirestore(searchtype:String, searchtext:String)= callbackFlow {
          val source: Source = Source.CACHE

        // trySend(Loading)//.isSuccess
        val value =  if(searchtype ==ID) Functions.removeLeadingZeroes(searchtext) else searchtext

         val snapshotListener = collectionRef. whereEqualTo(searchtype, value)/*.orderBy(TITLE)*/
              .addSnapshotListener { snapshot, e ->
              try {
                  val response = if (snapshot != null && !snapshot.isEmpty) {
                      Success(snapshot.toObjects(ProductModel::class.java))
                  } else {
                      Failure(searchtext +if(searchtype ==ID)" Code Barre Introuvable!" else " Produit Introuvable")

                  }
                  trySend(response)//.isSuccess
              } catch (e: Exception) {
                  Failure(e.message ?: e.toString())
              }

        }

        awaitClose {
            snapshotListener.remove()
        }


    }

    override fun getAllProductFromFirestore(): Flow<GetProductResponse> = callbackFlow {
        val source: Source = Source.CACHE

        // trySend(Loading)//.isSuccess
        /*   try {
                    response.data = productsRef.get().await().documents.mapNotNull { snapShot ->
                        snapShot.toObject(Product::class.java)
                    }
                } catch (exception: Exception) {
                    response.e = exception.toString()
                }*/
           collectionRef.get().addOnSuccessListener { querySnapshot ->
            // Documents retrieved successfully
            for (document in querySnapshot.documents) {
                val documentId = document.id
                val data = document.data // HashMap representation of the document's data
                // Handle each document as needed


            }
            trySend(Success(querySnapshot.toObjects(ProductModel::class.java)))

        }
            .addOnFailureListener { e ->
                // Handle error
                trySend(Failure(e.message ?: e.toString()))
            }



      /*  val snapshotListener = collectionRef
            .addSnapshotListener { snapshot, e ->
                try {
                    val response = if (snapshot != null && !snapshot.isEmpty) {
                        Success(snapshot.toObjects(ProductModel::class.java))
                    } else {
                        Failure(e?.message.toString()+" Introuvable")

                    }
                    trySend(response)//.isSuccess
                } catch (e: Exception) {
                    Failure(e.message ?: e.toString())
                }

            }
             awaitClose {
            snapshotListener.remove()
        }
        */

        awaitClose {

        }


    }


    override suspend fun addProductToFirestore(product: ProductModel, id: String): AddProductResponse = try {
        collectionRef.document(id).set(product).await()
        Success(true)
    } catch (e: FirebaseFirestoreException) {
        Failure(e.message?:"Erreur add Product")
    }






    override suspend fun deleteProductFromFirestore(id: String) : DeleteProductResponse = try {
        collectionRef.document(id).delete().await()
        Success(true)
    } catch (e: Exception) {
        Failure(e.message?:"Erreur Delete Product")
    }
}