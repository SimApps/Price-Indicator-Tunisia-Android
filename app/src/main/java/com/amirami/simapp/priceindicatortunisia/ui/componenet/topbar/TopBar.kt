package com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens

@Composable
fun TopBar(
    navController: NavHostController,
    modifier: Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val title = navBackStackEntry?.destination?.route ?: "Title Here"

    when (navBackStackEntry?.destination?.route) {
        ListScreens.Accueil.Route -> {
            topAppBar(title, false, modifier)

            //   topBarViewModel.ontopBarStateChanged(false)
        }
        ListScreens.Courses.Route -> {
            topAppBar(title, true, modifier) //  topBarViewModel.ontopBarStateChanged(true)
        }
        ListScreens.CarteFidelite.Route -> {
            topAppBar(title, true, modifier) //  topBarViewModel.ontopBarStateChanged(true)
        }
        ListScreens.Tiket.Route -> {
            topAppBar(title, true, modifier) // topBarViewModel.ontopBarStateChanged(true)
        }
        ListScreens.Settings.Route -> {
            topAppBar(title, true, modifier) //     topBarViewModel.ontopBarStateChanged(true)
        }

        ListScreens.CameraX.Route -> {
            topAppBar(title, false, modifier) //    topBarViewModel.ontopBarStateChanged(false)
        }
        ListScreens.BarCodeCameraPreview.Route -> {
            topAppBar(title, false, modifier) //   topBarViewModel.ontopBarStateChanged(false)
        }
        ListScreens.MainImageTiket.Route -> {
            topAppBar(title, false, modifier) //    topBarViewModel.ontopBarStateChanged(false)
        }

        ListScreens.GeneratedBarcodeImage.Route -> {
            topAppBar(title, false, modifier) //  topBarViewModel.ontopBarStateChanged(false)
        }

        ListScreens.PriceRemarques.Route -> {
            topAppBar(title, false, modifier) //  topBarViewModel.ontopBarStateChanged(false)
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
fun topAppBar(title: String, vis: Boolean, modifier: Modifier) {
    AnimatedVisibility(
        modifier = modifier
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


