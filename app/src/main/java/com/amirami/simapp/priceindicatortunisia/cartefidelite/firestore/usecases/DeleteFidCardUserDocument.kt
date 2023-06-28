package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.CartFidRepository

class DeleteFidCardUserDocument(
    private val repo: CartFidRepository
) {
    suspend operator fun invoke(
        docID:String
    ) = repo.deleteFidCardUserDocument(
        docID = docID
    )
}