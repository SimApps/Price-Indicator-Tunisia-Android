@file:OptIn(ExperimentalMaterial3Api::class)

package com.amirami.simapp.priceindicatortunisia.screens.courses

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDilogScreen
import com.amirami.simapp.priceindicatortunisia.utils.CustomModifiers.customWidth
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeAllDigitExeptX
import com.amirami.simapp.priceindicatortunisia.utils.Functions.succesToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun ShoppingScreen(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    shoppingViewModel: ShoppingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

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
            ProductDetailDilogScreen(productDetailDialogViewModel, navController)

        }
    }


        Scaffold(
            topBar = { },
          //  scaffoldState = scaffoldState,
            bottomBar = {},
            snackbarHost = {
                DefaultSnackbar(
                    snackbarHostState,
                    modifier = Modifier
                )
            }
        ) { padding ->

            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                ProductShopList(snackbarHostState = snackbarHostState,
                    productsViewModel, productDetailDialogViewModel, shoppingViewModel = shoppingViewModel)
            }
        }

}

@Composable
fun ProductShopList(
    snackbarHostState: SnackbarHostState,
    productsViewModel: ProductsViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    shoppingViewModel: ShoppingViewModel
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
                prodIndex = position

            )
        }
    }
}

@OptIn(InternalTextApi::class)
@Composable
fun ProductShopTiket(
    snackbarHostState: SnackbarHostState,
    prodsResponse: ProductModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productsViewModel: ProductsViewModel,
    shoppingViewModel: ShoppingViewModel,
    prodIndex: Int
) {
    val context = LocalContext.current
    //  val (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (productDetailDialogViewModel.prodDetailDialogVisibilityStates) {
                    productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(false)
                } else productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(true)

                productDetailDialogViewModel.onprodDetailDialogStatesChanged(product = prodsResponse)
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
                    modifier = Modifier.customWidth(LocalConfiguration.current, 0.10f).clickable {
                        if (prodsResponse.typesub == PRODUITS_FRAIS) {
                            if (prodsResponse.quantity - 0.1 <= 0.001) {
                                productsViewModel.DeleteProdFromShopList(prodsResponse.id)
                                setSnackBarData(coroutineScope = coroutineScope, snackbarHostState = snackbarHostState, prodsResponse = prodsResponse, context = context, productsViewModel = productsViewModel) //  errorToast(context = context, "delete")
                            } else productsViewModel.UpdateProdQuantity(String.format("%.3f", BigDecimal.valueOf(prodsResponse.quantity) - BigDecimal.valueOf(0.1)).toDouble(), prodsResponse.id)
                        } else {
                            if (prodsResponse.quantity - 1 < 1) {
                                setSnackBarData(coroutineScope = coroutineScope, snackbarHostState = snackbarHostState, prodsResponse = prodsResponse, context = context, productsViewModel = productsViewModel) //  errorToast(context = context, "delete")
                            } else productsViewModel.UpdateProdQuantity(prodsResponse.quantity - 1, prodsResponse.id)
                        }
                      },
                    imageVector =  Icons.Default.Minimize,
                    contentDescription = "Add"
                )

                Spacer(modifier = Modifier.padding(start = 9.dp))

                OutlinedTextField(
                    value = prodsResponse.quantity.toString(),
                    onValueChange = {
                        productsViewModel.UpdateProdQuantity(it.toDouble(), prodsResponse.id)
                    },
                   modifier = Modifier.customWidth(LocalConfiguration.current, 0.50f),
                    label = { Text(context.getString(
                        R.string.quantuty_en,
                        removeAllDigitExeptX(prodsResponse.sieze)
                    )) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(modifier = Modifier.padding(start = 9.dp))

                Icon(
                    modifier = Modifier.customWidth(LocalConfiguration.current, 0.10f).clickable {
                    productsViewModel.UpdateProdQuantity(prodsResponse.quantity + 1, prodsResponse.id)

                        if (prodsResponse.typesub == PRODUITS_FRAIS) {
                            productsViewModel.UpdateProdQuantity(String.format("%.3f", BigDecimal.valueOf(prodsResponse.quantity) + BigDecimal.valueOf(0.1)).toDouble(), prodsResponse.id)
                        } else {
                            productsViewModel.UpdateProdQuantity(prodsResponse.quantity + 1, prodsResponse.id)
                        }
                    },
                    imageVector =  Icons.Default.Add,
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
                                text = data.visuals.actionLabel?:"",
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

fun setSnackBarData(coroutineScope: CoroutineScope,  snackbarHostState: SnackbarHostState, prodsResponse: ProductModel, context: Context, productsViewModel: ProductsViewModel) {
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
