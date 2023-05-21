package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel


class AddProducts(
    private val repo: ProductsRepository
) {
    suspend operator fun invoke(
        product: ProductModel,
        id: String
    ) = repo.addProductToFirestore(product, id)
}