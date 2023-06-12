package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case

import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository


class GetProductsNamesBareCode (
    private val repo: ProductsNamesRepository
) {
    operator fun invoke() = repo.getProductsNamesBareCodeFromFirestore()
}