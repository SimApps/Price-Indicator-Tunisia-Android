package com.amirami.simapp.priceindicatortunisia.products.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.repository.ProductsRepository
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel


class AddProduct(
    private val repo: ProductsRepository
) {
    suspend operator fun invoke(
        product: ProductModel,
        id: String
    ) = repo.addProductToFirestore(product, id)
}