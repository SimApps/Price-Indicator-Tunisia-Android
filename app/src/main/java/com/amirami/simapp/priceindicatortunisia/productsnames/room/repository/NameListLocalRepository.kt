package com.amirami.simapp.priceindicatortunisia.productsnames.room.repository

import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.ProductName
import kotlinx.coroutines.flow.Flow

interface NameListLocalRepository {
    fun getProdNamesFromRoom(): Flow<List<ProductName>>

    fun getProdNameFromRoom(id: Int): Flow<ProductName>

    fun addProdNamesToRoom(productName: ProductName)
    fun addAllNames(productName: List<ProductName>)

    fun updateProdNameInRoom(name: String, updatedName: String)

    fun deleteProdNameFromRoom(codeBare: String)

    fun  deleteAllProdNameFromRoom()

}