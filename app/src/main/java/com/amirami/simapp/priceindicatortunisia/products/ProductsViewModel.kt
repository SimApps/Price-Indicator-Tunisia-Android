package com.amirami.simapp.priceindicatortunisia.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.NotInit
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.Success
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.use_case.UseCasesProduct
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
    var prodResponse by mutableStateOf<Response<MutableList<ProductModel>>>(NotInit)
        private set

    var prodataResponse by mutableStateOf<List<ProductModel>>(emptyList<ProductModel>())
        private set

    var addProdResponse by mutableStateOf<Response<Void?>>(Success(null))
        private set
    var addProdsResponse by mutableStateOf<Response<Void?>>(Success(null))
        private set

    var deleteProdResponse by mutableStateOf<Response<Void?>>(Success(null))
        private set
    var isLoading by mutableStateOf(false)
        private set

    var shopLists by mutableStateOf(emptyList<ProductModel>())

    init {
        getShopListProducts()
    }

    fun getProds(searchtype: String, searchtext: String) = viewModelScope.launch {
        useCasesProduct.getProduct(searchtype, searchtext).collect { response ->
            prodResponse = response

          /*  when (prodResponse) {
                is NotInit -> isLoading = false
                is Response.Loading ->  isLoading = true
                is Success -> {
                    isLoading = false

                    prodataResponse = (prodResponse as Success<MutableList<Product>>).data

                }
                is Response.Error -> isLoading = false

            }*/
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

    fun UpdateProdQuantity(quantity: Double?, id: String?) = viewModelScope.launch(Dispatchers.IO) {
        shopListRepository.updateProdQuantityInRoom(quantity, id)
    }



}
