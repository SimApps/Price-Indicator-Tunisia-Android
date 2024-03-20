package com.amirami.simapp.priceindicatortunisia.navigation

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amirami.simapp.priceindicatortunisia.accueil.HomeScreen
import com.amirami.simapp.priceindicatortunisia.addmodify.AddModifyScreen
import com.amirami.simapp.priceindicatortunisia.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.cartefidelite.FidCardViewModel
import com.amirami.simapp.priceindicatortunisia.cartefidelite.views.FidCardsScreen
import com.amirami.simapp.priceindicatortunisia.cartefidelite.views.GeneratedBarcodeImageScreen
import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.courses.ShoppingScreen
import com.amirami.simapp.priceindicatortunisia.datastore.viewmodel.DataViewModel
import com.amirami.simapp.priceindicatortunisia.google_sign.GoogleAuthUiClient
import com.amirami.simapp.priceindicatortunisia.google_sign.SignInViewModel
import com.amirami.simapp.priceindicatortunisia.priceremarqs.PriceRemarqScreen
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.productsnames.ProductNameViewModel
import com.amirami.simapp.priceindicatortunisia.settings.SettingViewModel
import com.amirami.simapp.priceindicatortunisia.settings.SettingsScreen
import com.amirami.simapp.priceindicatortunisia.tiket.TiketScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeCameraPreviewScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.CameraViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.MainImageTiket
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.camera.CameraXScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes.ProductTypesDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun AppNavigation(
    googleAuthUiClient: GoogleAuthUiClient,
    settingViewModel: SettingViewModel
) {

    val context = LocalContext.current
    val lifecycleScope = rememberCoroutineScope()
    
    val navController = rememberNavController()
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val barCodeViewModel: BarCodeViewModel = hiltViewModel()


    val cameraViewModel: CameraViewModel = hiltViewModel()
    val fidCardViewModel: FidCardViewModel = hiltViewModel()

    val productDetailDialogViewModel: ProductDetailDialogViewModel = hiltViewModel()
    val productTypesDialogViewModel: ProductTypesDialogViewModel = hiltViewModel()
    val addModifyViewModel: AddModifyViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val productNameViewModel: ProductNameViewModel = hiltViewModel()
    val signInViewModel = viewModel<SignInViewModel>()
    val dataViewModel = viewModel<DataViewModel>()

    NavHost(
    navController = navController,
    startDestination = ListScreens.Accueil.Route
    ) {

        composable(ListScreens.Accueil.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            HomeScreen(
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                addModifyViewModel = addModifyViewModel,
                searchViewModel = searchViewModel,
                productNameViewModel = productNameViewModel,
                dataViewModel = dataViewModel
            )
        }

        composable(ListScreens.AddModify.Route) {
            //   barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            AddModifyScreen(
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                addModifyViewModel = addModifyViewModel,
                // productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                productTypesDialogViewModel = productTypesDialogViewModel,
                productsViewModel = productsViewModel
            )
        }

        composable(ListScreens.Courses.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            ShoppingScreen(
                navController = navController,
                productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                addModifyViewModel = addModifyViewModel
            )
        }

        composable(ListScreens.CarteFidelite.Route) {
            FidCardsScreen(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                barCodeViewModel = barCodeViewModel,
                fidCardViewModel = fidCardViewModel,
                productsViewModel = productsViewModel,
                userId = googleAuthUiClient.getSignedInUser()?.userId?:"N/A",
                dataViewModel = dataViewModel
            )
        }

        composable(ListScreens.Tiket.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            TiketScreen(
                navController = navController,
                cameraViewModel = cameraViewModel,
                productsViewModel = productsViewModel
            )
        }

        composable(ListScreens.Settings.Route) {
            // val state by viewModel.state.collectAsStateWithLifecycle()
            val state = signInViewModel.state


            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    signInViewModel.onSetIsSigneIn()
                }
            }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == ComponentActivity.RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            signInViewModel.onSignInResult(signInResult)

                        }
                    }
                }
            )




            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            SettingsScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                googleAuthUiClient = googleAuthUiClient,
                state = state,
                onSignInClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        Toast.makeText(
                            context,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                        signInViewModel.resetState()
                        //  navController.popBackStack()
                    }
                },
                navController = navController,
                settingViewModel = settingViewModel,
                resetState = {
                    //  signInViewModel.resetState()
                },
                productNameViewModel = productNameViewModel,
                barCodeViewModel = barCodeViewModel,
                productsViewModel = productsViewModel,
                ondeleteAccount = {
                    val userId = googleAuthUiClient.getSignedInUser()?.userId?:"NA"
                    if(userId!="NA") fidCardViewModel.deleteRemoteFidCardUserDocument(docID = userId)

                    //  lifecycleScope.launch {
                    // val deleteAccountIntentSender = googleAuthUiClient.deleteAccount()
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                    }
                    if(googleAuthUiClient.deleteAccount().isSuccessful){

                        Toast.makeText(
                            context,
                            "Acount deleted",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else
                        Toast.makeText(
                            context,
                            "Account not deleted",
                            Toast.LENGTH_LONG
                        ).show()

                    signInViewModel.resetState()


                    //   }
                },
                fidCardViewModel = fidCardViewModel
            )
        }

        composable(ListScreens.CameraX.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            CameraXScreen(
                barCodeViewModel = barCodeViewModel,
            )
        }

        composable(ListScreens.MainImageTiket.Route) {
            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            MainImageTiket(
                navController = navController,
                cameraViewModel = cameraViewModel,
                barCodeViewModel = barCodeViewModel,
                productsViewModel = productsViewModel
            )
        }

        composable(ListScreens.BarCodeCameraPreview.Route) {
            //  fidCardsScreenViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            BarCodeCameraPreviewScreen(
                navController = navController,
                barCodeViewModel = barCodeViewModel,
            )
        }

        composable(ListScreens.GeneratedBarcodeImage.Route) {
            //    fidCardsScreenViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
            GeneratedBarcodeImageScreen(
                barCodeViewModel = barCodeViewModel,
            )
        }


        composable(ListScreens.PriceRemarques.Route) {
            PriceRemarqScreen(
                navController = navController,
                productsViewModel = productsViewModel,
                addModifyViewModel = addModifyViewModel,
            )
        }
    }
}