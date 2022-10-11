package com.amirami.simapp.priceindicatortunisia.products.room.domain.repository

import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ShopListRepository {
    fun getShopListFromRoom(): Flow<List<ProductModel>>

    fun getProdShopFromRoom(id: Int): Flow<ProductModel>

    fun addShopProdToRoom(productName: ProductModel)

    fun updateShopListInRoom(productName: ProductModel)

    fun updateProdQuantityInRoom(quantity: Double?, id: Long?)

    fun deleteProdShopListFromRoom(productid: String)

    fun deleteAllShopListFromRoom()
}
