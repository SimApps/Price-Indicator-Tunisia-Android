package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.usecases

import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository

class DeleteProduct(
    private val repo: ProductsRepository
) {
    suspend operator fun invoke(bookId: String) = repo.deleteProductFromFirestore(bookId)
}
