package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case

import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository



class AddListProductsNames(
    private val repo: ProductsNamesRepository
) {
    suspend operator fun invoke(
        prodNameList : ArrayList<String>
    ) = repo.addListProductsNamesToFirestore(prodNameList)
}