@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class
)

package com.amirami.simapp.priceindicatortunisia.screens.courses

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUITS_FRAIS
import com.amirami.simapp.priceindicatortunisia.core.StringFormatting.convertStringToPriceFormat
import com.amirami.simapp.priceindicatortunisia.core.StringFormatting.stringToDouble
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.screens.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDilogScreen
import com.amirami.simapp.priceindicatortunisia.utils.CustomModifiers.customWidth
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeAllDigitExeptX
import com.amirami.simapp.priceindicatortunisia.utils.Functions.succesToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun ShoppingScreen(
    padding: PaddingValues,
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    addModifyViewModel: AddModifyViewModel,
    shoppingViewModel: ShoppingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = productsViewModel.shopLists, key2 = shoppingViewModel.calculateDiscount) {


        shoppingViewModel.calculatePrices(
            context = context,
            product = productsViewModel.shopLists
        )
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
                onModifyClick = {
                    navController.navigate(ListScreens.AddModify.Route)
                },
                selectedOption = productDetailDialogViewModel.selectedOption,
                product = productsViewModel.selectedProductStates,
                onSelectionChange = {
                    productDetailDialogViewModel.onSelectionChange(it)
                }
            )
        }

    }


    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { },
        //  scaffoldState = scaffoldState,
        bottomBar = {
            AnimatedVisibility(
                visible = productsViewModel.shopLists.isNotEmpty(),
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                BottomAppBar(
                    actions = {
                        IconButton(
                            onClick = {
                                shoppingViewModel.onCalculateDiscountChange(!shoppingViewModel.calculateDiscount)
                            }) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (shoppingViewModel.calculateDiscount) Icons.Filled.Percent
                                    else Icons.Filled.Money,
                                    contentDescription = "Localized description"
                                )


                            }
                        }


                    },
                    floatingActionButton = {

                        FloatingActionButton(
                            onClick = {
                                productsViewModel.DeleteAllProdFromShopList()
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.DeleteForever, "Localized description")
                        }


                    }
                )
            }

        },
        snackbarHost = {
            DefaultSnackbar(
                snackbarHostState,
                modifier = Modifier
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            LazyColumn(
                modifier = Modifier
                    .wrapContentHeight()
                    // .fillMaxWidth()
                    .padding(3.dp, 6.dp, 3.dp, 6.dp),
                //   .fillMaxHeight()
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                shoppingViewModel.prices.sortByDescending { price -> stringToDouble(price.priceBill) }
                items(shoppingViewModel.prices.size) { position ->

                    Text(
                        text = shoppingViewModel.prices[position].magasin + " : " + convertStringToPriceFormat(
                            shoppingViewModel.prices[position].priceBill
                        ) + " (-" + shoppingViewModel.prices[position].nbrMissingPrice + " prix)" + " " + shoppingViewModel.prices[position].bonusfid
                    )

                }
            }


            ProductShopList(
                snackbarHostState = snackbarHostState,
                productsViewModel,
                productDetailDialogViewModel,
                shoppingViewModel = shoppingViewModel,
                addModifyViewModel = addModifyViewModel
            )
        }
    }

}

@Composable
fun ProductShopList(
    snackbarHostState: SnackbarHostState,
    productsViewModel: ProductsViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    shoppingViewModel: ShoppingViewModel,
    addModifyViewModel: AddModifyViewModel
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .height(500.dp)
            // .fillMaxWidth()
            .padding(3.dp, 6.dp, 3.dp, 6.dp),
        //   .fillMaxHeight()
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(productsViewModel.shopLists.size) { position ->

            ProductShopTiket(
                snackbarHostState = snackbarHostState,
                productsViewModel.shopLists[position],
                productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                shoppingViewModel = shoppingViewModel,
                prodIndex = position,
                addModifyViewModel = addModifyViewModel

            )
        }
    }
}

