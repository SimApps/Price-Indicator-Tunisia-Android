package com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TopBarViewModel (): ViewModel() {
    var topBarStates by mutableStateOf(false)
    fun ontopBarStateChanged(topBarState: Boolean) {
        topBarStates = topBarState
    }
}