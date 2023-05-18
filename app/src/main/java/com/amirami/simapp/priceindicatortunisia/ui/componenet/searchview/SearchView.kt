package com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.HOME_SCREEN
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.capitalizeWords
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    prodname: ArrayList<String>,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel

) {
    val context = LocalContext.current
    SearchBar(
        query = searchViewModel.searchValue ,
        onQueryChange = {
            searchViewModel.onsearchValue(it)
        },
        onSearch = {
            if (searchViewModel.searchValue.isEmpty()) return@SearchBar
            productsViewModel.getProds(
                Functions.searchType(searchViewModel.searchValue),
                searchViewModel.searchValue.capitalizeWords()
            )

            searchViewModel.onSearchIsActive(false)
        },
        active = searchViewModel.searchIsActive,
        onActiveChange = {
            searchViewModel.onSearchIsActive(it)
        },
        placeholder = {
            Text(text = context.getString(R.string.search_hint))
        },
        leadingIcon = {
            Icon(
                imageVector =  Icons.Default.Search,
                contentDescription = ""
            )
        },
        trailingIcon = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = searchViewModel.searchIsActive,
                    enter = slideInVertically(initialOffsetY = { -it }),
                    exit = slideOutVertically(targetOffsetY = { -it }),
                    content = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            Modifier.clickable {
                                if (searchViewModel.searchValue.isNotEmpty())
                                    searchViewModel.onsearchValue("")
                                else searchViewModel.onSearchIsActive(false)
                            }
                        )
                    })


                Spacer(modifier = Modifier.size(12.dp))

                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(
                        R.raw.barcode_scanner
                    )
                )
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    restartOnPlay = false,
                    iterations = Int.MAX_VALUE
                )

                LottieAnimation(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ListScreens.BarCodeCameraPreview.Route)
                        }
                        .size(40.dp),
                    composition = composition,
                    progress = { progress }
                )

            }



        }
    ) {
        val finalListName =     if(searchViewModel.searchValue.isEmpty())
            prodname
        else prodname.filter { it.lowercase(Locale.getDefault()).contains(searchViewModel.searchValue.lowercase(Locale.getDefault())) }


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(finalListName.size) { position ->
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                searchViewModel.onsearchValue(finalListName[position])
                                productsViewModel.getProds(
                                    Functions.searchType(finalListName[position]),
                                    finalListName[position].capitalizeWords()
                                )

                                searchViewModel.onSearchIsActive(false)
                            },
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row {
                            Column {

                                Text(text = finalListName[position])

                                // Text(text = product.phoneNumber, style = MaterialTheme.typography.subtitle2)
                                // Text(text = product.email, style = MaterialTheme.typography.subtitle2)
                            }
                        }
                    }
                }

            }
        }
    }
}