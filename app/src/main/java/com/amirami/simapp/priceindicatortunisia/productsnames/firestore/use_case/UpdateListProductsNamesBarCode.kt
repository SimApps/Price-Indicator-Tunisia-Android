package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.use_case

import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.repository.ProductsNamesRemoteRepository



class UpdateListProductsNamesBarCode(
    private val repo: ProductsNamesRemoteRepository
) {
    suspend operator fun invoke(
        prodNameList : Map<String,String>
    ) = repo.updateListProductsNamesToFirestore(prodNameList)
}