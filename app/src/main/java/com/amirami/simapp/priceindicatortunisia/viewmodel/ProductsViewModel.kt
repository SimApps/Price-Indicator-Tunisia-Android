package com.amirami.simapp.priceindicatortunisia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amirami.simapp.priceindicatortunisia.data.DataOrException
import com.amirami.simapp.priceindicatortunisia.model.Product
import com.amirami.simapp.priceindicatortunisia.mainfragment.ProductFirestoreRepository
import com.amirami.simapp.priceindicatortunisia.utils.Coroutines
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductFirestoreRepository
): ViewModel() {
  //  private val _dataState: MutableLiveData<RoomDataState<List<Product>>> = MutableLiveData()


    private val firestoreEventChannel = Channel<FirestoreEvents>()
    val firestoreEvents = firestoreEventChannel.receiveAsFlow()

    fun getProductListFromFirestore(searchtype:String,searchtext:String) =  liveData(Dispatchers.IO) {
        emit(repository.getProductListFromFirestore(searchtype,searchtext))
    }
    private val _getResponseFromFirestoreUsingCoroutines = MutableStateFlow<DataOrException<List<Product>, String>>(DataOrException<List<Product>, String>())
    val getResponseFromFirestoreUsingCoroutines: StateFlow<DataOrException<List<Product>, String>> = _getResponseFromFirestoreUsingCoroutines

     private val _getListResponseFromFirestoreUsingCoroutines = MutableSharedFlow<PagingData<Product>>()
    val getListResponseFromFirestoreUsingCoroutines: SharedFlow<PagingData<Product>> = _getListResponseFromFirestoreUsingCoroutines


    //   private val _getResponseFromFirestoreUsingCoroutines = MutableStateFlow<DataOrException<List<Product>, String>>(DataOrException(null,null))
  //  val getResponseFromFirestoreUsingCoroutines: StateFlow<DataOrException<List<Product>, String>> = _getResponseFromFirestoreUsingCoroutines

    fun getResponseFromFirestoreUsingCoroutines(searchtype:String,searchtext:String) {
        viewModelScope.launch {
            Functions.bottomsheetStateInfo= "FirestoreloadFrom_AddFragment"
             //    _getResponseFromFirestoreUsingCoroutines.emit(repository.getResponseFromFirestoreUsingCoroutines(searchtype,searchtext))
            _getResponseFromFirestoreUsingCoroutines.value = repository.getResponseFromFirestoreUsingCoroutines(searchtype,searchtext)

    }
    }


    fun getListResponseFromFirestoreUsingCoroutines(searchtype:String,searchtext:String) {
        viewModelScope.launch {
            _getListResponseFromFirestoreUsingCoroutines.emit(repository.getListResponseFromFirestoreUsingCoroutines(searchtype,searchtext).first())
            //     _getResponseFromFirestoreUsingCoroutines.value = repository.getResponseFromFirestoreUsingCoroutines(searchtype,searchtext)

        }
    }



    fun getAllProduct() = liveData(Dispatchers.IO) {
        emit(repository.getAllProductListFromFirestore())
    }






    fun getProduct(text: String)  = liveData(Dispatchers.IO) {
        emit(repository.getProductByIdFromFirestoreUsingCoroutines(text))
    }





    fun deleteProduct(id: String) = liveData(Dispatchers.IO) {
        emit(repository.deleteProductInFirestore(id))
    }
    fun firestoreOperationState(msg:String,id:String,name:String )   {
        Coroutines.io(this@ProductsViewModel) {
            firestoreEventChannel.send(FirestoreEvents.ProdDeleteMsg(msg,id,name))
        }

    }


    fun getProduct(product: Product, id: String) = liveData(Dispatchers.IO) {
        emit(repository.addProductInFirestore(product,id))
    }


    val productNameListLiveData = liveData(Dispatchers.IO) {
        emit(repository.getAllProductNameListFromFirestore())

    }



    fun addProductNamesArrayInFirestore(product: String) = liveData(Dispatchers.IO) {
        emit(repository.addProductNamesArrayInFirestore(product))

    }

    fun addAllProductNamesArrayInFirestore(product: ArrayList<String>) = liveData(Dispatchers.IO) {
        emit(repository.addAllProductNamesArrayInFirestore(product))

    }




    fun deleteProductNamesArrayInFirestore(product: String) = liveData(Dispatchers.IO) {
        emit(repository.deleteProductNamesArrayInFirestore(product))

    }




    sealed class FirestoreEvents {
        data class ProdAddToMsg(val msg:String) : FirestoreEvents()
        data class ProdDeleteMsg(val msg:String, val id: String,val name:String) : FirestoreEvents()
    }
}