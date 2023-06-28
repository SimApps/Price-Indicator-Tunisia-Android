package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.CartFidRepository



class CreateUserFidCardDocument(
    private val repo: CartFidRepository
) {
    suspend operator fun invoke(
        docID:String,
    ) = repo.addUserFidCardDocument(docID)
}