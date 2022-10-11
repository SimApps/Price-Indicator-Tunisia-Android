package com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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


    var searchViewVisibilityStates by mutableStateOf(false)
    fun onsearchViewVisibilityStatesChanged(bottomNavigationBar: Boolean) {
        searchViewVisibilityStates = bottomNavigationBar
    }

}