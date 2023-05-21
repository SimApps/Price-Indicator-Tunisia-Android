package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository


class GetProduct (
    private val repo: ProductsRepository
) {
       operator  fun invoke(searchtype:String,searchtext:String) = repo.getProductFromFirestore(searchtype,searchtext)
}