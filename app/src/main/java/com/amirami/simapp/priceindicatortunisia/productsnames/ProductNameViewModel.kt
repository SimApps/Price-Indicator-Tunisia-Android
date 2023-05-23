package com.amirami.simapp.priceindicatortunisia.productsnames

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.core.Utils
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.AddListProductNameResponse
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.AddProductNameResponse
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.DeleteProductNameResponse
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case.UseCasesProductName
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.repository.NameListRepository
import com.amirami.simapp.priceindicatortunisia.utils.Converters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductNameViewModel @Inject constructor(
    private val useCasesProductName: UseCasesProductName,
    private val repo: NameListRepository
) : ViewModel() {
    var prodnameRemoteResponse by mutableStateOf<Response<ArrayList<String>>>(Response.NotInit)
        private set



    var addProdNameRemoteResponse by mutableStateOf<AddProductNameResponse>(Response.NotInit)
        private set
    var addListProdsNamesRemoteResponse by mutableStateOf<AddListProductNameResponse>(Response.NotInit)
        private set

    var deleteProdNameRemoteResponse by mutableStateOf<DeleteProductNameResponse>(Response.NotInit)
        private set

  //  var productLocalNamesList by mutableStateOf(emptyList<ProductName>())
    var productLocalNamesList = mutableStateListOf<String>()
        private set
    var productLocalName by mutableStateOf(ProductName(0, ""))


    var isLoading by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set
    init {
        getLocalProdNamesList()
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
                is Response.Failure -> {
                    isLoading = false
                    message = prodNamesResponse.message
                    Utils.printMessage(prodNamesResponse.message)
                }

            }
        }
    }
    fun addRemoteProductName(prodName: String) = viewModelScope.launch {
        addProdNameRemoteResponse   = Response.Loading
        addProdNameRemoteResponse =  useCasesProductName.addProductName(prodName)
    }

    fun addRemoteListProductsNames(prodNameList : ArrayList<String>) = viewModelScope.launch {
        addListProdsNamesRemoteResponse = Response.Loading
        addListProdsNamesRemoteResponse =  useCasesProductName.addListProductsNames(prodNameList)

    }

    fun deleteRemoteProdName(prodName: String) = viewModelScope.launch {
        deleteProdNameRemoteResponse = Response.Loading
        deleteProdNameRemoteResponse =   useCasesProductName.deleteProductName(prodName)
    }

    fun getLocalProdNamesList() = viewModelScope.launch {
        repo.getProdNamesFromRoom().collect { nameList ->
            productLocalNamesList.addAll(
                Converters.fromString(nameList.map { it.name }.first()?:"")
            )

            if(productLocalNamesList.isEmpty()){
                getRemoteProdsNames()
            }
        }
    }


    fun addOneProductNamesToList(productName :String, preveiewsProdame : String){

        if (productLocalNamesList.indexOf(preveiewsProdame) != -1) {
         //   productLocalNamesList.first { it.name ==preveiewsProdame}.name = productName
             productLocalNamesList[productLocalNamesList.indexOf(preveiewsProdame)] = productName
        } else productLocalNamesList.add(productName)
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