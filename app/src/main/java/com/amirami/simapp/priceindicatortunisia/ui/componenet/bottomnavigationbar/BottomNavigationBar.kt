package com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amirami.simapp.priceindicatortunisia.BottomNavItem
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit,
    bottomNavigationBarViewModel: BottomNavigationBarViewModel = hiltViewModel(),
    productsViewModel: ProductsViewModel
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        ListScreens.BarCodeCameraPreview.Route -> {
            bottomNavigationBarViewModel.onBottomNavigationBarStateChanged(false)
        }
        ListScreens.CameraX.Route -> {
            bottomNavigationBarViewModel.onBottomNavigationBarStateChanged(false)
        }
        ListScreens.MainImageTiket.Route -> {
            bottomNavigationBarViewModel.onBottomNavigationBarStateChanged(false)
        }

        ListScreens.GeneratedBarcodeImage.Route -> {
            bottomNavigationBarViewModel.onBottomNavigationBarStateChanged(false)
        }

        ListScreens.AddModify.Route -> {
            bottomNavigationBarViewModel.onBottomNavigationBarStateChanged(false)
        }
        ListScreens.PriceRemarques.Route -> {
            bottomNavigationBarViewModel.onBottomNavigationBarStateChanged(false)
        }
        else -> {
            bottomNavigationBarViewModel.onBottomNavigationBarStateChanged(true)
        }
    }
    val bottomnavItems = listOf(
        BottomNavItem(
            name = "Accueil",
            route = "Accueil",
            icon = Icons.Default.Home,
        ),
        BottomNavItem(
            name = "Courses",
            route = "Courses",
            icon = Icons.Default.ShoppingCart,
            badgeCount = productsViewModel.shopLists.size,
        ),

        BottomNavItem(
            name = "Cart Fid",
            route = "Cartes de fidélitées",
            icon = Icons.Default.CreditCard,
        ),

        BottomNavItem(
            name = "Tiket",
            route = "Tiket",
            icon = Icons.Default.CameraAlt,
        ),

        BottomNavItem(
            name = "Parametres",
            route = "Parametres",
            icon = Icons.Default.Settings,
        ),
    )

    AnimatedVisibility(
        visible = bottomNavigationBarViewModel.BottomNavigationBarStates,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {

            NavigationBar {
                bottomnavItems.forEachIndexed { index, item ->
                    val selected = item.route == backStackEntry.value?.destination?.route

                    NavigationBarItem(
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AnimatedVisibility(
                                visible = item.badgeCount > 0,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut() + slideOutVertically()
                            ) {
                                BadgedBox(
                                    badge = {
                                        Text(text = item.badgeCount.toString())
                                    },
                                ) {
                                    Icon(
                                        imageVector =  item.icon,
                                        contentDescription = item.name,
                                    )
                                }
                            }
                            AnimatedVisibility(
                                visible = item.badgeCount <= 0,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut() + slideOutVertically()
                            ){
                                Icon(
                                    imageVector =  item.icon,
                                    contentDescription = item.name,
                                )
                            }

                        }
                        },
                        label = { Text(item.name) },
                        selected = selected,
                        onClick = { onItemClick(item)}
                    )
                }
            }

        },
    )
}
