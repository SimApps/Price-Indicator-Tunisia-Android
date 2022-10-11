package com.amirami.simapp.priceindicatortunisia.productsnames

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.core.Utils
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case.UseCasesProductName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.repository.BookRepository
import com.amirami.simapp.priceindicatortunisia.utils.Converters
import javax.inject.Inject

@HiltViewModel
class ProductNameViewModel @Inject constructor(
    private val useCasesProductName: UseCasesProductName,
    private val repo: BookRepository
) : ViewModel() {
    var prodnameRemoteResponse by mutableStateOf<Response<ArrayList<String>>>(Response.NotInit)
        private set



    var addProdNameRemoteResponse by mutableStateOf<Response<Void?>>(Response.Success(null))
        private set
    var addProdsNamesRemoteResponse by mutableStateOf<Response<Void?>>(Response.Success(null))
        private set

    var deleteProdNameRemoteResponse by mutableStateOf<Response<Void?>>(Response.Success(null))
        private set

    var productLocalNames by mutableStateOf(emptyList<ProductName>())

    var productLocalName by mutableStateOf(ProductName(0, ""))


    var isLoading by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set
    init {
        getLocalProdNames()
    }

    private fun getRemoteProdsNames() = viewModelScope.launch {
        isLoading = true
        useCasesProductName.getProductsNames().collect { response ->
            prodnameRemoteResponse = response

            when (val prodNamesResponse =  prodnameRemoteResponse) {
                is Response.NotInit ->  isLoading = false
                is Response.Loading ->   isLoading = true
                is Response.Success -> {
                    isLoading = false
                    message = ""

                    deleteAllLocalProdName()

                    addLocalProdNames(
                        ProductName(
                            id = 1,
                            Converters.fromArrayList(prodNamesResponse.data)
                        )
                    )


                }
                is Response.Error -> {
                    isLoading = false
                    message = prodNamesResponse.message
                    Utils.printMessage(prodNamesResponse.message)
                }

            }
        }
    }
    fun addRemoteProductName(title: String, author: String) = viewModelScope.launch {
        useCasesProductName.addProductName(title, author).collect { response ->
            addProdNameRemoteResponse = response
        }
    }

    fun addRemoteProductsNames(title: String, author: String) = viewModelScope.launch {
        useCasesProductName.addProductsNames(title, author).collect { response ->
            addProdsNamesRemoteResponse = response
        }
    }

    fun deleteRemoteProdName(bookId: String) = viewModelScope.launch {
        useCasesProductName.deleteProductName(bookId).collect { response ->
            deleteProdNameRemoteResponse = response
        }
    }

    fun getLocalProdNames() = viewModelScope.launch {
        repo.getProdNamesFromRoom().collect { dbBooks ->
            productLocalNames = dbBooks


            if(productLocalNames.isEmpty()){
                Log.d("loklok","1remote")
                getRemoteProdsNames()
            }
        }
    }

    fun  getLocalProdName(id: Int) = viewModelScope.launch {
        repo.getProdNameFromRoom(id).collect { dbBook ->
            productLocalName = dbBook
        }
    }

    fun addLocalProdNames(productName: ProductName) = viewModelScope.launch(Dispatchers.IO) {
        repo.addProdNamesToRoom(productName)
    }

    fun updatLocaleProdName(productName: ProductName) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateProdNameInRoom(productName)
    }

    fun deleteLocalProdName(productName: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteProdNameFromRoom(productName)
    }

    fun deleteAllLocalProdName() = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteAllProdNameFromRoom()
    }





}