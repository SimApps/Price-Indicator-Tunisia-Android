package com.amirami.simapp.priceindicatortunisia.products.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.repository.ProductsRepository


class GetProduct (
    private val repo: ProductsRepository
) {
       operator  fun invoke(searchtype:String,searchtext:String) = repo.getProductFromFirestore(searchtype,searchtext)
}