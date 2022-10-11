package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.use_case

import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository


class AddProduct(
    private val repo: ProductsRepository
) {
    suspend operator fun invoke(
        title: String,
        author: String
    ) = repo.addProductToFirestore(title, author)
}