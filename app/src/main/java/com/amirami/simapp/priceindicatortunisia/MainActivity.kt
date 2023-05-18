package com.amirami.simapp.priceindicatortunisia

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.google_sign.GoogleAuthUiClient
import com.amirami.simapp.priceindicatortunisia.google_sign.SignInViewModel
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.productsnames.ProductNameViewModel
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
import com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar.BottomNavigationBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.CameraViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.MainImageTiket
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.camera.CameraXScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes.ProductTypesDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar.TopBar
import com.amirami.simapp.priceindicatortunisia.ui.theme.AppTheme
import com.amirami.simapp.priceindicatortunisia.ui.theme.Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.auth.api.identity.Identity
import com.narify.netdetect.NetDetect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingViewModel: SettingViewModel by viewModels()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NetDetect.init(this@MainActivity)

        installSplashScreen().apply {
            /*  setKeepOnScreenCondition() {
                //    viewModel.isLoading.value
                         authViewModel.proCaisseState.value.loading ||
                        authViewModel.proInventoryState.value.loading ||
                        authViewModel.proCaisseLicence.loading ||
                        authViewModel.proInventoryLicence.loading
            }*/
        }
        setContent {
            Theme(settingViewModel = settingViewModel)
            AppTheme(darkTheme = settingViewModel.isDarkTheme) {
                val navController = rememberNavController()
                val productsViewModel: ProductsViewModel = hiltViewModel()

                MainScreen(
                    navController, modifier = Modifier,
                    productsViewModel= productsViewModel,
                    settingViewModel = settingViewModel)

            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun MainScreen(navController: NavHostController,
                   modifier: Modifier,
                   productsViewModel: ProductsViewModel,
                   settingViewModel: SettingViewModel) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopBar(
                    navController = navController,
                    modifier = modifier
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    modifier = modifier,
                    navController = navController,
                    onItemClick = {
                        navController.navigate(it.route)
                    },

                    productsViewModel = productsViewModel
                )
            },
            content = { padding ->
                val cameraViewModel: CameraViewModel = hiltViewModel()
                val barCodeViewModel: BarCodeViewModel = hiltViewModel()
                val fidCardRoomViewModel: FidCardRoomViewModel = hiltViewModel()

                val productDetailDialogViewModel: ProductDetailDialogViewModel = hiltViewModel()
                val productTypesDialogViewModel: ProductTypesDialogViewModel = hiltViewModel()
                val addModifyViewModel: AddModifyViewModel = hiltViewModel()
                val searchViewModel: SearchViewModel = hiltViewModel()
                val productNameViewModel: ProductNameViewModel = hiltViewModel()
                val signInViewModel = viewModel<SignInViewModel>()

                NavHost(modifier = Modifier.padding(padding),navController = navController, startDestination = ListScreens.Accueil.Route) {

                    composable(ListScreens.Accueil.Route) {
                        barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
                        HomeScreen(
                            padding = padding,
                            navController = navController,
                            barCodeViewModel = barCodeViewModel,
                            productsViewModel = productsViewModel,
                            productDetailDialogViewModel = productDetailDialogViewModel,
                            addModifyViewModel = addModifyViewModel,
                            searchViewModel = searchViewModel,
                            productNameViewModel = productNameViewModel
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
                            addModifyViewModel = addModifyViewModel
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
                       // val state by viewModel.state.collectAsStateWithLifecycle()
                        val state = signInViewModel.state


                        LaunchedEffect(key1 = Unit) {
                            if(googleAuthUiClient.getSignedInUser() != null) {
                               signInViewModel.onSetIsSigneIn()
                            }
                        }
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if(result.resultCode == RESULT_OK) {
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
                                        applicationContext,
                                        "Signed out",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    signInViewModel.resetState()
                                  //  navController.popBackStack()
                                }
                            },
                            padding = padding,
                            settingViewModel = settingViewModel,
                            resetState = {
                              //  signInViewModel.resetState()
                            }
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
              /*  Navigation(
                    googleAuthUiClient = googleAuthUiClient,
                    padding = it,
                    navController = navController,
                    productsViewModel = productsViewModel,
                    settingViewModel = settingViewModel)*/

            }

        )
    }

}
