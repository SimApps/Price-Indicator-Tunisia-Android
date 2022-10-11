package com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.repository

import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getProdNamesFromRoom(): Flow<List<ProductName>>

    fun getProdNameFromRoom(id: Int): Flow<ProductName>

    fun addProdNamesToRoom(productName: ProductName)

    fun updateProdNameInRoom(productName: ProductName)

    fun deleteProdNameFromRoom(productName: String)

    fun  deleteAllProdNameFromRoom()

}