package com.amirami.simapp.priceindicatortunisia.ui.screens.courses

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDilogScreen
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeAllDigitExeptX
import com.amirami.simapp.priceindicatortunisia.utils.Functions.succesToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoppingScreen(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    shoppingViewModel: ShoppingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    if (productDetailDialogViewModel.prodDetailDialogVisibilityStates) {
        // LaunchedEffect(key1 = context) {
        scope.launch {
            modalBottomSheetState.show()
            productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(false)
        }
    }

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    ModalBottomSheetLayout(
        sheetContent = {
            ProductDetailDilogScreen(productDetailDialogViewModel)
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        // sheetBackgroundColor = colorResource(id = R.color.gray),
        // scrimColor = Color.Red,  // Color for the fade background when you open/close the drawer
    ) {
        Scaffold(
            topBar = { },
            scaffoldState = scaffoldState,
            bottomBar = {},
            /*
            //use it like this when there is no lambda trail
            content = {
                   ShowSnackBar(visibleState, snackBarMessage)
               },*/
            //    backgroundColor = colorResource(id = R.color.purple_500),
               snackbarHost = {
                   DefaultSnackbar(
                   scaffoldState.snackbarHostState,
                   onDismiss = {
                       scaffoldState.snackbarHostState.currentSnackbarData?.performAction()
                   },
                   modifier = Modifier
               )}
        ) { padding ->

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ProductShopList(scaffoldState, productsViewModel, productDetailDialogViewModel, shoppingViewModel = shoppingViewModel)


            }
        }
    }
}

@Composable
fun ProductShopList(
    scaffoldState: ScaffoldState,
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
                scaffoldState,
                productsViewModel.shopLists[position],
                productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                shoppingViewModel = shoppingViewModel
            )
        }
    }
}

