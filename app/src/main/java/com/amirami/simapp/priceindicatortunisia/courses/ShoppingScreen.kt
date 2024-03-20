@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class
)

package com.amirami.simapp.priceindicatortunisia.courses

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUITS_FRAIS
import com.amirami.simapp.priceindicatortunisia.core.StringFormatting
import com.amirami.simapp.priceindicatortunisia.core.StringFormatting.stringToDouble
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.LottieComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar.BottomNavigationBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDilogScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar.TopBar
import com.amirami.simapp.priceindicatortunisia.utils.CustomModifiers.customWidth
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeAllDigitExeptX
import com.amirami.simapp.priceindicatortunisia.utils.Functions.succesToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    addModifyViewModel: AddModifyViewModel,
    shoppingViewModel: ShoppingViewModel = hiltViewModel()
) {


    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

     val prodList =   productsViewModel.shopLists

    val showPriceDetail = shoppingViewModel.showPriceDetail
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
        topBar = {
            TopBar(
                navController = navController,
                actions = {

                     Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = "Localized description",
                        modifier = Modifier.clickable {
                            productsViewModel.DeleteAllProdFromShopList()
                        })
                }
            )
        },
        bottomBar = {
        
             

                BottomNavigationBar(
                    navController = navController,
                    onItemClick = {
                        productsViewModel.resetErreurValue()
                        navController.navigate(it.route)
                    },

                    productsViewModel = productsViewModel
                )
            


        },

        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState)
        },
        floatingActionButton = {
            val density = LocalDensity.current

            AnimatedVisibility(
                // modifier = modifier,
                //TODO
                /**
                 * Add control auth to add article
                 */
                /**
                 * Add control auth to add article
                 */
                visible = prodList.isNotEmpty(),
                enter = slideInVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeIn(),
                exit = fadeOut(
                    animationSpec = keyframes {
                        this.durationMillis = 120
                    }
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    FloatingActionButton(
                        onClick = {
                            shoppingViewModel.onShowPriceDetailChange(!showPriceDetail)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory,
                            contentDescription = "stringResource(id = R.string.add_Article_button)"
                        )
                    }


                }
            }
        }

    ) { padding ->

        if(showPriceDetail) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }
                    shoppingViewModel.onShowPriceDetailChange(false)
                },
            ) {
                LazyColumn(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(3.dp, 6.dp, 3.dp, 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        IconButton(
                            onClick = {
                                shoppingViewModel.onCalculateDiscountChange(!shoppingViewModel.calculateDiscount)
                            }) {
                            Icon(
                                imageVector = if (shoppingViewModel.calculateDiscount) Icons.Filled.Percent
                                else Icons.Filled.Money,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                    shoppingViewModel.prices.sortByDescending { price -> stringToDouble(price.priceBill) }
                    items(shoppingViewModel.prices.size) { position ->

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        textDecoration = TextDecoration.Underline,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                                        fontStyle = MaterialTheme.typography.titleMedium.fontStyle
                                    )
                                ) {
                                    append(shoppingViewModel.prices[position].magasin + ": ")
                                }


                                append(StringFormatting.convertStringToPriceFormat(shoppingViewModel.prices[position].priceBill))


                                if(shoppingViewModel.prices[position].nbrMissingPrice>0) {
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.error
                                            //  fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                        )
                                    ) {
                                        append(" (-" + shoppingViewModel.prices[position].nbrMissingPrice + " prix)")
                                    }
                                }


                                if(shoppingViewModel.prices[position].bonusfid>0) {
                                    withStyle(
                                        SpanStyle(
                                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                                            fontStyle = MaterialTheme.typography.titleMedium.fontStyle
                                        )
                                    ) {
                                        append("bonus fid:")
                                    }
                                    append(shoppingViewModel.prices[position].bonusfid.toString())

                                }

                            }
                        )

                    }
                }

            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {







        if(prodList.isEmpty())    LottieComposable(
                size =  250.dp,
                lottiefile = R.raw.empty
            )

       else      ProductShopList(
                snackbarHostState = snackbarHostState,
                productsViewModel =  productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                shoppingViewModel = shoppingViewModel,
                addModifyViewModel = addModifyViewModel,
                 productList = prodList
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
    addModifyViewModel: AddModifyViewModel,
    productList :  List<ProductModel>
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .height(500.dp)
            .padding(3.dp, 6.dp, 3.dp, 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(productList.size) { position ->

            ProductShopTiket(
                snackbarHostState = snackbarHostState,
                prodsResponse = productList[position],
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ) {

                Icon(
                    modifier = Modifier
                        .customWidth(LocalConfiguration.current, 0.10f)
                        .clickable {
                            if (prodsResponse.typesub == PRODUITS_FRAIS) {
                                if (stringToDouble(prodsResponse.quantity) - 0.1 <= 0.001) {
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
                                    quantity = (stringToDouble(prodsResponse.quantity) - 0.1).toString(),
                                    id = prodsResponse.id
                                )
                            } else {
                                if (stringToDouble(prodsResponse.quantity) - 1 < 1) {
                                    setSnackBarData(
                                        coroutineScope = coroutineScope,
                                        snackbarHostState = snackbarHostState,
                                        prodsResponse = prodsResponse,
                                        context = context,
                                        productsViewModel = productsViewModel
                                    ) //  errorToast(context = context, "delete")
                                } else productsViewModel.UpdateProdQuantity(
                                    quantity = (stringToDouble(prodsResponse.quantity) - 1).toString(),
                                    id = prodsResponse.id
                                )
                            }
                        },
                    imageVector = Icons.Default.Minimize,
                    contentDescription = "Add"
                )

                Spacer(modifier = Modifier.padding(start = 9.dp))
                val keyboardController = LocalSoftwareKeyboardController.current


                OutlinedTextField(
                    enabled = false,
                    value = prodsResponse.quantity,
                    onValueChange = {

                   productsViewModel.UpdateProdQuantity(it, prodsResponse.id)

                        /* if (prodsResponse.typesub == PRODUITS_FRAIS) {
                            if (stringToDouble(it) - 0.1 <= 0.001) {
                                productsViewModel.DeleteProdFromShopList(prodsResponse.id)
                                setSnackBarData(
                                    coroutineScope = coroutineScope,
                                    snackbarHostState = snackbarHostState,
                                    prodsResponse = prodsResponse,
                                    context = context,
                                    productsViewModel = productsViewModel
                                )
                            }
                            else productsViewModel.UpdateProdQuantity(it, prodsResponse.id)
                        }
                        else {
                            if (stringToDouble(it) - 1 < 1) {
                                setSnackBarData(
                                    coroutineScope = coroutineScope,
                                    snackbarHostState = snackbarHostState,
                                    prodsResponse = prodsResponse,
                                    context = context,
                                    productsViewModel = productsViewModel
                                )
                            }

                            else productsViewModel.UpdateProdQuantity(it, prodsResponse.id)
                        }*/

                    },
                    modifier = Modifier.customWidth(LocalConfiguration.current, 0.30f),
                    label = {
                        Text(
                         text = context.getString(R.string.quantuty_en, removeAllDigitExeptX(prodsResponse.sieze)),
                             style = MaterialTheme.typography.labelSmall

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
                                quantity = (stringToDouble(prodsResponse.quantity) + 1).toString(),
                                id = prodsResponse.id
                            )

                            if (prodsResponse.typesub == PRODUITS_FRAIS) {
                                productsViewModel.UpdateProdQuantity(
                                    quantity = String.format(
                                        "%.3f",
                                        BigDecimal.valueOf(stringToDouble(prodsResponse.quantity)) + BigDecimal.valueOf(
                                            0.1
                                        )
                                    ),
                                    id = prodsResponse.id
                                )
                            } else {
                                productsViewModel.UpdateProdQuantity(
                                    quantity = (stringToDouble(prodsResponse.quantity) + 1).toString(),
                                    id = prodsResponse.id
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
) {
    SnackbarHost(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom),
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

                    OutlinedButton(onClick = { data.performAction() }) {
                        Text(
                            text = data.visuals.actionLabel ?: "",
                        )
                    }

                }
            )
        },
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




