package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.CartFidRepository



class AddListFidCard(
    private val repo: CartFidRepository
) {
    suspend operator fun invoke(
        docID:String,
        fidCardList : Map<String,String>
    ) = repo.addListFidCardToFirestore(docID,fidCardList)
}