@OptIn(InternalTextApi::class)
@Composable
fun ProductShopTiket(
    scaffoldState: ScaffoldState,
    prodsResponse: ProductModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productsViewModel: ProductsViewModel,
    shoppingViewModel: ShoppingViewModel
) {
    val context = LocalContext.current
    //  val (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            //  .fillMaxWidth()
            // .fillMaxHeight()
            .fillMaxWidth()
            .clickable {
                if (productDetailDialogViewModel.prodDetailDialogVisibilityStates) {
                    productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(false)
                } else productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(true)

                productDetailDialogViewModel.onprodDetailDialogStatesChanged(prodsResponse)
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
                    //  .fillMaxWidth()
                    // .fillMaxHeight()
                    .wrapContentWidth()
                // verticalAlignment = Alignment.CenterVertically,
                //   horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = context.getString(
                        R.string.Lunitéest,
                        removeAllDigitExeptX(prodsResponse.sieze)
                    )
                )

                RepeatingButton(
                    modifier = Modifier.width(32.dp),
                    onClick = {
                        //   shoppingViewModel.onquantityChange(shoppingViewModel.quantity-1)

                        if (prodsResponse.typesub == PRODUITS_FRAIS) {
                            if (prodsResponse.quantity - 0.1 <= 0.001) {
                                productsViewModel.DeleteProdFromShopList(prodsResponse.id)
                                setSnackBarData(coroutineScope=coroutineScope, scaffoldState=scaffoldState,prodsResponse= prodsResponse, context = context,productsViewModel = productsViewModel)                                //  errorToast(context = context, "delete")
                            } else productsViewModel.UpdateProdQuantity(String.format("%.3f", BigDecimal.valueOf(prodsResponse.quantity) - BigDecimal.valueOf(0.1)).toDouble(), prodsResponse.id)
                        } else {
                            if (prodsResponse.quantity - 1 < 1) {
                                setSnackBarData(coroutineScope=coroutineScope, scaffoldState=scaffoldState,prodsResponse= prodsResponse, context = context,productsViewModel = productsViewModel)                                //  errorToast(context = context, "delete")
                            } else productsViewModel.UpdateProdQuantity(prodsResponse.quantity - 1, prodsResponse.id)
                        }

                        //  shoppingViewModel.onquantityChange(prodsResponse.quantity-1)
                    },
                    enabled = true,
                    interactionSource = MutableInteractionSource(),
                    elevation = ButtonDefaults.elevation(),
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(3.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(),
                    contentPadding = ButtonDefaults.ContentPadding,
                    maxDelayMillis = 1000,
                    minDelayMillis = 5,
                    delayDecayFactor = .20f,
                    content = {}
                )
                Spacer(modifier = Modifier.padding(start = 9.dp))

             /*   OutlinedEditText( modifier = Modifier.width(55.dp),
                    text = shoppingViewModel.quantity.toString(),
                    R.string.quantite,
                    onValueChange = { shoppingViewModel.onquantityChange(it)})*/

               /* OutlinedTextField(
                    value = shoppingViewModel.quantity.toString(),
                    onValueChange = { shoppingViewModel.onquantityChange(it.toDouble()) },
                    modifier = Modifier.width(55.dp),
                    label = { Text(context.getString(R.string.Commentaires)) }
                )*/

                NumberTextInputComponent(
                    modifier = Modifier.width(55.dp),
                    productsViewModel = productsViewModel,
                    prodsResponse = prodsResponse
                )

                Spacer(modifier = Modifier.padding(start = 9.dp))
                RepeatingButton(
                    modifier = Modifier.width(32.dp),
                    onClick = {
                        // shoppingViewModel.onquantityChange(shoppingViewModel.quantity+1)
                        productsViewModel.UpdateProdQuantity(prodsResponse.quantity + 1, prodsResponse.id)

                        if (prodsResponse.typesub == PRODUITS_FRAIS) {
                            productsViewModel.UpdateProdQuantity(String.format("%.3f", BigDecimal.valueOf(prodsResponse.quantity) + BigDecimal.valueOf(0.1)).toDouble(), prodsResponse.id)
                        } else {
                            productsViewModel.UpdateProdQuantity(prodsResponse.quantity + 1, prodsResponse.id)
                        }
                        // shoppingViewModel.onquantityChange(prodsResponse.quantity+1)
                    },
                    enabled = true,
                    interactionSource = MutableInteractionSource(),
                    elevation = ButtonDefaults.elevation(),
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(3.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(),
                    contentPadding = ButtonDefaults.ContentPadding,
                    maxDelayMillis = 1000,
                    minDelayMillis = 5,
                    delayDecayFactor = .20f,
                    content = {}
                )
            }

            /*   TextWithIconString(
                   PricesAndLogos(context, prodsResponse)[0].first,
                   PricesAndLogos(
                       context,
                       prodsResponse
                   )[0].second
               )*/
        }

        //     Spacer(modifier = Modifier.padding(start = 12.dp))

      /*a  Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "",
            Modifier
                .weight(1f)
                // .size(35.dp)
                .clickable {
                    val productRoom = ProductModel(

                        prodsResponse.id,
                        prodsResponse.date,
                        prodsResponse.name,
                        prodsResponse.namearabe,
                        prodsResponse.marques,
                        prodsResponse.marquesarabe,
                        1.0,
                        prodsResponse.description,
                        prodsResponse.descriptionarabe,
                        prodsResponse.imageurl,
                        prodsResponse.type,
                        prodsResponse.typesub,
                        prodsResponse.typesubsub,
                        prodsResponse.sieze,
                        prodsResponse.monoprixprice,
                        prodsResponse.monoprixremarq,
                        prodsResponse.mgprice,
                        prodsResponse.mgremarq,
                        prodsResponse.carrefourprice,
                        prodsResponse.carrefourremarq,
                        prodsResponse.azzizaprice,
                        prodsResponse.azzizaremarq,
                        prodsResponse.geantprice,
                        prodsResponse.geantremarq,

                        prodsResponse.monoprixremarqmodifdate,
                        prodsResponse.mgremarqmodifdate,
                        prodsResponse.carrefourremarqmodifdate,
                        prodsResponse.azzizaremarqmodifdate,
                        prodsResponse.geantremarqmodifdate,

                        prodsResponse.monoprixmodifdate,
                        prodsResponse.mgmodifdate,
                        prodsResponse.carrefourmodifdate,
                        prodsResponse.azzizamodifdate,
                        prodsResponse.geantmodifdate,
                        prodsResponse.monoprixbonusfid,
                        prodsResponse.mgbonusfid,
                        prodsResponse.carrefourbonusfid,
                        prodsResponse.azzizabonusfid,
                        prodsResponse.geantbonusfid,

                        prodsResponse.monoprixbonusfidmodifdate,
                        prodsResponse.mgbonusfidmodifdate,
                        prodsResponse.carrefourbonusfidmodifdate,
                        prodsResponse.azzizabonusfidmodifdate,
                        prodsResponse.geantbonusfidmodifdate,

                        prodsResponse.monoprixPricHistory,
                        prodsResponse.mgpricHistory,
                        prodsResponse.azizaPricHistory,
                        prodsResponse.carrefourPricHistory,
                        prodsResponse.geantPricHistory
                    )
                    //  productsViewModel.AddProdToShopList(productRoom)
                    //  productsViewModel.deleteProd(prodsResponse.id)

                    productsViewModel.DeleteProdFromShopList(prodsResponse.id)
                }
        )


        NumberTextInputComponent(modifier = Modifier)
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "",
            Modifier
                .weight(1f)
                // .size(35.dp)
                .clickable {
                    val productRoom = ProductModel(

                        prodsResponse.id,
                        prodsResponse.date,
                        prodsResponse.name,
                        prodsResponse.namearabe,
                        prodsResponse.marques,
                        prodsResponse.marquesarabe,
                        1.0,
                        prodsResponse.description,
                        prodsResponse.descriptionarabe,
                        prodsResponse.imageurl,
                        prodsResponse.type,
                        prodsResponse.typesub,
                        prodsResponse.typesubsub,
                        prodsResponse.sieze,
                        prodsResponse.monoprixprice,
                        prodsResponse.monoprixremarq,
                        prodsResponse.mgprice,
                        prodsResponse.mgremarq,
                        prodsResponse.carrefourprice,
                        prodsResponse.carrefourremarq,
                        prodsResponse.azzizaprice,
                        prodsResponse.azzizaremarq,
                        prodsResponse.geantprice,
                        prodsResponse.geantremarq,

                        prodsResponse.monoprixremarqmodifdate,
                        prodsResponse.mgremarqmodifdate,
                        prodsResponse.carrefourremarqmodifdate,
                        prodsResponse.azzizaremarqmodifdate,
                        prodsResponse.geantremarqmodifdate,

                        prodsResponse.monoprixmodifdate,
                        prodsResponse.mgmodifdate,
                        prodsResponse.carrefourmodifdate,
                        prodsResponse.azzizamodifdate,
                        prodsResponse.geantmodifdate,
                        prodsResponse.monoprixbonusfid,
                        prodsResponse.mgbonusfid,
                        prodsResponse.carrefourbonusfid,
                        prodsResponse.azzizabonusfid,
                        prodsResponse.geantbonusfid,

                        prodsResponse.monoprixbonusfidmodifdate,
                        prodsResponse.mgbonusfidmodifdate,
                        prodsResponse.carrefourbonusfidmodifdate,
                        prodsResponse.azzizabonusfidmodifdate,
                        prodsResponse.geantbonusfidmodifdate,

                        prodsResponse.monoprixPricHistory,
                        prodsResponse.mgpricHistory,
                        prodsResponse.azizaPricHistory,
                        prodsResponse.carrefourPricHistory,
                        prodsResponse.geantPricHistory
                    )
                    //  productsViewModel.AddProdToShopList(productRoom)
                    //  productsViewModel.deleteProd(prodsResponse.id)

                    productsViewModel.DeleteProdFromShopList(prodsResponse.id)
                }
        )*/

        //  Spacer(Modifier.weight(1f))
    }
}

