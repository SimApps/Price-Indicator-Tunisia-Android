package com.amirami.simapp.priceindicatortunisia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil.annotation.ExperimentalCoilApi
import com.amirami.simapp.priceindicatortunisia.google_sign.GoogleAuthUiClient
import com.amirami.simapp.priceindicatortunisia.navigation.AppNavigation
import com.amirami.simapp.priceindicatortunisia.settings.SettingViewModel
import com.amirami.simapp.priceindicatortunisia.ui.theme.AppTheme
import com.amirami.simapp.priceindicatortunisia.ui.theme.Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.auth.api.identity.Identity
import com.narify.netdetect.NetDetect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingViewModel: SettingViewModel by viewModels()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    @ExperimentalCoroutinesApi
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalCoilApi::class,
        ExperimentalMaterial3Api::class
    )
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
                ChangeSystemBarsTheme(lightTheme = !settingViewModel.isDarkTheme)



                AppNavigation(
                    googleAuthUiClient = googleAuthUiClient,
                    settingViewModel = settingViewModel
                )



            }
        }
    }



    @Composable
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val barColor = MaterialTheme.colorScheme.background.toArgb()
        LaunchedEffect(lightTheme) {
            if (lightTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        barColor, barColor,
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        barColor, barColor,
                    ),
                )
            } else {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(
                        barColor,
                    ),
                    navigationBarStyle = SystemBarStyle.dark(
                        barColor,
                    ),
                )
            }
        }
    }

}
