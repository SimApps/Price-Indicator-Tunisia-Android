package com.amirami.simapp.priceindicatortunisia.screens.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirami.simapp.priceindicatortunisia.datastore.preferences.abstraction.DataStoreRepository
import com.amirami.simapp.priceindicatortunisia.datastore.utils.DARK_THEME
import com.amirami.simapp.priceindicatortunisia.ui.theme.Theme
import com.amirami.simapp.priceindicatortunisia.utils.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel@Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val repository: DataStoreRepository

) : ViewModel() {

    init {
        viewModelScope.launch {
            val theme = if(repository.getString(DARK_THEME) == "") Theme.SYSTEM_THEME.theme
            else repository.getString(DARK_THEME)
            setThemeMode(theme)
        }


    }
    var darkTheme: String by mutableStateOf(Theme.SYSTEM_THEME.theme)
        private set
    fun setThemeMode(value : String){
        darkTheme = value

        viewModelScope.launch {

            repository.putString(DARK_THEME,value)
        }

    }


    var isDarkTheme: Boolean by mutableStateOf(false)
        private set
    fun setisDarkThemeMode(value : Boolean){
        isDarkTheme = value
    }



    var showCustomDialog: Boolean by mutableStateOf(false)
        private set
    fun onShowCustomDialogChange(value : Boolean){
        showCustomDialog = value
    }

    var dialogueInfo by mutableStateOf(DialogueInfo())
        private set
    fun onDialogueInfoChange(value : DialogueInfo){
        dialogueInfo = value
    }
}

data class DialogueInfo(
    val title : String = "",
    val msg : String = "",
    val  imageVector: ImageVector = Icons.Default.ExitToApp
)