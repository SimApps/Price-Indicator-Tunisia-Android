package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.use_case

import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository


class GetProduct (
    private val repo: ProductsRepository
) {
    suspend   operator  fun invoke(searchtype:String,searchtext:String) = repo.getProductFromFirestore(searchtype,searchtext)
}