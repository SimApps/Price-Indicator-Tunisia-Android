package com.amirami.simapp.priceindicatortunisia.productsnames

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.core.Utils
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.use_case.UseCasesProductName
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.ProductName
import com.amirami.simapp.priceindicatortunisia.productsnames.room.repository.NameListLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductNameViewModel @Inject constructor(
    private val useCasesProductName: UseCasesProductName,
    private val repo: NameListLocalRepository
) : ViewModel() {


    var isLoading by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set
    init {
        getLocalProdNamesList()
    }



    private fun getRemoteProdsNamesBareCode() = viewModelScope.launch {
        isLoading = true
        useCasesProductName.getProductsNamesBareCode().collect { response ->


            when (val prodNamesResponse =  response) {
                is Response.NotInit ->  isLoading = false
                is Response.Loading ->   isLoading = true
                is Response.Success -> {
                    isLoading = false
                    message = ""

                    deleteAllLocalProdName()
                    //addAllProdNames(prodNamesResponse.data)

                    prodNamesResponse.data.forEach { (key, value) ->
                        // Do something with each key and value
                        addLocalProdNames(
                            ProductName(
                                id = key,
                                name = value
                            )
                        )
                    }
                  /*  for (name in prodNamesResponse.data) {
                        addLocalProdNames(
                            ProductName(
                                id =
                                //Converters.fromArrayList(prodNamesResponse.data)
                                name = name
                            )
                        )
                    }*/



                }
                is Response.Failure -> {
                    isLoading = false
                    message = prodNamesResponse.message
                    Utils.printMessage(prodNamesResponse.message)
                }

            }
        }
    }











    /**
     * * * * R O O M * * * *
     */


    var productLocalNamesList = mutableStateListOf<ProductName>()
        private set
    var productLocalName by mutableStateOf(ProductName())
    private fun getLocalProdNamesList() = viewModelScope.launch {
        repo.getProdNamesFromRoom().collect { nameList ->

            productLocalNamesList.clear()

            productLocalNamesList.addAll(nameList)
          //  addRemoteListProductsNamesBareCode(nameList.associate { it.id to it.name!! })
            if(productLocalNamesList.isEmpty()){
                getRemoteProdsNamesBareCode()
                return@collect
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

    fun addAllProdNames(productName: List<ProductName>) = viewModelScope.launch(Dispatchers.IO) {
        repo.addAllNames(productName)
    }
    fun updatLocaleProdName(id: String, updatedName: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateProdNameInRoom(id = id, updatedName = updatedName)
    }

    fun deleteLocalProdName(codeBar: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteProdNameFromRoom(codeBar)


    }

    private fun deleteAllLocalProdName() = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteAllProdNameFromRoom()
    }





}