package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.CartFidRepository

class DeleteFidCard(
    private val repo: CartFidRepository
) {
    suspend operator fun invoke(
        docID:String,
        bareCode: String
    ) = repo.deleteFidCardFromFirestore(
        docID = docID,
        bareCode = bareCode
    )
}