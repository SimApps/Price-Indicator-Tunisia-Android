package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case

import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository

class DeleteProductName(
    private val repo: ProductsNamesRepository
) {
    suspend operator fun invoke(name: String) = repo.deleteProductNameFromFirestore(name)
}