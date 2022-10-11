package com.amirami.simapp.priceindicatortunisia.ui.screens.accueil

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ProgressBar
import com.amirami.simapp.priceindicatortunisia.domain.model.Response.*
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.productsnames.ProductNameViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.LottieComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ProductList
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDilogScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.producttype.ProductTypesListView
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchView
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.FidCardsListItem
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ERREUR_CONNECTION
import com.amirami.simapp.priceindicatortunisia.utils.Converters
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.capitalizeWords
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel = hiltViewModel(),
    productNameViewModel: ProductNameViewModel = hiltViewModel(),
    productDetailDialogViewModel: ProductDetailDialogViewModel


) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    if (productDetailDialogViewModel.prodDetailDialogVisibilityStates) {
        // LaunchedEffect(key1 = context) {
        scope.launch {
            modalBottomSheetState.show()
            productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(false)
        }
        //  }
    }


    ModalBottomSheetLayout(
        sheetContent = {
            ProductDetailDilogScreen(productDetailDialogViewModel)
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        //sheetBackgroundColor = colorResource(id = R.color.gray),
        // scrimColor = Color.Red,  // Color for the fade background when you open/close the drawer
    ) {
        Scaffold(
            topBar = { },
            /*
            //use it like this when there is no lambda trail
            content = {
                   ShowSnackBar(visibleState, snackBarMessage)
               },*/
            //    backgroundColor = colorResource(id = R.color.purple_500),
            snackbarHost = {}
        ) { padding ->

            HomeScreenContent(
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                productsViewModel = productsViewModel,
                searchViewModel = searchViewModel,
                productNameViewModel = productNameViewModel,
                productDetailDialogViewModel
            )

        }
    }


}

@Composable
fun HomeScreenContent(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel,
    productNameViewModel: ProductNameViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
        // .background(Color.White)
        //  .verticalScroll(rememberScrollState()),
        // verticalArrangement = Arrangement.Top,
        //  horizontalAlignment = Alignment.Start
    ) {
        if (productNameViewModel.productLocalNames.isNotEmpty()) {
            SearchView(
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                prodname = Converters.fromString(productNameViewModel.productLocalNames.map { it.name }[0]!!),
                productsViewModel = productsViewModel,
                searchViewModel = searchViewModel
            )

        }


        if (productNameViewModel.isLoading) {
            Spacer(modifier = Modifier.padding(top = 60.dp))
            ProgressBar()
        }


        //THIS COLUM MAKE THE TYPE  LIST HIDE WHEN SCROLL DOWN
        Column(
            modifier = Modifier
                .fillMaxSize()
                // .background(Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(top = 15.dp))
            ProductTypesListView(productsViewModel)
            Spacer(modifier = Modifier.padding(top = 15.dp))
            if (productNameViewModel.message == ERREUR_CONNECTION) LottieComposable(
                250.dp,
                R.raw.no_internet_connection
            )
            when (val prodsResponse = productsViewModel.prodResponse) {
                is NotInit -> Log.d("dd", "Not Init")
                is Loading -> ProgressBar()
                is Success -> ProductList(
                    prodsResponse = prodsResponse.data,
                    productDetailDialogViewModel = productDetailDialogViewModel,
                    productsViewModel = productsViewModel
                )
                is Error -> Functions.errorToast(context, prodsResponse.message)

            }

            /*
            if (productsViewModel.isLoading)   ProgressBar()
         else if(productsViewModel.prodataResponse.isNotEmpty())  SubcomposeAsyncImage(
                model = productsViewModel.prodataResponse[0].imageurl,
                contentDescription = "stringResource(R.string.description)"
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator()
                    }
                    is AsyncImagePainter.State.Error -> {
                        SubcomposeAsyncImageContent(
                            painter = painterResource(id = R.drawable.ic_close)
                        )
                    }
                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
             */
        }

    }
}



