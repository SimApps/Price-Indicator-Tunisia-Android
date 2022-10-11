package com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject



class CameraViewModel @Inject constructor() : ViewModel() {

    private val _imState: MutableStateFlow<Uri> = MutableStateFlow(Uri.parse("file://dev/null"))
    val imState: StateFlow<Uri> get() = _imState.asStateFlow()

    fun onimState(imgUri: Uri) {
        _imState.update { imgUri }

    }

    var imageUri by mutableStateOf(Uri.parse("file://dev/null"))
        private set

    fun onimageUri(imgUri: Uri) {
        imageUri = imgUri
    }

    var showGallerySelect by mutableStateOf(false)
        private set

    fun onshowGallerySelect(showGallerySlct: Boolean) {
        showGallerySelect = showGallerySlct
    }

}