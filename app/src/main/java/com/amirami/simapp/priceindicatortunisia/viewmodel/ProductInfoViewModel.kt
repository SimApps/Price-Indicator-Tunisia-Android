package com.amirami.simapp.priceindicatortunisia.viewmodel

import androidx.lifecycle.*
import com.amirami.simapp.priceindicatortunisia.fidcard.Barecode
import com.amirami.simapp.priceindicatortunisia.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val productInfoEventsChannels = Channel<ProductEvents>()
    val productInfoEvents = productInfoEventsChannels.receiveAsFlow()

    private val dialogueEventsChannel = Channel<DialogueEvents>()
    val dialogueEvents = dialogueEventsChannel.receiveAsFlow()


    private val barecodeScanEventsChannel = Channel<BareCodeScanerEvents>()
    val barecodeScanEvents = barecodeScanEventsChannel.receiveAsFlow()


 /*   private val _putProductInfo = MutableLiveData<Product>()
    val putProductInfo: LiveData<Product>
        get() = _putProductInfo

    private val _putProductInfo = MutableSharedFlow<Product>()
    val putProductInfo: SharedFlow<Product>
        get() = _putProductInfo

  */



   /* private val _putProductInfo = MutableStateFlow<Product>(Product())
    val putProductInfo: StateFlow<Product>
        get() = _putProductInfo*/

    // Backing property to avoid state updates from other classes
    private val _putProductInfo = MutableStateFlow(LatestprodInfoTomodify.Success(Product()))
    // The UI collects from this StateFlow to get its state updates
    val putProductInfo: StateFlow<LatestprodInfoTomodify> = _putProductInfo

    fun putproductRemarqueInfo(magasin:String,result:String,bonusSurCartFid:String) {
        viewModelScope.launch {
            productInfoEventsChannels.send(ProductEvents.PutRemarqueInfo(magasin,result,bonusSurCartFid))
        }
    }

    fun putproductTypesInfo(type:String/*, subtype:String, subsubtype:String*/) {
        viewModelScope.launch {
            productInfoEventsChannels.send(ProductEvents.PutProductTypesInfo(type/*, subtype, subsubtype*/))
        }
    }

    fun putproductSubTypesInfo(type:String/*, subtype:String, subsubtype:String*/) {
        viewModelScope.launch {
            productInfoEventsChannels.send(ProductEvents.PutProductSubTypesInfo(type/*, subtype, subsubtype*/))
        }
    }


    fun putproductSubSubTypesInfo(type:String/*, subtype:String, subsubtype:String*/) {
        viewModelScope.launch {
            productInfoEventsChannels.send(ProductEvents.PutProductSubSubTypesInfo(type/*, subtype, subsubtype*/))
        }
    }

    fun putproductSizeInfo(size:String/*, subtype:String, subsubtype:String*/) {
        viewModelScope.launch {
            productInfoEventsChannels.send(ProductEvents.PutProductSizeInfo(size/*, subtype, subsubtype*/))
        }
    }



    fun putprodInfoTomodify(product: Product) {
        viewModelScope.launch {
           _putProductInfo.value =  LatestprodInfoTomodify.Success(product)
          //  _putProductInfo.emit(product)
        }

    }



    fun putProductInfoFromAddDialogue(id:String){
        viewModelScope.launch {
           // _putNewProductId.value =id
                dialogueEventsChannel.send(DialogueEvents.PutAddDialogueInfo(id))
        }
    }


    fun putBareCodeInfo(barecode: Barecode){
// _barecodeInfo.value = fidCard
        viewModelScope.launch {
            barecodeScanEventsChannel.send(BareCodeScanerEvents.PutBareCodeScanerInfo(barecode))
        }
    }
    sealed class ProductEvents {
        data class PutProductTypesInfo(val type:String/*, val subtype:String, val subsubtype:String*/) : ProductEvents()
        data class PutProductSubTypesInfo(val subtype:String) : ProductEvents()
        data class PutProductSubSubTypesInfo(val subsubtype:String) : ProductEvents()
        data class PutProductSizeInfo(val size:String) : ProductEvents()
        data class PutRemarqueInfo(val magasin:String, val discountInfo:String, val bonusfidnfo:String) : ProductEvents()

    }

    sealed class DialogueEvents {
        data class PutAddDialogueInfo(val id:String) : DialogueEvents()
    }


    sealed class BareCodeScanerEvents {
        data class PutBareCodeScanerInfo(val barecode: Barecode) : BareCodeScanerEvents()
    }


    // Represents different states for the LatestNews screen
    sealed class LatestprodInfoTomodify {
        data class Success(val product: Product): LatestprodInfoTomodify()
        data class Error(val exception: Throwable): LatestprodInfoTomodify()
    }
}