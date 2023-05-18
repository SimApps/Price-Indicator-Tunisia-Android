@file:OptIn(ExperimentalMaterial3Api::class)

package com.amirami.simapp.priceindicatortunisia.screens.accueil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.productsnames.ProductNameViewModel
import com.amirami.simapp.priceindicatortunisia.screens.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.ui.componenet.LottieComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ProductList
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ProgressBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDilogScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.getbyproducttype.GetProductByTypesListView
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchView
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ACTION_GET_PROD_BY_TYPES
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ERREUR_CONNECTION
import com.amirami.simapp.priceindicatortunisia.utils.Converters
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.capitalizeWords
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    padding: PaddingValues,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel,
    productNameViewModel: ProductNameViewModel,
    addModifyViewModel : AddModifyViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel

) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
LaunchedEffect(key1 = barCodeViewModel.fidCardBarCodeInfo.value){
    if (barCodeViewModel.fidCardBarCodeInfo.value != "") {
        val barecodeValue = if (Functions.isNumber(barCodeViewModel.fidCardBarCodeInfo.value)) {
            Functions.removeLeadingZeroes(barCodeViewModel.fidCardBarCodeInfo.value)
        } else barCodeViewModel.fidCardBarCodeInfo.value

        barecodeValue.let {
            searchViewModel.onsearchValue(it)
            productsViewModel.getProds(Functions.searchType(it), it.capitalizeWords())

            val fidcard = FidCardEntity(name = "", value = "", barecodeformat = -1, barecodetype = -1)
            barCodeViewModel.onfidCardInfo(fidcard)
        }
    }
}
    if (productDetailDialogViewModel.prodDetailDialogVisibilityStates) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
                productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(false)

            },
        ) {
            ProductDetailDilogScreen(
               product =  productsViewModel.selectedProductStates,
                onModifyClick = {
                    navController.navigate(ListScreens.AddModify.Route)
                },
                selectedOption = productDetailDialogViewModel.selectedOption,
                onSelectionChange = {
                    productDetailDialogViewModel.onSelectionChange(it)
                }
            )

        }
    }




            HomeScreenContent(
                padding = padding,
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                productsViewModel = productsViewModel,
                searchViewModel = searchViewModel,
                productNameViewModel = productNameViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                addModifyViewModel = addModifyViewModel
            )
        }



@Composable
fun HomeScreenContent(
    padding: PaddingValues,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel,
    productNameViewModel: ProductNameViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    addModifyViewModel : AddModifyViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),

        // .background(Color.White)
        //  .verticalScroll(rememberScrollState()),
        // verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (productNameViewModel.productLocalNames.isNotEmpty()) {
            SearchView(
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                prodname = Converters.fromString(productNameViewModel.productLocalNames.map { it.name }.first()!!),
                productsViewModel = productsViewModel,
                searchViewModel = searchViewModel
            )



        }

        if (productNameViewModel.isLoading) {
            Spacer(modifier = Modifier.padding(top = 60.dp))
            ProgressBar()
        }

        // THIS COLUM MAKE THE TYPE  LIST HIDE WHEN SCROLL DOWN
        Column(
            modifier = Modifier
                .fillMaxSize()
                // .background(Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(top = 15.dp))
            productsViewModel.onActionTypesListViewChanged(ACTION_GET_PROD_BY_TYPES)
            productsViewModel.onTypesArraysChange(context.resources.getStringArray(R.array.productFeauteredTypeArray))

            GetProductByTypesListView(
                productsViewModel = productsViewModel,
                addModifyViewModel = addModifyViewModel,
               from = ""
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))

            if (productNameViewModel.message == ERREUR_CONNECTION) {
                LottieComposable(
                    250.dp,
                    R.raw.no_internet_connection
                )
            }

            if(productsViewModel.isLoading) ProgressBar()
if(productsViewModel.errorValue !="")Functions.errorToast(context, productsViewModel.errorValue)
            ProductList(
                prodsResponse = productsViewModel.productListStates,
                productDetailDialogViewModel = productDetailDialogViewModel,
                productsViewModel = productsViewModel,
                addModifyViewModel = addModifyViewModel
            )



        }
    }
}
