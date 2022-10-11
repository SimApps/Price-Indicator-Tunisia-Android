package com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.domain.repository.FidCardRoomBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FidCardRoomViewModel @Inject constructor(
    private val fidCardRoomBaseRepository: FidCardRoomBaseRepository
) : ViewModel() {

    var fidCards by mutableStateOf(emptyList<FidCardEntity>())


    var fidCardByValue by mutableStateOf(FidCardEntity())

init {
    getFidCard()

}
    fun getFidCard() = viewModelScope.launch {
        fidCardRoomBaseRepository.getAll().collect { fidCard ->
            fidCards = fidCard
        }
    }

    fun getFidCardByValue(value: String) = viewModelScope.launch {
        fidCardRoomBaseRepository.getByValue(value).collect { fidCrd ->
            fidCardByValue = fidCrd
        }
    }

    fun deleteFidCardByValue(value: String) = viewModelScope.launch(Dispatchers.IO) {
        fidCardRoomBaseRepository.deletebyid(value)
    }


    fun upsertFidCard(fidCardEntity: FidCardEntity) = viewModelScope.launch(Dispatchers.IO) {
        fidCardRoomBaseRepository.upsert(fidCardEntity)
    }
}