@InternalTextApi
@Composable
fun NumberTextInputComponent(
    modifier: Modifier,
    productsViewModel: ProductsViewModel,
    prodsResponse: ProductModel
) {
    Surface(color = Color.Transparent, modifier = modifier) {
        val context = LocalContext.current
       /*
        var textValue by remember { mutableStateOf(TextFieldValue("123")) }
        BasicTextField(
            value = textValue,
            modifier = Modifier
                .padding(16.dp),
            // Setting the keyboard type allows you to configure what kind of data you can input
            // in this TextInput. Some examples are number, phone, email, password, etc.
            // Update value of textValue with the latest value of the text field
            onValueChange = {
               textValue = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )*/

        OutlinedTextField(
            value = prodsResponse.quantity.toString(), // shoppingViewModel.quantity.toString(),
            onValueChange = {
                // shoppingViewModel.onquantityChange(it.toDouble())

                productsViewModel.UpdateProdQuantity(it.toDouble(), prodsResponse.id)
            },
            modifier = modifier,
            label = { Text(context.getString(R.string.Commentaires)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Composable
fun RepeatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    maxDelayMillis: Long = 1000,
    minDelayMillis: Long = 5,
    delayDecayFactor: Float = .20f,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier.repeatingClickable(
            interactionSource = interactionSource,
            enabled = enabled,
            maxDelayMillis = maxDelayMillis,
            minDelayMillis = minDelayMillis,
            delayDecayFactor = delayDecayFactor
        ) { onClick() },
        onClick = {},
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}

fun Modifier.repeatingClickable(
    interactionSource: InteractionSource,
    enabled: Boolean,
    maxDelayMillis: Long = 1000,
    minDelayMillis: Long = 5,
    delayDecayFactor: Float = .20f,
    onClick: () -> Unit
): Modifier = composed {
    val currentClickListener by rememberUpdatedState(onClick)

    pointerInput(interactionSource, enabled) {
        forEachGesture {
            coroutineScope {
                awaitPointerEventScope {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val heldButtonJob = launch {
                        var currentDelayMillis = maxDelayMillis
                        while (enabled && down.pressed) {
                            currentClickListener()
                            delay(currentDelayMillis)
                            val nextMillis = currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                            currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelayMillis)
                        }
                    }
                    waitForUpOrCancellation()
                    heldButtonJob.cancel()
                }
            }
        }
    }
}

@Composable
fun SnackbarDemo(scaffoldState: ScaffoldState) {
    // val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Button(onClick = {
            coroutineScope.launch {
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "This is your message",
                    actionLabel = "Do something"
                )
                when (snackbarResult) {
                    SnackbarResult.Dismissed -> TODO()
                    SnackbarResult.ActionPerformed -> TODO()
                }
            }
        }) {
            Text(text = "Click me!")
        }
    }
}

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = actionLabel,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                },
            //    contentColor = Color.Blue,
            //    backgroundColor = Color.Black
            )
        },
        modifier = modifier
            .padding(0.dp,0.dp,0.dp,50.dp)
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

fun setSnackBarData(coroutineScope: CoroutineScope, scaffoldState: ScaffoldState,prodsResponse: ProductModel, context :Context,productsViewModel: ProductsViewModel){
    coroutineScope.launch {
        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
            message = "Delete product : " + prodsResponse.name,
            actionLabel = "Delete",
            duration = SnackbarDuration.Short
        )
        when (snackbarResult) {
            SnackbarResult.Dismissed -> succesToast(context, "not deleted")
            SnackbarResult.ActionPerformed -> productsViewModel.DeleteProdFromShopList(prodsResponse.id)
        }
    }

}

