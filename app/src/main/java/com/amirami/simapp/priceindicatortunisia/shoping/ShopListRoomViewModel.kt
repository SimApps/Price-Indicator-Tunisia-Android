package com.amirami.simapp.priceindicatortunisia.shoping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.model.Product
import com.amirami.simapp.priceindicatortunisia.utils.Functions.bottomsheetStateInfo
import com.amirami.simapp.priceindicatortunisia.repository.ShopListRoomBaseRepository
import com.amirami.simapp.priceindicatortunisia.utils.ConvertShopList
import com.amirami.simapp.priceindicatortunisia.utils.Coroutines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject



@HiltViewModel
class ShopListRoomViewModel @Inject constructor(
    private val  sholistRoomBaseRepository : ShopListRoomBaseRepository
): ViewModel() {
    private val tasksEventChannel = Channel<ShopListEvents>()
    val shopListEvents = tasksEventChannel.receiveAsFlow()

    private val liveList : MutableLiveData<MutableList<ProductShopingRoom>> by lazy(LazyThreadSafetyMode.NONE, initializer = {
        MutableLiveData<MutableList<ProductShopingRoom>>()
    })
    private val liveUpdate : MutableLiveData<ProductShopingRoom> by lazy(LazyThreadSafetyMode.NONE, initializer = {
        MutableLiveData<ProductShopingRoom>()
    })


    private val _nobillShopingRoom = MutableLiveData<ProductShopingRoom>()
    val nobillShopingRoom: LiveData<ProductShopingRoom>
        get() = _nobillShopingRoom

    private val _productinfo = MutableLiveData<Product>()
    val productinfo: LiveData<Product>
        get() = _productinfo

    private val _billShopingRoom = MutableLiveData<List<ProductShopingRoom>>()
    val billShopingRoom: LiveData<List<ProductShopingRoom>>
        get() = _billShopingRoom

    fun getInstance() : String {
        return this.toString()
    }

    fun getRepositoryInstance() : String {
        return sholistRoomBaseRepository.giveRepository()
    }

    @Deprecated("For Static Data")
    fun setItems() {
        Coroutines.default {
            sholistRoomBaseRepository.deleteAll()
            for (index in 0 until 500) {
               // sholistRoomBaseRepository.insert(ConvertList.toEntity(ProductShopingRoom(index,"name $index")))
            }

        }
    }


// NEVER NEVER DONT DELTE AND SEE IF IT POSSIBLE TO USE IT
/*
 fun setUpdate(item : ProductShopingRoom) {
        liveUpdate.value = item
    }

    fun getUpdate() : LiveData<ProductShopingRoom> {
        return liveUpdate
    }
    fun updateItem() {
        liveUpdate.value?.let {
            Coroutines.io(this@ShopListRoomViewModel) {
                sholistRoomBaseRepository.update(
                    ConvertShopList.toEntity(it)
                )
            }
        }
    }
*/



    fun updateItem(item : ProductShopingRoom) {
        Coroutines.io(this@ShopListRoomViewModel) {
            sholistRoomBaseRepository.update(ConvertShopList.toEntity(item))
        }

    }



    fun insertItem(item : ProductShopingRoom, msg:String) {
        Coroutines.io(this@ShopListRoomViewModel) {
            sholistRoomBaseRepository.insert(ConvertShopList.toEntity(item))
            tasksEventChannel.send(ShopListEvents.ProdAddToShopMsg(msg))
        }
    }


    fun updateQuantity(quantity : Double,id:Long) {
        Coroutines.io(this@ShopListRoomViewModel) {
            sholistRoomBaseRepository.updateQuantity(quantity,id)
        }
    }


    fun deleteItem(prodId : Long?,msg:String) {
        Coroutines.io(this@ShopListRoomViewModel) {
            sholistRoomBaseRepository.delete(prodId)
            tasksEventChannel.send(ShopListEvents.ProdDeleteShopMsg(msg))
        }
    }

    fun deleteAll(msg:String) {
        Coroutines.io(this@ShopListRoomViewModel) {
            sholistRoomBaseRepository.deleteAll()
            tasksEventChannel.send(ShopListEvents.ProdDeleteShopMsg(msg))

        }
    }

    fun getItems() : LiveData<MutableList<ProductShopingRoom>> { //return  liveList
        return ConvertShopList.toLiveDataListModel(
            sholistRoomBaseRepository.getAll()
        )
    }



    fun putProductRoomInfoToBottomSheetNoBill(productShopingRoom: ProductShopingRoom, bottomsheetStateInf:String){
        bottomsheetStateInfo = bottomsheetStateInf
        _nobillShopingRoom.value=productShopingRoom

    }

    fun putProductRoomInfoToBottomSheetBill(productShopingRoom: List<ProductShopingRoom>){
        bottomsheetStateInfo = "showbill"
      //  pricewithDicount = !pricewithDicount
        _billShopingRoom.value= productShopingRoom
    }



    sealed class ShopListEvents {
        data class ProdAddToShopMsg(val msg:String) : ShopListEvents()
        data class ProdDeleteShopMsg(val msg:String) : ShopListEvents()
    }



}