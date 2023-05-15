package com.amirami.simapp.priceindicatortunisia.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.screens.accueil.HomeScreen
import com.amirami.simapp.priceindicatortunisia.screens.addmodify.AddModifyScreen
import com.amirami.simapp.priceindicatortunisia.screens.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.FidCardsScreen
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.GeneratedBarcodeImageScreen
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.FidCardRoomViewModel
import com.amirami.simapp.priceindicatortunisia.screens.courses.ShoppingScreen
import com.amirami.simapp.priceindicatortunisia.screens.priceremarqs.PriceRemarqScreen
import com.amirami.simapp.priceindicatortunisia.screens.settings.SettingViewModel
import com.amirami.simapp.priceindicatortunisia.screens.settings.SettingsScreen
import com.amirami.simapp.priceindicatortunisia.screens.tiket.TiketScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeCameraPreviewScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.CameraViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.MainImageTiket
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.camera.CameraXScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes.ProductTypesDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalCoilApi::class,
)
@Composable
fun Navigation(
    padding: PaddingValues,
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    settingViewModel: SettingViewModel,
) {
    val cameraViewModel: CameraViewModel = hiltViewModel()
    val barCodeViewModel: BarCodeViewModel = hiltViewModel()
    val fidCardRoomViewModel: FidCardRoomViewModel = hiltViewModel()

    val productDetailDialogViewModel: ProductDetailDialogViewModel = hiltViewModel()
    val productTypesDialogViewModel: ProductTypesDialogViewModel = hiltViewModel()
    val addModifyViewModel: AddModifyViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = ListScreens.Accueil.Route) {
        composable(ListScreens.Accueil.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            HomeScreen(
                padding = padding,
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
            )
        }

        composable(ListScreens.AddModify.Route) {
            //   barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            AddModifyScreen(
                padding = padding,
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                addModifyViewModel = addModifyViewModel,
                // productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                productTypesDialogViewModel  = productTypesDialogViewModel,
                productsViewModel = productsViewModel
            )
        }

        composable(ListScreens.Courses.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            ShoppingScreen(
                padding = padding,
                navController = navController,
                productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
            )
        }

        composable(ListScreens.CarteFidelite.Route) {
            FidCardsScreen(
                padding = padding,
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                fidCardRoomViewModel = fidCardRoomViewModel,
            )
        }

        composable(ListScreens.Tiket.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            TiketScreen(
                padding = padding,
                navController = navController,
                cameraViewModel = cameraViewModel,
            )
        }

        composable(ListScreens.Settings.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            SettingsScreen(
                padding = padding,
                settingViewModel = settingViewModel,
            )
        }

        composable(ListScreens.CameraX.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            CameraXScreen(
                padding = padding,
                barCodeViewModel = barCodeViewModel,
            )
        }

        composable(ListScreens.MainImageTiket.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            MainImageTiket(
                padding = padding,
                navController = navController,
                cameraViewModel = cameraViewModel,
                barCodeViewModel = barCodeViewModel,
            )
        }

        composable(ListScreens.BarCodeCameraPreview.Route) {
            //  fidCardsScreenViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            BarCodeCameraPreviewScreen(
                padding = padding,
                navController = navController,
                barCodeViewModel = barCodeViewModel,
            )
        }

        composable(ListScreens.GeneratedBarcodeImage.Route) {
            //    fidCardsScreenViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            GeneratedBarcodeImageScreen(
                padding = padding,
                barCodeViewModel = barCodeViewModel,
            )
        }


        composable(ListScreens.PriceRemarques.Route) {
            PriceRemarqScreen(
                padding = padding,
                navController = navController,
                addModifyViewModel = addModifyViewModel,
            )
        }
    }
}
