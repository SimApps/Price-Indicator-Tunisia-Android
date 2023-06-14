package com.amirami.simapp.priceindicatortunisia.cartefidelite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.AddListFidCardResponse
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.DeleteFidCardResponse
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases.UseCasesFidCard
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.repository.FidCardRoomBaseRepository
import com.amirami.simapp.priceindicatortunisia.core.Utils
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FidCardViewModel @Inject constructor(
    private val fidCardRoomBaseRepository: FidCardRoomBaseRepository,
    private val useCasesFidCard: UseCasesFidCard
) : ViewModel() {





init {
    getFidCard()

}

    var addListFidCardRemoteResponse by mutableStateOf<AddListFidCardResponse>(Response.NotInit)
        private set

    var deleteFidCardRemoteResponse by mutableStateOf<DeleteFidCardResponse>(Response.NotInit)
        private set



    var isLoading by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set



    private fun getRemoteFidCardBareCode(docID : String) = viewModelScope.launch {
        isLoading = true
        useCasesFidCard.getFidCard(docID).collect { response ->


            when (val prodNamesResponse =  response) {
                is Response.NotInit ->  isLoading = false
                is Response.Loading ->   isLoading = true
                is Response.Success -> {
                    isLoading = false
                    message = ""

                    deleteAllLocalFidCard()
                    //addAllProdNames(prodNamesResponse.data)

                    prodNamesResponse.data.forEach { (key, value) ->
                        // Do something with each key and value
                        upsertFidCard(
                            FidCardEntity(
                                value = key,
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







    fun addRemoteListFidCard(docID : String, fidCardList : Map<String,String>) = viewModelScope.launch {
        addListFidCardRemoteResponse = Response.Loading
        addListFidCardRemoteResponse =  useCasesFidCard.addListFidCard(docID = docID, fidCardList =fidCardList)



    }

    fun deleteRemoteFidCard(docID : String, bareCode: String) = viewModelScope.launch {
        deleteFidCardRemoteResponse = Response.Loading
        deleteFidCardRemoteResponse =   useCasesFidCard.deleteFidCard(docID = docID,bareCode = bareCode)
    }



    /**
     * * * * * R O O M * * * * *
     */


    var fidCards by mutableStateOf(emptyList<FidCardEntity>())

    fun getFidCard() = viewModelScope.launch {
        fidCardRoomBaseRepository.getAll().collect { fidCard ->
            fidCards = fidCard
        }
    }



    fun deleteFidCardByValue(value: String) = viewModelScope.launch(Dispatchers.IO) {
        fidCardRoomBaseRepository.deletebyid(value)
    }


    fun deleteAllLocalFidCard() = viewModelScope.launch(Dispatchers.IO) {
        fidCardRoomBaseRepository.deleteAll()
    }


    fun upsertFidCard(fidCardEntity: FidCardEntity) = viewModelScope.launch(Dispatchers.IO) {
        fidCardRoomBaseRepository.upsert(fidCardEntity)
    }
}