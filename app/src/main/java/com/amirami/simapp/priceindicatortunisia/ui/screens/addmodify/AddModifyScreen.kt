package com.amirami.simapp.priceindicatortunisia.ui.screens.addmodify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.productsnames.ProductNameViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.LottieComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ProgressBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.productinfodialog.ProductDetailDilogScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchView
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Constants
import com.amirami.simapp.priceindicatortunisia.utils.Converters
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddModifyScreen(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel = hiltViewModel(),
    productNameViewModel: ProductNameViewModel = hiltViewModel(),
    // addModifyViewModel: AddModifyViewModel = hiltViewModel(),
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
            ProductDetailDilogScreen(productDetailDialogViewModel, navController)
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

            AddModifyScreenContent(
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                productsViewModel = productsViewModel,
                searchViewModel = searchViewModel,
                productNameViewModel = productNameViewModel,
                // addModifyViewModel = addModifyViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel
            )
        }
    }
}

@Composable
fun AddModifyScreenContent(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel,
    productNameViewModel: ProductNameViewModel,
    addModifyViewModel: AddModifyViewModel = hiltViewModel(),
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

            if (productNameViewModel.message == Constants.ERREUR_CONNECTION) {
                LottieComposable(
                    250.dp,
                    R.raw.no_internet_connection
                )
            }

            when (val prodsResponse = productsViewModel.prodResponse) {
                is Response.NotInit -> {
                    ScreenContent(
                        productModel = ProductModel(),
                        addModifyViewModel = addModifyViewModel
                    )
                }
                is Response.Loading -> ProgressBar()
                is Response.Success -> {
                    ScreenContent(
                        productModel = prodsResponse.data[0],
                        addModifyViewModel
                    )
                }
                is Response.Error -> Functions.errorToast(context, prodsResponse.message)
            }
        }
    }
}

@Composable
fun ScreenContent(
    productModel: ProductModel,
    // text: String,
    addModifyViewModel: AddModifyViewModel
) {
    SubcomposeAsyncImage(
        modifier = Modifier
            //  .weight(2f)
            .size(85.dp)
            .clip(RoundedCornerShape(6.dp)),
        model = if (productModel.imageurl == "") R.drawable.ic_photo else productModel.imageurl,
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

    EditTextInputComponent(
        modifier = Modifier,
        if (addModifyViewModel.editTextValue != "") addModifyViewModel.editTextValue else productModel.name,
        R.string.Commentaires,
        onValueChange = {
            addModifyViewModel.onEditTextValue(it)
        }
    )
}

@Composable
fun EditTextInputComponent(
    modifier: Modifier,
    text: String,
    stringId : Int,
    onValueChange: (String) -> Unit
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
            value = text, // shoppingViewModel.quantity.toString(),
            onValueChange = {
                onValueChange(it)
            },
            modifier = modifier,
            label = { Text(context.getString(stringId)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
    }
}
