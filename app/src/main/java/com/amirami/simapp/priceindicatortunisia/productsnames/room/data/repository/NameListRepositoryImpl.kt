package com.amirami.simapp.priceindicatortunisia.productsnames.room.data.repository

import com.amirami.simapp.priceindicatortunisia.productsnames.room.data.dao.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.repository.NameListRepository

class NameListRepositoryImpl(
    private val prodNamesDAO: ProdNamesDAO
) : NameListRepository {
    override fun getProdNamesFromRoom() = prodNamesDAO.getProdNames()

    override fun getProdNameFromRoom(id: Int) = prodNamesDAO.getName(id)

    override fun addProdNamesToRoom(productName: ProductName) = prodNamesDAO.addName(productName)
    override fun addAllNames(productName: List<ProductName>) = prodNamesDAO.addAllNames(productName)

    override fun updateProdNameInRoom(name: String, updatedName: String) = prodNamesDAO.updateName(name = name, updatedName = updatedName)

    override fun deleteProdNameFromRoom(codeBare: String) = prodNamesDAO.deleteProdName(codeBare)

    override fun deleteAllProdNameFromRoom() = prodNamesDAO.deleteAll()

}