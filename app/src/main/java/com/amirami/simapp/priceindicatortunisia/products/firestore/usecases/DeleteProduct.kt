package com.amirami.simapp.priceindicatortunisia.products.firestore.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.repository.ProductsRepository

class DeleteProduct(
    private val repo: ProductsRepository
) {
    suspend operator fun invoke(id: String) = repo.deleteProductFromFirestore(id)
}
