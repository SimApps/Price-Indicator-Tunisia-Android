package com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.repository

import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName
import kotlinx.coroutines.flow.Flow

interface NameListRepository {
    fun getProdNamesFromRoom(): Flow<List<ProductName>>

    fun getProdNameFromRoom(id: Int): Flow<ProductName>

    fun addProdNamesToRoom(productName: ProductName)
    fun addAllNames(productName: List<ProductName>)

    fun updateProdNameInRoom(name: String, updatedName: String)

    fun deleteProdNameFromRoom(codeBare: String)

    fun  deleteAllProdNameFromRoom()

}