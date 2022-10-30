package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository


class AddProducts(
    private val repo: ProductsRepository
) {
    suspend operator fun invoke(
        title: String,
        author: String
    ) = repo.addProductsToFirestore(title, author)
}