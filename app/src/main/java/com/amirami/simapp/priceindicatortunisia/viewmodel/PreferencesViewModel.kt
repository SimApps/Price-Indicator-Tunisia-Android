package com.amirami.simapp.priceindicatortunisia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel  @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    val preferencesFlow = preferencesManager.preferencesFlow
/*
used to sort database see prference manager class and code inflow
    val preferencesFlow = preferencesManager.preferencesFlow


    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }
    */


    fun onFirstOpenChanged(value: Boolean) = viewModelScope.launch {
        preferencesManager.updateFirstOpen(value)
    }

    fun onDateListNameRefreshChanged(value: String) = viewModelScope.launch {
        preferencesManager.ListNameRefresh(value)
    }

    fun onNbrInterstitialAdShowedChanged(value: Int) = viewModelScope.launch {
        preferencesManager.nbrInterstitialAdShowed(value)
    }

    fun onLastShopingRefreshChanged(value: String) = viewModelScope.launch {
        preferencesManager.updateLastShopingRefresh(value)
    }
    //  val tasks = tasksFlow.asLiveData() ALSO FOR SORT ORDER SE CODE IN FLOW
}