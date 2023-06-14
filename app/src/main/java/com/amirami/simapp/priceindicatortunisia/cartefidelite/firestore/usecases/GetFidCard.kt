package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.CartFidRepository


class GetFidCard (
    private val repo: CartFidRepository
) {
    operator fun invoke(docID:String) = repo.getFidCardsFromFirestore(docID)
}