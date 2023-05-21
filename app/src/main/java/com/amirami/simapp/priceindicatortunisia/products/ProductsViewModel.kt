package com.amirami.simapp.priceindicatortunisia.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.NotInit
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.Success
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.AddProductResponse
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.DeleteProductResponse
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.usecases.UseCasesProduct
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.products.room.domain.repository.ShopListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val useCasesProduct: UseCasesProduct,
    private val shopListRepository: ShopListRepository
) : ViewModel() {



    var addProdResponse by mutableStateOf<AddProductResponse>(Success(false))
        private set


    var deleteProdResponse by mutableStateOf<DeleteProductResponse>(Success(false))
        private set
    var isLoading by mutableStateOf(false)
        private set

    var errorValue by mutableStateOf("")
        private set

   fun resetErreurValue(){
       errorValue = ""
   }

    var shopLists by mutableStateOf(emptyList<ProductModel>())

    init {
        getShopListProducts()
    }
    var actionTypesListView by mutableStateOf<String>("")
    var prodType by mutableStateOf(ProductModel())

    fun onActionTypesListViewChanged(action: String) {
        actionTypesListView = action
    }

    var productListStates by mutableStateOf(emptyList<ProductModel>())

    var selectedProductStates by mutableStateOf(ProductModel())

    fun onSelectedProductChanged(product: ProductModel) {
        selectedProductStates = product


    }

    fun getProds(searchtype: String, searchtext: String) = viewModelScope.launch {
        useCasesProduct.getProduct(searchtype, searchtext).collect { response ->

            when (response) {
                is NotInit -> {
                    errorValue = ""
                    isLoading = false
                }
                is Response.Loading ->  {
                    errorValue = ""
                    isLoading = true
                }
                is Success -> {
                    isLoading = false
                    errorValue = ""
                    productListStates =  (response as Success<List<ProductModel>>).data
                    selectedProductStates = (response as Success<List<ProductModel>>).data.first()

                }
                is Response.Failure -> {
                    isLoading = false
                    errorValue = (response as Response.Failure).message
                }

            }
        }
    }

  /*  fun addProduct(product: ProductModel, id: String) = viewModelScope.launch {
        useCasesProduct.addProduct(product, author).collect { response ->
            addProdResponse = response
        }
    }*/

    fun addProductRemote(product: ProductModel, id: String) = viewModelScope.launch {
        addProdResponse = Response.Loading
        addProdResponse =   useCasesProduct.addProduct(product, id)
    }



    fun deleteProdRemote(id: String) = viewModelScope.launch {
        deleteProdResponse = Response.Loading
        deleteProdResponse =  useCasesProduct.deleteProduct(id)
    }

    fun getShopListProducts() = viewModelScope.launch {
        shopListRepository.getShopListFromRoom().collect { shoplist ->
            shopLists = shoplist
        }
    }

    fun AddProdToShopList(product: ProductModel) = viewModelScope.launch(Dispatchers.IO) {
        shopListRepository.addShopProdToRoom(product)
    }

    fun DeleteProdFromShopList(productid: String) = viewModelScope.launch(Dispatchers.IO) {
        shopListRepository.deleteProdShopListFromRoom(productid)
    }

    fun DeleteAllProdFromShopList() = viewModelScope.launch(Dispatchers.IO) {
        shopListRepository.deleteAllShopListFromRoom()
    }

    fun UpdateProdQuantity(quantity: Double?, id: String?) = viewModelScope.launch(Dispatchers.IO) {
        shopListRepository.updateProdQuantityInRoom(quantity, id)
    }

    var typesArrays by mutableStateOf(emptyList<String>().toTypedArray())

    fun onTypesArraysChange(typesArray: Array<String>) {
        typesArrays = typesArray
    }




    var typesModel by mutableStateOf(ProductModel())

    fun onTypesArraysChange(typeModel: ProductModel) {
        typesModel = typeModel
    }
}
