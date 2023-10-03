package com.amirami.simapp.priceindicatortunisia.products.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.repository.ProductsRepository


class GetAllProduct (
    private val repo: ProductsRepository
) {
    operator  fun invoke() = repo.getAllProductFromFirestore()
}