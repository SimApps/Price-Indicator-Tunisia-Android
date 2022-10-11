package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case

data class UseCasesProductName (
    val getProductsNames: GetProductsNames,
    val addProductName: AddProductName,
    val addProductsNames: AddProductsNames,
    val deleteProductName: DeleteProductName
)