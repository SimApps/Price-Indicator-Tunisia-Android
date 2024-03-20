package com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar

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
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.utils.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    navController: NavController,
    onItemClick: (BottomNavItem) -> Unit,
    productsViewModel: ProductsViewModel
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

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
            name = "Paramètres",
            route = "Parametres",
            icon = Icons.Default.Settings,
        ),
    )



            NavigationBar {
                bottomnavItems.forEachIndexed { index, item ->
                    val selected = item.route == backStackEntry.value?.destination?.route

                    NavigationBarItem(
                        icon = {
                          //  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                BadgedBox(
                                    badge = {
                                       if(item.badgeCount > 0)
                                        Text(text = item.badgeCount.toString(),
                                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                            fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    },
                                ) {
                                    Icon(
                                        imageVector =  item.icon,
                                        contentDescription = item.name,
                                    )
                                }



                     //   }
                        },
                        label = {
                            Text(text = item.name, style = MaterialTheme.typography.labelSmall)
                                },
                        selected = selected,
                        onClick = { onItemClick(item)}
                    )
                }
            }



}
