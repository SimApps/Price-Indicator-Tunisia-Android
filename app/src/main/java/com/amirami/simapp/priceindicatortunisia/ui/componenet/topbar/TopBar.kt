package com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens

@Composable
fun TopBar(
    navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val title = navBackStackEntry?.destination?.route ?: "Title Here"

    when (navBackStackEntry?.destination?.route) {
        ListScreens.Accueil.Route -> {
            topAppBar(title, false)

            //   topBarViewModel.ontopBarStateChanged(false)
        }
        ListScreens.Courses.Route -> {
            topAppBar(title, true) //  topBarViewModel.ontopBarStateChanged(true)
        }
        ListScreens.CarteFidelite.Route -> {
            topAppBar(title, true) //  topBarViewModel.ontopBarStateChanged(true)
        }
        ListScreens.Tiket.Route -> {
            topAppBar(title, true) // topBarViewModel.ontopBarStateChanged(true)
        }
        ListScreens.Settings.Route -> {
            topAppBar(title, true) //     topBarViewModel.ontopBarStateChanged(true)
        }

        ListScreens.CameraX.Route -> {
            topAppBar(title, false) //    topBarViewModel.ontopBarStateChanged(false)
        }
        ListScreens.BarCodeCameraPreview.Route -> {
            topAppBar(title, false) //   topBarViewModel.ontopBarStateChanged(false)
        }
        ListScreens.MainImageTiket.Route -> {
            topAppBar(title, false) //    topBarViewModel.ontopBarStateChanged(false)
        }

        ListScreens.GeneratedBarcodeImage.Route -> {
            topAppBar(title, false) //  topBarViewModel.ontopBarStateChanged(false)
        }

        ListScreens.PriceRemarques.Route -> {
            topAppBar(title, false) //  topBarViewModel.ontopBarStateChanged(false)
        }
    }

    /**
     * OR
     * anim(title ,topBarViewModel.topBarStates)
     *
     */
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBar(title: String, vis: Boolean) {
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth(),
          //  .background(MaterialTheme.colorScheme.primary),
        visible = vis, // topBarViewModel.topBarStates,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            CenterAlignedTopAppBar(

                title = {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    //    color = MaterialTheme.colorScheme.onPrimary
                    )
                }

            )
        }
    )
}


