package com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
) : ViewModel() {




    //var searchList = mutableStateListOf<String>()

    var searchValue by mutableStateOf<String>("")
        private set

    fun onsearchValue(product: String) {
        searchValue = product

    }


    var searchIsActive by mutableStateOf(false)
        private set

    fun onSearchIsActive(searchisActive: Boolean) {
        searchIsActive = searchisActive

    }

    var searchViewVisibilityStates by mutableStateOf(false)
    fun onsearchViewVisibilityStatesChanged(bottomNavigationBar: Boolean) {
        searchViewVisibilityStates = bottomNavigationBar
    }

}