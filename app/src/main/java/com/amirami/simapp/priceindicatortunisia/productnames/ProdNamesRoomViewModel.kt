package com.amirami.simapp.priceindicatortunisia.productnames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.model.ProdRoomNamesModel
import com.amirami.simapp.priceindicatortunisia.repository.ProdNameRoomBaseRepository
import com.amirami.simapp.priceindicatortunisia.utils.ConvertProdNamesList
import com.amirami.simapp.priceindicatortunisia.utils.Coroutines
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProdNamesRoomViewModel @Inject constructor(
    private val  prodNameRoomBaseRepository : ProdNameRoomBaseRepository
): ViewModel() {


    private val liveList : MutableLiveData<MutableList<ProdRoomNamesModel>> by lazy(LazyThreadSafetyMode.NONE, initializer = {
        MutableLiveData<MutableList<ProdRoomNamesModel>>()
    })
    private val liveUpdate : MutableLiveData<ProdRoomNamesModel> by lazy(LazyThreadSafetyMode.NONE, initializer = {
        MutableLiveData<ProdRoomNamesModel>()
    })



    fun getInstance() : String {
        return this.toString()
    }

    fun getRepositoryInstance() : String {
        return prodNameRoomBaseRepository.giveRepository()
    }

    @Deprecated("For Static Data")
    fun setItems() {
        Coroutines.default {
            prodNameRoomBaseRepository.deleteAll()
            for (index in 0 until 500) {
                prodNameRoomBaseRepository.insert(ConvertProdNamesList.toEntity(ProdRoomNamesModel(index,"name $index")))
            }

        }
    }

    fun setUpdate(item : ProdRoomNamesModel) {
        liveUpdate.value = item
    }

    fun getUpdate() : LiveData<ProdRoomNamesModel> {
        return liveUpdate
    }

    fun insertItem(item : ProdRoomNamesModel) {
        Coroutines.io(this@ProdNamesRoomViewModel) {
            prodNameRoomBaseRepository.insert(
                ConvertProdNamesList.toEntity(item)
            )
        }
    }

    fun updateItem() {
        liveUpdate.value?.let {
            Coroutines.io(this@ProdNamesRoomViewModel) {
                prodNameRoomBaseRepository.update(
                    ConvertProdNamesList.toEntity(it)
                )
            }
        }
    }

    fun deleteItem(item : String) {
        Coroutines.io(this@ProdNamesRoomViewModel) {
            prodNameRoomBaseRepository.delete(
                //ConvertList.toEntity(item)
                item
            )
        }
    }

    fun deleteAll() {
        Coroutines.io(this@ProdNamesRoomViewModel) {
            prodNameRoomBaseRepository.deleteAll()
        }
    }

    fun getItems() : LiveData<MutableList<ProdRoomNamesModel>> { //return  liveList
        return ConvertProdNamesList.toLiveDataListModel(
            prodNameRoomBaseRepository.getAll()
        )
    }
}