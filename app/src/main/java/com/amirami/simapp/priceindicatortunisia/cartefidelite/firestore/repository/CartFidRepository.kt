package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository

import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import kotlinx.coroutines.flow.Flow


typealias AddListFidCardResponse = Response<Boolean>
typealias DeleteFidCardResponse = Response<Boolean>
interface CartFidRepository {
    fun getFidCardsFromFirestore(docID:String): Flow<Response<Map<String, String>>>


    suspend   fun addListFidCardToFirestore(docID:String,fidCardList : Map<String, String>): AddListFidCardResponse

    suspend fun deleteFidCardFromFirestore(docID:String, bareCode: String): DeleteFidCardResponse
}