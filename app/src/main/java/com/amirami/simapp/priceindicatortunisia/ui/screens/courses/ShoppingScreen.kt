package com.amirami.simapp.priceindicatortunisia.ui.screens.courses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDilogScreen
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeAllDigitExeptX
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoppingScreen(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel
) {
    val scope = rememberCoroutineScope()

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    if (productDetailDialogViewModel.prodDetailDialogVisibilityStates) {
        // LaunchedEffect(key1 = context) {
        scope.launch {
            modalBottomSheetState.show()
            productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(false)
        }
    }

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
            /*
            //use it like this when there is no lambda trail
            content = {
                   ShowSnackBar(visibleState, snackBarMessage)
               },*/
            //    backgroundColor = colorResource(id = R.color.purple_500),
            snackbarHost = {}
        ) { padding ->

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ProductShopList(productsViewModel, productDetailDialogViewModel)
            }
        }
    }
}

@Composable
fun ProductShopList(
    productsViewModel: ProductsViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .height(500.dp)
            // .fillMaxWidth()
            .padding(bottom = 60.dp),
        //   .fillMaxHeight()
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(productsViewModel.shopLists.size) { position ->

            ProductShopTiket(
                productsViewModel.shopLists[position],
                productsViewModel = productsViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel
            )
        }
    }
}

@OptIn(InternalTextApi::class)
@Composable
fun ProductShopTiket(
    prodsResponse: ProductModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productsViewModel: ProductsViewModel
) {
    val context = LocalContext.current
    val (count, updateCount) = remember { mutableStateOf(1) }
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

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = context.getString(
                    R.string.Lunitéest,
                    removeAllDigitExeptX(prodsResponse.sieze)
                )
            )

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

      /*  QuantitySelector(
            count = count,
            decreaseItemCount = { if (count > 0) updateCount(count - 1) },
            increaseItemCount = { updateCount(count + 1) }
        )*/
        //  Spacer(Modifier.weight(1f))
    }
}

@InternalTextApi
@Composable
fun NumberTextInputComponent(modifier: Modifier) {
    Surface(color = Color.LightGray, modifier = modifier) {
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
        )
    }
}
