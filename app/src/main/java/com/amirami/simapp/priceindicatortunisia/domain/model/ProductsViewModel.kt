package com.amirami.simapp.priceindicatortunisia.domain.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductModelsViewModel @Inject constructor(
    private val repository: ProductModelFirestoreRepository
): ViewModel() {
  //  private val _dataState: MutableLiveData<RoomDataState<List<ProductModel>>> = MutableLiveData()


    private val firestoreEventChannel = Channel<FirestoreEvents>()
    val firestoreEvents = firestoreEventChannel.receiveAsFlow()

    fun getProductModelListFromFirestore(searchtype:String,searchtext:String) =  liveData(Dispatchers.IO) {
        emit(repository.getProductModelListFromFirestore(searchtype,searchtext))
    }
    private val _getResponseFromFirestoreUsingCoroutines = MutableSharedFlow<DataOrException<List<ProductModel>, String>>()
    val getResponseFromFirestoreUsingCoroutines: SharedFlow<DataOrException<List<ProductModel>, String>> = _getResponseFromFirestoreUsingCoroutines

     private val _getListResponseFromFirestoreUsingCoroutines = MutableSharedFlow<PagingData<ProductModel>>()
    val getListResponseFromFirestoreUsingCoroutines: SharedFlow<PagingData<ProductModel>> = _getListResponseFromFirestoreUsingCoroutines


    //   private val _getResponseFromFirestoreUsingCoroutines = MutableStateFlow<DataOrException<List<ProductModel>, String>>(DataOrException(null,null))
  //  val getResponseFromFirestoreUsingCoroutines: StateFlow<DataOrException<List<ProductModel>, String>> = _getResponseFromFirestoreUsingCoroutines




    fun getListResponseFromFirestoreUsingCoroutines(searchtype:String,searchtext:String) {
        viewModelScope.launch {
            _getListResponseFromFirestoreUsingCoroutines.emit(repository.getListResponseFromFirestoreUsingCoroutines(searchtype,searchtext).first())
            //     _getResponseFromFirestoreUsingCoroutines.value = repository.getResponseFromFirestoreUsingCoroutines(searchtype,searchtext)

        }
    }



    fun getAllProductModel() = liveData(Dispatchers.IO) {
        emit(repository.getAllProductModelListFromFirestore())
    }






    fun addProductModel(text: String)  = liveData(Dispatchers.IO) {
        emit(repository.getProductModelByIdFromFirestoreUsingCoroutines(text))
    }





    fun deleteProductModel(id: String) = liveData(Dispatchers.IO) {
        emit(repository.deleteProductModelInFirestore(id))
    }
    


    fun addProductModel(product: ProductModel, id: String) = liveData(Dispatchers.IO) {
        emit(repository.addProductModelInFirestore(product,id))
    }


    val productNameListLiveData = liveData(Dispatchers.IO) {
        emit(repository.getAllProductModelNameListFromFirestore())

    }



    fun addProductModelNamesArrayInFirestore(product: String) = liveData(Dispatchers.IO) {
        emit(repository.addProductModelNamesArrayInFirestore(product))

    }

    fun addAllProductModelNamesArrayInFirestore(product: ArrayList<String>) = liveData(Dispatchers.IO) {
        emit(repository.addAllProductModelNamesArrayInFirestore(product))

    }




    fun deleteProductModelNamesArrayInFirestore(product: String) = liveData(Dispatchers.IO) {
        emit(repository.deleteProductModelNamesArrayInFirestore(product))

    }




    sealed class FirestoreEvents {
        data class ProdAddToMsg(val msg:String) : FirestoreEvents()
        data class ProdDeleteMsg(val msg:String, val id: String,val name:String) : FirestoreEvents()
    }
}