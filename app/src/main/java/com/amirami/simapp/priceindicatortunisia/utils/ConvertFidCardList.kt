package com.amirami.simapp.priceindicatortunisia.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.amirami.simapp.priceindicatortunisia.fidcard.Barecode
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardEntity


object ConvertFidCardList {
    private fun toListModel(fidCardEntity: List<FidCardEntity>) : MutableList<Barecode> {
        val itemList : MutableList<Barecode> = mutableListOf<Barecode>()
        fidCardEntity.map {
            itemList.add(
                Barecode( it.id?:0L, it.name?:"",it.value?:"",it.barecodetype?:"")
            )
        }
        return itemList
    }

    fun toLiveDataListModel(localList : LiveData<List<FidCardEntity>>) : LiveData<MutableList<Barecode>> {
        return Transformations.map<
                List<FidCardEntity>, //localList Data Type
                MutableList<Barecode> //toListModel List Data Type
                >(
            localList) { listEntity ->
            toListModel(listEntity)
        }
    }

    fun toEntity(barecode: Barecode) : FidCardEntity {
        return when(barecode.value) {
            null -> {
                FidCardEntity(
                    barecode.id,
                    barecode.name,
                    barecode.value,
                    barecode.barecodetype
                )
            }
            else -> {
                FidCardEntity(
                    barecode.id,
                    barecode.name,
                    barecode.value,
                    barecode.barecodetype

                )
            }
        }
    }
}