package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository

import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
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
    class CartFidRepositoryImpl @Inject constructor(
        @Named("Fid_Card")  private val collectionRef: CollectionReference
    ): CartFidRepository {
        override fun getFidCardsFromFirestore(docID:String):Flow<Response<Map<String, String>>> = callbackFlow {
            trySend(Response.Loading).isSuccess
            val snapshotListener = collectionRef.document(docID)
                /*.orderBy(TITLE)*/.addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        if(snapshot.exists()) {
                            //  Failure("")
                            Response.Success(snapshot.get(Constants.FID_CARD_MAP) as Map<String, String>)
                        }
                        else  Response.Failure(com.amirami.simapp.priceindicatortunisia.utils.Constants.ERREUR_CONNECTION)
                    } else Response.Failure(e?.message ?: e.toString())

                    trySend(response).isSuccess
                }

            awaitClose {
                snapshotListener.remove()
            }
        }



        override suspend fun addListFidCardToFirestore(docID:String,fidCardList: Map<String, String>): AddListFidCardResponse  = try{
            collectionRef.document(docID).update(
                Constants.FID_CARD_MAP, fidCardList).await()
            Response.Success(true)
        }catch (e: Exception) {
                Response.Failure(e.message ?: "Erreur Add List Carte Fid")
        }

        override suspend fun deleteFidCardFromFirestore(docID:String, bareCode: String): DeleteFidCardResponse = try {
            collectionRef.document(docID).update(Constants.FID_CARD_MAP, FieldValue.arrayRemove(bareCode)).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "Erreur Delete Carte Fid")
        }
    }