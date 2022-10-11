package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.use_case

import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository

class DeleteProduct(
    private val repo: ProductsRepository
) {
    suspend operator fun invoke(bookId: String) = repo.deleteProductFromFirestore(bookId)
}
