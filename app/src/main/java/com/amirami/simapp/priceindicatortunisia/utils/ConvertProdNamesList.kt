package com.amirami.simapp.priceindicatortunisia.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.amirami.simapp.priceindicatortunisia.model.ProdRoomNamesModel
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesEntity

object ConvertProdNamesList {
    private fun toListModel(prodNamesEntity: List<ProdNamesEntity>) : MutableList<ProdRoomNamesModel> {
        val itemList : MutableList<ProdRoomNamesModel> = mutableListOf<ProdRoomNamesModel>()
        prodNamesEntity.map {
            itemList.add(
                ProdRoomNamesModel(it.id?:0, it.name?:"")
            )
        }
        return itemList
    }

    fun toLiveDataListModel(localList : LiveData<List<ProdNamesEntity>>) : LiveData<MutableList<ProdRoomNamesModel>> {
        return Transformations.map<
                List<ProdNamesEntity>, //localList Data Type
                MutableList<ProdRoomNamesModel> //toListModel List Data Type
                >(
            localList) { listEntity ->
            toListModel(listEntity)
        }
    }

    fun toEntity(prodRoomNamesModel: ProdRoomNamesModel) : ProdNamesEntity {
        return when(prodRoomNamesModel.id) {
            null -> {
                ProdNamesEntity(
                    prodRoomNamesModel.name?:""
                )
            }
            else -> {
                ProdNamesEntity(
                    prodRoomNamesModel.id!!,
                    prodRoomNamesModel.name?:""
                )
            }
        }
    }
}