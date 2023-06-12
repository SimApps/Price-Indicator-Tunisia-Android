package com.amirami.simapp.priceindicatortunisia.products.room.data.repository

import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.products.room.data.dao.ProdShopListDAO
import com.amirami.simapp.priceindicatortunisia.products.room.domain.repository.ShopListRepository
import kotlinx.coroutines.flow.Flow

class ShopListRepositoryImpl(
    private val prodShopListDAO: ProdShopListDAO
) : ShopListRepository {

    override fun getShopListFromRoom(): Flow<List<ProductModel>> = prodShopListDAO.getShopList()

    override fun getProdShopFromRoom(id: Int): Flow<ProductModel> = prodShopListDAO.getItemShopList(id)

    override fun addShopProdToRoom(productName: ProductModel) = prodShopListDAO.addProduct(productName)
    override fun addAllProdToRoom(productName: List<ProductModel>) = prodShopListDAO.addAllProduct(productName)

    override fun updateShopListInRoom(productName: ProductModel) = prodShopListDAO.updateShopList(productName)

    override fun updateProdQuantityInRoom(quantity: Double?, id: String?) = prodShopListDAO.updateQuantity(quantity, id)

    override fun deleteProdShopListFromRoom(productid: String) = prodShopListDAO.deleteShopingProduct(productid)

    override fun deleteAllShopListFromRoom() = prodShopListDAO.deleteAll()
}
