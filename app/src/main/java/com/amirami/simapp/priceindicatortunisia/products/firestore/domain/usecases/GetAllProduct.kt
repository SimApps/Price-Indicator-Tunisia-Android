package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository


class GetAllProduct (
    private val repo: ProductsRepository
) {
    operator  fun invoke() = repo.getAllProductFromFirestore()
}