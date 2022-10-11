package com.amirami.simapp.priceindicatortunisia.productsnames.room.data.repository

import com.amirami.simapp.priceindicatortunisia.productsnames.room.data.network.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.repository.BookRepository

class BookRepositoryImpl(
    private val prodNamesDAO: ProdNamesDAO
) : BookRepository {
    override fun getProdNamesFromRoom() = prodNamesDAO.getProdNames()

    override fun getProdNameFromRoom(id: Int) = prodNamesDAO.getBook(id)

    override fun addProdNamesToRoom(productName: ProductName) = prodNamesDAO.addBook(productName)

    override fun updateProdNameInRoom(productName: ProductName) = prodNamesDAO.updateBook(productName)

    override fun deleteProdNameFromRoom(productName: String) = prodNamesDAO.deleteProdName(productName)

    override fun deleteAllProdNameFromRoom() = prodNamesDAO.deleteAll()

}