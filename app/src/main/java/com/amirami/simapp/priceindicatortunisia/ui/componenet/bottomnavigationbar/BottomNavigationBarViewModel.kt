package com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel



class BottomNavigationBarViewModel (): ViewModel() {
    var BottomNavigationBarStates by mutableStateOf(false)
    fun onBottomNavigationBarStateChanged(bottomNavigationBar: Boolean) {
        BottomNavigationBarStates = bottomNavigationBar
    }
}