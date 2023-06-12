package com.amirami.simapp.priceindicatortunisia.datastore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.datastore.preferences.abstraction.DataStoreRepository
import com.amirami.simapp.priceindicatortunisia.datastore.utils.DARK_THEME
import com.amirami.simapp.priceindicatortunisia.datastore.utils.MAPNAME
import com.amirami.simapp.priceindicatortunisia.utils.IoDispatcher


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: DataStoreRepository
) : ViewModel() {

    fun getMAPNAME(): String = runBlocking {
        repository.getString(MAPNAME)
    }

    fun saveMAPNAME(value: String) {
        viewModelScope.launch(dispatcher) {
            repository.putString(MAPNAME, value)
        }
    }





    fun getDarkTheme(): String = runBlocking {
        repository.getString(DARK_THEME)
    }

    fun saveDarkTheme(value: String) {
        viewModelScope.launch(dispatcher) {
            repository.putString(DARK_THEME, value)
        }
    }





}
