package com.amirami.simapp.priceindicatortunisia.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.amirami.simapp.priceindicatortunisia.shopingfragment.ProductShopingRoom
import com.amirami.simapp.priceindicatortunisia.shopingfragment.ProdShopListEntity


object ConvertShopList {
    private fun toListModel(prodNamesEntity: List<ProdShopListEntity>): MutableList<ProductShopingRoom> {
        val itemList: MutableList<ProductShopingRoom> = mutableListOf<ProductShopingRoom>()
        prodNamesEntity.map {
            itemList.add(
                ProductShopingRoom(
                    it.productid ?: 0L,
                    it.date ?: "",
                    it.name ?: "",
                    it.namearabe ?: "",
                    it.marques ?: "",
                    it.marquesarabe ?: "",
                    it.quantity ?: 0.0,
                    it.description ?: "",
                    it.descriptionarabe ?: "",
                    it.imageurl ?: "",
                    it.type ?: "",
                    it.typesub ?: "",
                    it.typesubsub ?: "",
                    it.sieze ?: "",
                    it.monoprixprice ?: "",
                    it.monoprixremarq ?: "",
                    it.mgprice ?: "",
                    it.mgremarq ?: "",
                    it.carrefourprice ?: "",
                    it.carrefourremarq ?: "",
                    it.azzizaprice ?: "",
                    it.azzizaremarq ?: "",
                    it.geantprice ?: "",
                    it.geantremarq ?: "",


                    it.monoprixremarqmodifdate?: "",
                it.mgremarqmodifdate?: "",
            it.carrefourremarqmodifdate?: "",
            it.azzizaremarqmodifdate?: "",
            it.geantremarqmodifdate?: "",


                    it.monoprixmodifdate ?: "",
                    it.mgmodifdate ?: "",
                    it.carrefourmodifdate ?: "",
                    it.azzizamodifdate ?: "",
                    it.geantmodifdate ?: "",

                    it.monoprixbonusfid ?: "",
                    it.mgbonusfid ?: "",
                    it.carrefourbonusfid ?: "",
                    it.azzizabonusfid ?: "",
                    it.geantbonusfid ?: "",

                    it.monoprixbonusfidmodifdate?: "",
                    it.mgbonusfidmodifdate?: "",
                    it.carrefourbonusfidmodifdate?: "",
                    it.azzizabonusfidmodifdate?: "",
                    it.geantbonusfidmodifdate?: "",

                    it.monoprixPriceHistory ?: "",
                    it.mgpriceHistory ?: "",
                    it.azizaPriceHistory ?: "",
                    it.carrefourPriceHistory ?: "",
                    it.geantPriceHistory ?: ""
                )
            )
        }
        return itemList
    }

    fun toLiveDataListModel(localList: LiveData<List<ProdShopListEntity>>): LiveData<MutableList<ProductShopingRoom>> {
        return Transformations.map<
                List<ProdShopListEntity>, //localList Data Type
                MutableList<ProductShopingRoom> //toListModel List Data Type
                >(
            localList
        ) { listEntity ->
            toListModel(listEntity)
        }
    }

