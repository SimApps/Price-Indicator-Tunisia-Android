package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository

import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import kotlinx.coroutines.flow.Flow


typealias UpdateListFidCardResponse = Response<Boolean>
typealias AddUserDocumentResponse = Response<Boolean>
typealias DeleteFidCardUserDocumentResponse = Response<Boolean>
interface CartFidRepository {
    fun getFidCardsFromFirestore(docID:String): Flow<Response<Map<String, String>>>


    suspend   fun updateListFidCardToFirestore(docID:String, fidCardList : Map<String, String>): UpdateListFidCardResponse
    suspend   fun addUserFidCardDocument(docID:String): AddUserDocumentResponse

    suspend fun deleteFidCardUserDocument(docID:String): DeleteFidCardUserDocumentResponse
}