@OptIn(InternalTextApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ProductShopTiket(
    snackbarHostState: SnackbarHostState,
    prodsResponse: ProductModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productsViewModel: ProductsViewModel,
    shoppingViewModel: ShoppingViewModel,
    prodIndex: Int,
    addModifyViewModel: AddModifyViewModel
) {
    val context = LocalContext.current
    //  val (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(!productDetailDialogViewModel.prodDetailDialogVisibilityStates)


                productsViewModel.onSelectedProductChanged(product = prodsResponse)
                addModifyViewModel.onCurrentProductChange(prodsResponse)

            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .weight(2f)
                .size(85.dp)
                .clip(RoundedCornerShape(6.dp)),
            model = prodsResponse.imageurl,
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

        //  Spacer(modifier = Modifier.padding(start = 12.dp))

        Column(
            modifier = Modifier
                .weight(5f)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = prodsResponse.name,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 9.dp),
                softWrap = true
            )

            Row(
                modifier = Modifier
                    .wrapContentWidth(),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {

                Icon(
                    modifier = Modifier
                        .customWidth(LocalConfiguration.current, 0.10f)
                        .clickable {
                            if (prodsResponse.typesub == PRODUITS_FRAIS) {
                                if (prodsResponse.quantity - 0.1 <= 0.001) {
                                    productsViewModel.DeleteProdFromShopList(prodsResponse.id)
                                    setSnackBarData(
                                        coroutineScope = coroutineScope,
                                        snackbarHostState = snackbarHostState,
                                        prodsResponse = prodsResponse,
                                        context = context,
                                        productsViewModel = productsViewModel
                                    ) //  errorToast(context = context, "delete")
                                }
                                //   else productsViewModel.UpdateProdQuantity(String.format("%.3f", BigDecimal.valueOf(prodsResponse.quantity) - BigDecimal.valueOf(0.1)).toDouble(), prodsResponse.id)
                                else productsViewModel.UpdateProdQuantity(
                                    prodsResponse.quantity - 0.1,
                                    prodsResponse.id
                                )
                            } else {
                                if (prodsResponse.quantity - 1 < 1) {
                                    setSnackBarData(
                                        coroutineScope = coroutineScope,
                                        snackbarHostState = snackbarHostState,
                                        prodsResponse = prodsResponse,
                                        context = context,
                                        productsViewModel = productsViewModel
                                    ) //  errorToast(context = context, "delete")
                                } else productsViewModel.UpdateProdQuantity(
                                    prodsResponse.quantity - 1,
                                    prodsResponse.id
                                )
                            }
                        },
                    imageVector = Icons.Default.Minimize,
                    contentDescription = "Add"
                )

                Spacer(modifier = Modifier.padding(start = 9.dp))
                val keyboardController = LocalSoftwareKeyboardController.current

                /*  AnimatedText(
                      modifier = Modifier.customWidth(LocalConfiguration.current, 0.50f),
                  label = context.getString(
                      R.string.quantuty_en,
                      removeAllDigitExeptX(prodsResponse.sieze)
                  ),
                  count = prodsResponse.quantity,
                  onValueChange = {
                      productsViewModel.UpdateProdQuantity(stringToDouble(it), prodsResponse.id)
                  },
                      )*/
                OutlinedTextField(
                    value = prodsResponse.quantity.toString(),
                    onValueChange = {
                        productsViewModel.UpdateProdQuantity(stringToDouble(it), prodsResponse.id)
                    },
                    modifier = Modifier.customWidth(LocalConfiguration.current, 0.50f),
                    label = {
                        Text(
                            context.getString(
                                R.string.quantuty_en,
                                removeAllDigitExeptX(prodsResponse.sieze)
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions {
                        keyboardController?.hide()
                    }
                )

                Spacer(modifier = Modifier.padding(start = 9.dp))

                Icon(
                    modifier = Modifier
                        .customWidth(LocalConfiguration.current, 0.10f)
                        .clickable {
                            productsViewModel.UpdateProdQuantity(
                                prodsResponse.quantity + 1,
                                prodsResponse.id
                            )

                            if (prodsResponse.typesub == PRODUITS_FRAIS) {
                                productsViewModel.UpdateProdQuantity(
                                    String
                                        .format(
                                            "%.3f",
                                            BigDecimal.valueOf(prodsResponse.quantity) + BigDecimal.valueOf(
                                                0.1
                                            )
                                        )
                                        .toDouble(), prodsResponse.id
                                )
                            } else {
                                productsViewModel.UpdateProdQuantity(
                                    prodsResponse.quantity + 1,
                                    prodsResponse.id
                                )
                            }
                        },
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )

            }


        }


    }
}


@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.visuals.message,
                    )
                },
                action = {

                    TextButton(onClick = { data.performAction() }) {
                        Text(
                            text = data.visuals.actionLabel ?: "",
                        )
                    }

                }
            )
        },
        modifier = modifier
            .padding(0.dp, 0.dp, 0.dp, 50.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

fun setSnackBarData(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    prodsResponse: ProductModel,
    context: Context,
    productsViewModel: ProductsViewModel
) {
    coroutineScope.launch {
        val snackbarResult = snackbarHostState.showSnackbar(
            message = "Delete product : " + prodsResponse.name,
            actionLabel = "Delete",
            duration = androidx.compose.material3.SnackbarDuration.Long
        )

        when (snackbarResult) {
            SnackbarResult.Dismissed -> succesToast(context, "not deleted")
            SnackbarResult.ActionPerformed -> productsViewModel.DeleteProdFromShopList(prodsResponse.id)
        }
    }
}




