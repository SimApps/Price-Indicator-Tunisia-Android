package com.amirami.simapp.priceindicatortunisia.cartefidelite

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.AddUserDocumentResponse
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.DeleteFidCardUserDocumentResponse
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.UpdateListFidCardResponse
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases.UseCasesFidCard
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.repository.FidCardRoomBaseRepository
import com.amirami.simapp.priceindicatortunisia.core.Constants
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







    var updateListFidCardRemoteResponse by mutableStateOf<UpdateListFidCardResponse>(Response.NotInit)
        private set

    var addFidCardUserDocumentResponse by mutableStateOf<AddUserDocumentResponse>(Response.NotInit)
        private set

    init {
        getFidCard()

    }
    var deleteFidCardRemoteUserDocumentResponse by mutableStateOf<DeleteFidCardUserDocumentResponse>(Response.NotInit)
        private set



    var isLoading by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set



     fun getRemoteFidCardBareCode(docID : String) = viewModelScope.launch {
        isLoading = true
        useCasesFidCard.getFidCard(docID).collect { response ->


            when (val prodNamesResponse =  response) {
                is Response.NotInit ->  isLoading = false
                is Response.Loading ->   isLoading = true
                is Response.Success -> {
                    isLoading = false
                    message = ""

                    //deleteAllLocalFidCard()
                    //addAllProdNames(prodNamesResponse.data)
                    val listFidCard =  if(fidCards.isNotEmpty())   fidCards as ArrayList
                    else ArrayList()
                     Log.d("pplkn","$docID "+listFidCard.size)
                    Log.d("pplkn","xx "+prodNamesResponse.data.size)
                    Log.d("pplkn","ww "+prodNamesResponse.toString())

               prodNamesResponse.data.forEach { (key, value) ->
                        // Do something with each key and value
                        val parts = value.split("°\\*\\-\\*°".toRegex())

                        listFidCard.add(
                            FidCardEntity(
                            value = key,
                            name = parts[0],
                            barecodeformat = parts[1].toInt(),
                            barecodetype = parts[2].toInt()
                        )
                        )


                            // Do something with each key and value

                            upsertFidCard(
                                FidCardEntity(
                                    value = key,
                                    name = parts[0],
                                    barecodeformat = parts[1].toInt(),
                                    barecodetype = parts[2].toInt()
                                )
                            )

                    }

                    Log.d("pplkn","listFidCard "+listFidCard.size)
                    updateRemoteListFidCard(
                        docID = docID,
                        fidCardList =
                        listFidCard.associateBy({fidCard -> fidCard.value }, {fidCard -> fidCard.name + Constants.SEPARATOR +fidCard.barecodeformat + Constants.SEPARATOR + fidCard.barecodetype })
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







    fun updateRemoteListFidCard(docID : String, fidCardList : Map<String,String>) = viewModelScope.launch {
        updateListFidCardRemoteResponse = Response.Loading
        updateListFidCardRemoteResponse =  useCasesFidCard.addListFidCard(docID = docID, fidCardList =fidCardList)



    }


    fun addRemoteUserDocumentFidCard(docID : String) = viewModelScope.launch {
        addFidCardUserDocumentResponse = Response.Loading
        addFidCardUserDocumentResponse =  useCasesFidCard.createUserFidCardDocument(docID = docID)
    }

    fun resetAddFidCardUserDocumentResponse(){
        addFidCardUserDocumentResponse = Response.NotInit
    }
    fun deleteRemoteFidCardUserDocument(docID : String) = viewModelScope.launch {
        deleteFidCardRemoteUserDocumentResponse = Response.Loading
        deleteFidCardRemoteUserDocumentResponse =   useCasesFidCard.deleteFidCardUserDocument(docID = docID)
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