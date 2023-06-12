package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case

import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository



class AddListProductsNamesBarCode(
    private val repo: ProductsNamesRepository
) {
    suspend operator fun invoke(
        prodNameList : Map<String,String>
    ) = repo.addListProductsNamesBareCodeToFirestore(prodNameList)
}