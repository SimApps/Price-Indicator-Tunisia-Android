package com.amirami.simapp.priceindicatortunisia.products.firestore.domain.usecases

data class UseCasesProduct(
    val getProduct: GetProduct,
    val getAllProduct: GetAllProduct,
    val addProduct: AddProduct,
    val addProducts: AddProducts,
    val deleteProduct: DeleteProduct
)
