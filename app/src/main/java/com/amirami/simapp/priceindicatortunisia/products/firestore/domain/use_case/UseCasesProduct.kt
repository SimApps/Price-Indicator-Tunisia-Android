package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.use_case

data class UseCasesProduct(
    val getProduct: GetProduct,
    val addProduct: AddProduct,
    val addProducts: AddProducts,
    val deleteProduct: DeleteProduct
)