    fun toEntity(prodRoomNamesModel: ProductShopingRoom): ProdShopListEntity {
        return when (prodRoomNamesModel.productid) {
            null -> {
                ProdShopListEntity(
                    prodRoomNamesModel.productid ?: 0L,
                    prodRoomNamesModel.date,
                    prodRoomNamesModel.name,
                    prodRoomNamesModel.namearabe,
                    prodRoomNamesModel.marques,
                    prodRoomNamesModel.marquesarabe,
                    prodRoomNamesModel.quantity ?: 0.0,
                    prodRoomNamesModel.description,
                    prodRoomNamesModel.descriptionarabe,
                    prodRoomNamesModel.imageurl,
                    prodRoomNamesModel.type,
                    prodRoomNamesModel.typesub,
                    prodRoomNamesModel.typesubsub,
                    prodRoomNamesModel.sieze,
                    prodRoomNamesModel.monoprixprice,
                    prodRoomNamesModel.monoprixremarq,
                    prodRoomNamesModel.mgprice,
                    prodRoomNamesModel.mgremarq,
                    prodRoomNamesModel.carrefourprice,
                    prodRoomNamesModel.carrefourremarq,
                    prodRoomNamesModel.azzizaprice,
                    prodRoomNamesModel.azzizaremarq,
                    prodRoomNamesModel.geantprice,
                    prodRoomNamesModel.geantremarq,

                    prodRoomNamesModel.monoprixremarqmodifdate,
                    prodRoomNamesModel.mgremarqmodifdate,
                    prodRoomNamesModel.carrefourremarqmodifdate,
                    prodRoomNamesModel.azzizaremarqmodifdate,
                    prodRoomNamesModel.geantremarqmodifdate,

                    prodRoomNamesModel.monoprixmodifdate,
                    prodRoomNamesModel.mgmodifdate,
                    prodRoomNamesModel.carrefourmodifdate,
                    prodRoomNamesModel.azzizamodifdate,
                    prodRoomNamesModel.geantmodifdate,

                    prodRoomNamesModel.monoprixbonusfid,
                    prodRoomNamesModel.mgbonusfid,
                    prodRoomNamesModel.carrefourbonusfid,
                    prodRoomNamesModel.azzizabonusfid,
                    prodRoomNamesModel.geantbonusfid,


                    prodRoomNamesModel.monoprixbonusfidmodifdate,
                    prodRoomNamesModel.mgbonusfidmodifdate,
                    prodRoomNamesModel.carrefourbonusfidmodifdate,
                    prodRoomNamesModel.azzizabonusfidmodifdate,
                    prodRoomNamesModel.geantbonusfidmodifdate,

                    prodRoomNamesModel.monoprixPriceHistory,
                    prodRoomNamesModel.mgpriceHistory,
                    prodRoomNamesModel.azizaPriceHistory,
                    prodRoomNamesModel.carrefourPriceHistory,
                    prodRoomNamesModel.geantPriceHistory

                )
            }
            else -> {
                ProdShopListEntity(
                    prodRoomNamesModel.productid ?: 0L,
                    prodRoomNamesModel.date,
                    prodRoomNamesModel.name,
                    prodRoomNamesModel.namearabe,
                    prodRoomNamesModel.marques,
                    prodRoomNamesModel.marquesarabe,
                    prodRoomNamesModel.quantity ?: 0.0,
                    prodRoomNamesModel.description,
                    prodRoomNamesModel.descriptionarabe,
                    prodRoomNamesModel.imageurl,
                    prodRoomNamesModel.type,
                    prodRoomNamesModel.typesub,
                    prodRoomNamesModel.typesubsub,
                    prodRoomNamesModel.sieze,
                    prodRoomNamesModel.monoprixprice,
                    prodRoomNamesModel.monoprixremarq,
                    prodRoomNamesModel.mgprice,
                    prodRoomNamesModel.mgremarq,
                    prodRoomNamesModel.carrefourprice,
                    prodRoomNamesModel.carrefourremarq,
                    prodRoomNamesModel.azzizaprice,
                    prodRoomNamesModel.azzizaremarq,
                    prodRoomNamesModel.geantprice,
                    prodRoomNamesModel.geantremarq,

                    prodRoomNamesModel.monoprixremarqmodifdate,
                    prodRoomNamesModel.mgremarqmodifdate,
                    prodRoomNamesModel.carrefourremarqmodifdate,
                    prodRoomNamesModel.azzizaremarqmodifdate,
                    prodRoomNamesModel.geantremarqmodifdate,



                    prodRoomNamesModel.monoprixmodifdate,
                    prodRoomNamesModel.mgmodifdate,
                    prodRoomNamesModel.carrefourmodifdate,
                    prodRoomNamesModel.azzizamodifdate,
                    prodRoomNamesModel.geantmodifdate,

                    prodRoomNamesModel.monoprixbonusfid,
                    prodRoomNamesModel.mgbonusfid,
                    prodRoomNamesModel.carrefourbonusfid,
                    prodRoomNamesModel.azzizabonusfid,
                    prodRoomNamesModel.geantbonusfid,

                    prodRoomNamesModel.monoprixbonusfidmodifdate,
                    prodRoomNamesModel.mgbonusfidmodifdate,
                    prodRoomNamesModel.carrefourbonusfidmodifdate,
                    prodRoomNamesModel.azzizabonusfidmodifdate,
                    prodRoomNamesModel.geantbonusfidmodifdate,

                    prodRoomNamesModel.monoprixPriceHistory,
                    prodRoomNamesModel.mgpriceHistory,
                    prodRoomNamesModel.azizaPriceHistory,
                    prodRoomNamesModel.carrefourPriceHistory,
                    prodRoomNamesModel.geantPriceHistory
                )
            }
        }
    }
}
