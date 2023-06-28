package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository

import android.util.Log
import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
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
                        // !snapshot.metadata.isFromCache IS HERE TO DONT ALLOW FIRST EMPTY READ
                        if(snapshot.exists() && !snapshot.metadata.isFromCache) {
                            //  Failure("")


                            val responseSnapshot = snapshot.get(Constants.FID_CARD_MAP)

                            Response.Success(if (responseSnapshot != null) responseSnapshot as Map<String, String>
                            else emptyMap<String, String>())

                        }
                        else  Response.Failure(com.amirami.simapp.priceindicatortunisia.utils.Constants.ERREUR_CONNECTION)
                    } else Response.Failure(e?.message ?: e.toString())

                    trySend(response).isSuccess

                }

            awaitClose {
                snapshotListener.remove()
            }
        }



        override suspend fun updateListFidCardToFirestore(docID:String, fidCardList: Map<String, String>): UpdateListFidCardResponse  {
            var updateListFidCardResponse : UpdateListFidCardResponse = Response.Loading
            try{
                collectionRef.document(docID)/*.set(fidCardList, SetOptions.merge())*/.update(
                Constants.FID_CARD_MAP, fidCardList )
                    .addOnSuccessListener {
                        Log.d("okpl", "KKKK User Document Carte Fid")
                        // Document creation success
                        updateListFidCardResponse =      Response.Success(true)
                    }
                    .addOnFailureListener { e ->
                        Log.d("okpl", e.message+"Add User Document Carte Fid")
                        // Handle any errors that occur during document creation
                        updateListFidCardResponse =    Response.Failure(e.message ?: "Erreur Update List Carte Fid")
                    }
                    .await()

                //  Response.Success(true)
            }
            catch (e: Exception) {
                Log.d("okpl",e.message ?: "Erreur update List Carte Fid")
                updateListFidCardResponse = Response.Failure(e.message ?: "Erreur update List Carte Fid")
            }


            return updateListFidCardResponse
        }

        override suspend fun addUserFidCardDocument(docID: String): AddUserDocumentResponse = try{
            collectionRef.document(docID).set(emptyMap<String,String>(), SetOptions.merge()).await()
            Log.d("okpl", "Add User Document Carte Fid")
             Response.Success(true)
        }
        catch (e: Exception) {
            Log.d("okpl",e.message ?: "Erreur Add User Document Carte Fid")
             Response.Failure(e.message ?: "Erreur Add User Document Carte Fid")
        }

        override suspend fun deleteFidCardUserDocument(docID:String): DeleteFidCardUserDocumentResponse = try {
            collectionRef.document(docID).delete().await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "Erreur Delete Carte Fid")
        }
    }