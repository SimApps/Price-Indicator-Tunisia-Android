package com.amirami.simapp.priceindicatortunisia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar.BottomNavigationBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar.TopBar
import com.amirami.simapp.priceindicatortunisia.ui.navigation.Navigation
import com.amirami.simapp.priceindicatortunisia.ui.theme.DynamicColorTheme
import com.narify.netdetect.NetDetect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NetDetect.init(this@MainActivity)

        setContent {

            DynamicColorTheme {
                val navController = rememberNavController()
                val productsViewModel: ProductsViewModel = hiltViewModel()

                MainScreen(navController, modifier = Modifier.background(MaterialTheme.colorScheme.primary), productsViewModel)

            }
        }
    }

    @Composable
    fun MainScreen(navController: NavHostController, modifier: Modifier, productsViewModel: ProductsViewModel) {
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
                Navigation(modifier = modifier.padding(it), navController = navController, productsViewModel)

                          /*  Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(Color(0xff8d6e63)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Content of the page",
                        fontSize = 30.sp,
                        color = Color.White
                    )
                }*/
            }

        )
    }

}
