package com.amirami.simapp.priceindicatortunisia.productsnames.room.repository

import com.amirami.simapp.priceindicatortunisia.productsnames.room.dao.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.ProductName

class NameListLocalRepositoryImpl(
    private val prodNamesDAO: ProdNamesDAO
) : NameListLocalRepository {
    override fun getProdNamesFromRoom() = prodNamesDAO.getProdNames()

    override fun getProdNameFromRoom(id: Int) = prodNamesDAO.getName(id)

    override fun addProdNamesToRoom(productName: ProductName) = prodNamesDAO.addName(productName)
    override fun addAllNames(productName: List<ProductName>) = prodNamesDAO.addAllNames(productName)

    override fun updateProdNameInRoom(id: String, updatedName: String) = prodNamesDAO.updateName(id = id, updatedName = updatedName)

    override fun deleteProdNameFromRoom(codeBare: String) = prodNamesDAO.deleteProdName(codeBare)

    override fun deleteAllProdNameFromRoom() = prodNamesDAO.deleteAll()

}