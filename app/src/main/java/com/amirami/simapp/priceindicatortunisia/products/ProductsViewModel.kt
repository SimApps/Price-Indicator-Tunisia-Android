package com.amirami.simapp.priceindicatortunisia.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.NotInit
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.Success
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



    var addProdResponse by mutableStateOf<Response<Void?>>(Success(null))
        private set
    var addProdsResponse by mutableStateOf<Response<Void?>>(Success(null))
        private set

    var deleteProdResponse by mutableStateOf<Response<Void?>>(Success(null))
        private set
    var isLoading by mutableStateOf(false)
        private set

    var errorValue by mutableStateOf("")
        private set

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
                is Response.Error -> {
                    isLoading = false
                    errorValue = (response as Response.Error).message
                }

            }
        }
    }

    fun addProduct(title: String, author: String) = viewModelScope.launch {
        useCasesProduct.addProduct(title, author).collect { response ->
            addProdResponse = response
        }
    }

    fun addProducts(title: String, author: String) = viewModelScope.launch {
        useCasesProduct.addProducts(title, author).collect { response ->
            addProdsResponse = response
        }
    }

    fun deleteProd(bookId: String) = viewModelScope.launch {
        useCasesProduct.deleteProduct(bookId).collect { response ->
            deleteProdResponse = response
        }
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

    var typesArrays by mutableStateOf<Array<String>>(emptyList<String>().toTypedArray())

    fun onprodDetailDialogVisibilityStatesChanged(typesArray: Array<String>) {
        typesArrays = typesArray
    }




    var typesModel by mutableStateOf(ProductModel())

    fun onprodDetailDialogVisibilityStatesChanged(typeModel: ProductModel) {
        typesModel = typeModel
    }
}
