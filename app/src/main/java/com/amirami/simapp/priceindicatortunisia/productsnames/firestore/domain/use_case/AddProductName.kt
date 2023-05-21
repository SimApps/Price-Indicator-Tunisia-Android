package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case

import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository

class AddProductName(
    private val repo: ProductsNamesRepository
) {
    suspend operator fun invoke(
        prodName:String
    ) = repo.addProductNameToFirestore(prodName)
}