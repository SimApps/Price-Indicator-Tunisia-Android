package com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amirami.simapp.priceindicatortunisia.BottomNavItem
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.ui.navigation.ListScreens

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
        else -> {
            bottomNavigationBarViewModel.onBottomNavigationBarStateChanged(true)
        }
    }

    AnimatedVisibility(
        visible = bottomNavigationBarViewModel.BottomNavigationBarStates,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomNavigation(
                modifier = modifier.background(MaterialTheme.colorScheme.primary),
                //   backgroundColor = Color.Black,
                elevation = 5.dp
            ) {
                val bottomnavItems = listOf(
                    BottomNavItem(
                        name = "Accueil",
                        route = "Accueil",
                        icon = R.drawable.ic_homepage
                    ),
                    BottomNavItem(
                        name = "Courses",
                        route = "Courses",
                        icon = R.drawable.ic_shopping_list,
                        badgeCount = productsViewModel.shopLists.size
                    ),

                    BottomNavItem(
                        name = "Cartes de fidélitées",
                        route = "Cartes de fidélitées",
                        icon = R.drawable.ic_giftcard
                    ),

                    BottomNavItem(
                        name = "Tiket",
                        route = "Tiket",
                        icon = R.drawable.ic_take_a_picture
                    ),

                    BottomNavItem(
                        name = "Parametres",
                        route = "Parametres",
                        icon = R.drawable.ic_settings
                    )
                )

                bottomnavItems.forEach { item ->
                    val selected = item.route == backStackEntry.value?.destination?.route
                    BottomNavigationItem(
                        selected = selected,
                        onClick = { onItemClick(item) },
                        selectedContentColor = Color.Blue,
                        unselectedContentColor = Color.DarkGray,
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (item.badgeCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Text(text = item.badgeCount.toString(), color = MaterialTheme.colorScheme.error)
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = item.icon),
                                            contentDescription = item.name
                                        )
                                    }
                                } else {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = item.name
                                    )
                                }
                                if (selected) {
                                    Text(
                                        text = item.name,
                                        textAlign = TextAlign.Center,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onError
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    )
}
