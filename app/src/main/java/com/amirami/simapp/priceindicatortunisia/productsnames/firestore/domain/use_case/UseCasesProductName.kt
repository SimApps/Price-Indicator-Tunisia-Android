package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case

data class UseCasesProductName (
    val getProductsNamesBareCode: GetProductsNamesBareCode,
    val addProductName: AddProductName,
    val addListProductsNames: AddListProductsNames,
    val addListProductsNamesBareCode: AddListProductsNamesBarCode,
    val deleteProductName: DeleteProductName
)