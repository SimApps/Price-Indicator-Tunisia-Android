package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.use_case

import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.repository.ProductsNamesRemoteRepository

class DeleteProductName(
    private val repo: ProductsNamesRemoteRepository
) {
    suspend operator fun invoke(name: String) = repo.deleteProductNameFromFirestore(name)
}