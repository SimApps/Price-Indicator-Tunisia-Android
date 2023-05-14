package com.amirami.simapp.priceindicatortunisia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar.BottomNavigationBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar.TopBar
import com.amirami.simapp.priceindicatortunisia.navigation.Navigation
import com.amirami.simapp.priceindicatortunisia.screens.settings.SettingViewModel
import com.amirami.simapp.priceindicatortunisia.ui.theme.AppTheme
import com.amirami.simapp.priceindicatortunisia.ui.theme.Theme
import com.narify.netdetect.NetDetect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingViewModel: SettingViewModel by viewModels()

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

                MainScreen(navController, modifier = Modifier
                   /* .background(MaterialTheme.colorScheme.primary)*/,
                    productsViewModel= productsViewModel,
                    settingViewModel = settingViewModel)

            }
        }
    }

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
            content = {
                Navigation(modifier = modifier.padding(it),
                    navController = navController,
                    productsViewModel = productsViewModel,
                    settingViewModel = settingViewModel)

            }

        )
    }

}
