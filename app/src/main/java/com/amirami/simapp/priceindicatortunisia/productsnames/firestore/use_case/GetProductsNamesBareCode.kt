package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.use_case

import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.repository.ProductsNamesRemoteRepository


class GetProductsNamesBareCode (
    private val repo: ProductsNamesRemoteRepository
) {
    operator fun invoke() = repo.getProductsNamesBareCodeFromFirestore()
}