package com.amirami.simapp.priceindicatortunisia.ui.navigation

sealed class ListScreens(val Route: String) {
    object Accueil : ListScreens("Accueil")
    object CarteFidelite : ListScreens("Cartes de fidélitées")
    object Courses : ListScreens("Courses")
    object Tiket : ListScreens("Tiket")
    object Settings : ListScreens("Parametres")

    object CameraX : ListScreens("CameraX")
    object MainImageTiket : ListScreens("MainImageTiket")

    object BarCodeCameraPreview : ListScreens("BarCodeCameraPreview")

    object GeneratedBarcodeImage : ListScreens("GeneratedBarcodeImage")

}