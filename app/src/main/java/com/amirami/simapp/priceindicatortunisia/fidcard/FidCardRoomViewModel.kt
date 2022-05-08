package com.amirami.simapp.priceindicatortunisia.fidcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.utils.ConvertFidCardList
import com.amirami.simapp.priceindicatortunisia.utils.Coroutines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject



@HiltViewModel
class FidCardRoomViewModel @Inject constructor(
    private val  fidCardRoomBaseRepository : FidCardRoomBaseRepository
): ViewModel() {

    private val tasksEventChannel = Channel<FidCardEvents>()
    val fidCardEvents = tasksEventChannel.receiveAsFlow()

    private val liveList : MutableLiveData<MutableList<Barecode>> by lazy(LazyThreadSafetyMode.NONE, initializer = {
        MutableLiveData<MutableList<Barecode>>()
    })
    private val liveUpdate : MutableLiveData<Barecode> by lazy(LazyThreadSafetyMode.NONE, initializer = {
        MutableLiveData<Barecode>()
    })



    fun getInstance() : String {
        return this.toString()
    }

    fun getRepositoryInstance() : String {
        return fidCardRoomBaseRepository.giveRepository()
    }

    @Deprecated("For Static Data")
    fun setItems() {
        Coroutines.default {
            fidCardRoomBaseRepository.deleteAll()
            for (index in 0 until 500) {
                fidCardRoomBaseRepository.upsert(ConvertFidCardList.toEntity(
                    Barecode("name $index",
                    "value $index",
                    "barecode type $index",)
                ))
            }

        }
    }

    fun setUpdate(item : Barecode) {
        liveUpdate.value = item
    }

    fun getUpdate() : LiveData<Barecode> {
        return liveUpdate
    }

    fun insertItem(item : Barecode) {
        Coroutines.io(this@FidCardRoomViewModel) {
            fidCardRoomBaseRepository.upsert(
                ConvertFidCardList.toEntity(item)
            )
        }
    }

    fun updateItem() {
        liveUpdate.value?.let {
            Coroutines.io(this@FidCardRoomViewModel) {
                fidCardRoomBaseRepository.update(
                    ConvertFidCardList.toEntity(it)
                )
            }
        }
    }

    fun deletebyValue(item : String,msg:String) {
        Coroutines.io(this@FidCardRoomViewModel) {
            fidCardRoomBaseRepository.deletebyvalue(item)
            tasksEventChannel.send(FidCardEvents.FidCardDeleteMsg(msg))
        }
    }

    fun deleteAll() {
        Coroutines.io(this@FidCardRoomViewModel) {
            fidCardRoomBaseRepository.deleteAll()
        }
    }

    fun getItems() : LiveData<MutableList<Barecode>> { //return  liveList
        return ConvertFidCardList.toLiveDataListModel(
            fidCardRoomBaseRepository.getAll()
        )
    }

    sealed class FidCardEvents {

        data class FidCardDeleteMsg(val msg:String) : FidCardEvents()
    }
}