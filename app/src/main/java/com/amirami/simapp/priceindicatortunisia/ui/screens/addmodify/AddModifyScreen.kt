package com.amirami.simapp.priceindicatortunisia.ui.screens.addmodify

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes.ProductTypesDialogScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes.ProductTypesDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchView
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchViewModel
import com.amirami.simapp.priceindicatortunisia.ui.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.utils.Constants
import com.amirami.simapp.priceindicatortunisia.utils.Converters
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddModifyScreen(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    // productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel = hiltViewModel(),
    productNameViewModel: ProductNameViewModel = hiltViewModel(),
    addModifyViewModel: AddModifyViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productTypesDialogViewModel: ProductTypesDialogViewModel = hiltViewModel()

) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
        animationSpec = SpringSpec(stiffness = 10f)
    )



    if (productTypesDialogViewModel.prodTypesDialogVisibilityStates) {
        LaunchedEffect(key1 = context) {
            scope.launch {
                modalBottomSheetState.show()
                productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(false)
            }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            ProductTypesDialogScreen(productTypesDialogViewModel, navController, addModifyViewModel = addModifyViewModel)
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
                //  productsViewModel = productsViewModel,
                searchViewModel = searchViewModel,
                productNameViewModel = productNameViewModel,
                addModifyViewModel = addModifyViewModel,
                productDetailDialogViewModel = productDetailDialogViewModel,
                productTypesDialogViewModel = productTypesDialogViewModel
            )
        }
    }
}

@Composable
fun AddModifyScreenContent(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel,
    productNameViewModel: ProductNameViewModel,
    addModifyViewModel: AddModifyViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productTypesDialogViewModel: ProductTypesDialogViewModel
) {
    val context = LocalContext.current
    val prodDetailDialogStates = productDetailDialogViewModel.prodDetailDialogStates

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
                .padding(9.dp, 0.dp, 9.dp, 0.dp)
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
                        navController = navController,
                        productModel = prodDetailDialogStates,
                        addModifyViewModel = addModifyViewModel,
                        productTypesDialogViewModel = productTypesDialogViewModel
                    )

                   /* ScreenContent(
                        productModel = ProductModel(),
                        addModifyViewModel = addModifyViewModel
                    )*/
                }

                is Response.Loading -> ProgressBar()

                is Response.Success -> {
                    // Text(productIndex.toString() + "data size" + prodsResponse.data.size.toString())
                    ScreenContent(
                        navController = navController,
                        productModel = prodsResponse.data[0],
                        addModifyViewModel = addModifyViewModel,
                        productTypesDialogViewModel = productTypesDialogViewModel
                    )
                }

                is Response.Error -> Functions.errorToast(context, prodsResponse.message)
            }
        }
    }
}

@Composable
fun ScreenContent(
    navController: NavHostController,
    productModel: ProductModel,
    addModifyViewModel: AddModifyViewModel,
    productTypesDialogViewModel: ProductTypesDialogViewModel
) {
    val context = LocalContext.current

    EditTextInputComponent(
        modifier = Modifier.fillMaxWidth(),
        if (addModifyViewModel.barcodeTextValue != "") addModifyViewModel.barcodeTextValue else productModel.id,
        R.string.ScanproductIdoradditmanually,
        onValueChange = {
            addModifyViewModel.onbarcodeTextValue(it)
        },
        readOnly = true,
        enabled = false
    )

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
        modifier = Modifier.fillMaxWidth(),
        if (addModifyViewModel.nomProduitTextValue != "") addModifyViewModel.nomProduitTextValue else productModel.name,
        R.string.Nomduproduit,
        onValueChange = {
            productModel.name = it
            addModifyViewModel.onNomProduitTextValue(it)
        }
    )

    EditTextInputComponent(
        modifier = Modifier.fillMaxWidth(),
        if (addModifyViewModel.nomProduitArabeTextValue != "") addModifyViewModel.nomProduitArabeTextValue else productModel.namearabe,
        R.string.NomduproduitenArabe,
        onValueChange = {
            productModel.namearabe = it
            addModifyViewModel.onNomProduitTextArabeValue(it)
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
        // .background(Color.White)
        // verticalAlignment = Arrangement.Top,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextInputComponent(
            modifier = Modifier.weight(weight = 1f, fill = true),
            if (addModifyViewModel.marquesTextValue != "") addModifyViewModel.marquesTextValue else productModel.marques,
            R.string.marques,
            onValueChange = {
                productModel.marques = it
                addModifyViewModel.onMarquesTextValue(it)
            }
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))

        EditTextInputComponent(
            modifier = Modifier.weight(weight = 1f, fill = true),
            if (addModifyViewModel.marquesArabeTextValue != "") addModifyViewModel.marquesArabeTextValue else productModel.marquesarabe,
            R.string.marquesenArabe,
            onValueChange = {
                productModel.marquesarabe = it
                addModifyViewModel.onMarquesArabeTextValue(it)
            }
        )
    }

    EditTextInputComponent(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                if (productTypesDialogViewModel.prodTypesDialogVisibilityStates) {
                    productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(false)
                } else productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(true)

                productTypesDialogViewModel.onprodTypesDialogStatesChanged(title = context.getString(R.string.Catégories), context.getString(R.string.Tous_les_Catégories))
            },
        returnProdType(addModifyViewModel, productModel),
        R.string.Catégories,
        onValueChange = {
            addModifyViewModel.onprodTypeTextValue(it)
        },
        readOnly = false,
        enabled = false
    )

    EditTextInputComponent(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                if (productTypesDialogViewModel.prodTypesDialogVisibilityStates) {
                    productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(false)
                } else productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(true)

                productTypesDialogViewModel.onprodTypesDialogStatesChanged(title = context.getString(R.string.SousCatégories), returnProdType(addModifyViewModel, productModel))
            },
        returnProdSubType(addModifyViewModel, productModel),
        R.string.SousCatégories,
        onValueChange = {
            addModifyViewModel.onprodSubTypeTextValue(it)
        },
        readOnly = false,
        enabled = false
    )

    EditTextInputComponent(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                if (productTypesDialogViewModel.prodTypesDialogVisibilityStates) {
                    productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(false)
                } else productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(true)

                productTypesDialogViewModel.onprodTypesDialogStatesChanged(title = context.getString(R.string.sousousCatégories), returnProdSubType(addModifyViewModel, productModel))
            },
        returnProdSubSubType(addModifyViewModel, productModel),
        R.string.sousousCatégories,
        onValueChange = {
            addModifyViewModel.onprodSubSubTypeTextValue(it)
        },
        readOnly = false,
        enabled = false
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
        // .background(Color.White)
        // verticalAlignment = Arrangement.Top,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextInputComponent(
            modifier = Modifier.weight(weight = 1f, fill = true),
            if (addModifyViewModel.siezeTextValue != "") addModifyViewModel.siezeTextValue else Functions.replacesiez(productModel.sieze),
            R.string.Volumepoidproduit,
            onValueChange = {
                addModifyViewModel.onSiezeTextValue(it)
            },
            keyboardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))

        DropDownMenu(
            items = addModifyViewModel.mesureUnitList,
            // selectedItem = addModifyViewModel.unitselected.value,
            selectedItem = if (addModifyViewModel.unitselected.value != "") addModifyViewModel.unitselected.value else Functions.removeAllDigitExeptX(productModel.sieze),
            onValueChange = {
                //  productModel.sieze = Functions.replacesiez(productModel.sieze) +Functions.removeAllDigitExeptX(it)

                addModifyViewModel.unitselected.value = it
            },
            stringId = R.string.Unite,
            modifier = Modifier.weight(weight = 1f, fill = true)
        )

        /*   EditTextInputComponent(
               modifier = Modifier.weight(weight = 1f, fill = true),
               if (addModifyViewModel.unitTextValue != "") addModifyViewModel.unitTextValue else Functions.removeAllDigitExeptX(productModel.sieze),
               R.string.Unite,
               onValueChange = {
                   addModifyViewModel.onUnitTextValue(it)
               }
           )*/
    }

    EditTextInputComponent(
        modifier = Modifier.fillMaxWidth(),
        if (addModifyViewModel.imageLinkTextValue != "") addModifyViewModel.imageLinkTextValue else productModel.imageurl,
        R.string.Liendimage,
        onValueChange = {
            productModel.imageurl = it
            addModifyViewModel.onimageLinkTextValue(it)
        },
        keyboardType = KeyboardType.Uri
    )

    EditTextInputComponent(
        modifier = Modifier.fillMaxWidth(),
        if (addModifyViewModel.prodDescriptionTextValue != "") addModifyViewModel.prodDescriptionTextValue else productModel.description,
        R.string.Descriptionduproduit,
        onValueChange = {
            productModel.description = it
            addModifyViewModel.onprodDescriptionTextValue(it)
        }
    )

    EditTextInputComponent(
        modifier = Modifier.fillMaxWidth(),
        if (addModifyViewModel.prodDescriptionARTextValue != "") addModifyViewModel.prodDescriptionARTextValue else productModel.descriptionarabe,
        R.string.DescriptionduproduitenArabe,
        onValueChange = {
            productModel.descriptionarabe = it
            addModifyViewModel.onprodDescriptionARTextValue(it)
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
        // .background(Color.White)
        // verticalAlignment = Arrangement.Top,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextInputComponent(
            modifier = Modifier.weight(weight = 1f, fill = true),
            if (addModifyViewModel.monoprixPriceTextValue != "") addModifyViewModel.monoprixPriceTextValue else productModel.monoprixprice,
            R.string.monoprix_prix,
            onValueChange = {
                productModel.monoprixprice = it
                addModifyViewModel.onmonoprixPriceTextValue(it)
            }
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))

        EditTextInputComponent(
            modifier = Modifier.weight(weight = 2f, fill = true)
                .clickable {
                    addModifyViewModel.onmonoprixPromotionTextValue(productModel.monoprixremarq)
                    addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_monoprix))
                    addModifyViewModel.onMonoprixBonusSurCartFidTextValue(productModel.monoprixbonusfid)
                    navController.navigate(ListScreens.PriceRemarques.Route)
                },
            if (addModifyViewModel.monoprixPromotionTextValue != "") addModifyViewModel.monoprixPromotionTextValue else productModel.monoprixremarq,
            R.string.promotion_monoprix,
            onValueChange = {
                productModel.monoprixremarq = it
                addModifyViewModel.onmonoprixPromotionTextValue(it)
            },
            readOnly = false,
            enabled = false
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
        // .background(Color.White)
        // verticalAlignment = Arrangement.Top,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextInputComponent(
            modifier = Modifier.weight(weight = 1f, fill = true),
            if (addModifyViewModel.mgPriceTextValue != "") addModifyViewModel.mgPriceTextValue else productModel.mgprice,
            R.string.mg_prix,
            onValueChange = {
                productModel.mgprice = it
                addModifyViewModel.onMgPriceTextValue(it)
            }
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))

        EditTextInputComponent(
            modifier = Modifier.weight(weight = 2f, fill = true)
                .clickable {
                    addModifyViewModel.onMgPromotionTextValue(productModel.mgremarq)
                    addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_mg))
                    addModifyViewModel.onMgBonusSurCartFidTextValue(productModel.mgbonusfid)
                    navController.navigate(ListScreens.PriceRemarques.Route)
                },
            if (addModifyViewModel.mgPromotionTextValue != "") addModifyViewModel.mgPromotionTextValue else productModel.mgremarq,
            R.string.promotion_mg,
            onValueChange = {
                productModel.mgremarq = it
                addModifyViewModel.onMgPromotionTextValue(it)
            },
            readOnly = false,
            enabled = false
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
        // .background(Color.White)
        // verticalAlignment = Arrangement.Top,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextInputComponent(
            modifier = Modifier.weight(weight = 1f, fill = true),
            if (addModifyViewModel.carrefourPriceTextValue != "") addModifyViewModel.carrefourPriceTextValue else productModel.carrefourprice,
            R.string.carrefour_prix,
            onValueChange = {
                productModel.carrefourprice = it
                addModifyViewModel.onCarrefourPriceTextValue(it)
            }
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))

        EditTextInputComponent(
            modifier = Modifier.weight(weight = 2f, fill = true)
                .clickable {
                    addModifyViewModel.onCarrefouPromotionTextValue(productModel.carrefourremarq)
                    addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_carrefour))
                    addModifyViewModel.onCarrefouBonusSurCartFidTextValue(productModel.carrefourbonusfid)
                    navController.navigate(ListScreens.PriceRemarques.Route)
                },
            if (addModifyViewModel.carrefourPromotionTextValue != "") addModifyViewModel.carrefourPromotionTextValue else productModel.carrefourremarq,
            R.string.promotion_carrefour,
            onValueChange = {
                productModel.carrefourremarq = it
                addModifyViewModel.onCarrefouPromotionTextValue(it)
            },
            readOnly = false,
            enabled = false
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
        // .background(Color.White)
        // verticalAlignment = Arrangement.Top,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextInputComponent(
            modifier = Modifier.weight(weight = 1f, fill = true),
            if (addModifyViewModel.azizaPriceTextValue != "") addModifyViewModel.azizaPriceTextValue else productModel.azzizaprice,
            R.string.azziza_prix,
            onValueChange = {
                productModel.azzizaprice = it
                addModifyViewModel.onAzizaPriceTextValue(it)
            }
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))

        EditTextInputComponent(
            modifier = Modifier.weight(weight = 2f, fill = true)
                .clickable {
                addModifyViewModel.onAzizaPromotionTextValue(productModel.azzizaremarq)
                addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_azziza))
                addModifyViewModel.onAzizaBonusSurCartFidTextValue(productModel.azzizabonusfid)
                navController.navigate(ListScreens.PriceRemarques.Route)
            },
            if (addModifyViewModel.azizaPromotionTextValue != "") addModifyViewModel.azizaPromotionTextValue else productModel.azzizaremarq,
            R.string.promotion_azziza,
            onValueChange = {
                productModel.azzizaremarq = it
                addModifyViewModel.onAzizaPromotionTextValue(it)
            },
            readOnly = false,
            enabled = false
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
        // .background(Color.White)
        // verticalAlignment = Arrangement.Top,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextInputComponent(
            modifier = Modifier.weight(weight = 1f, fill = true),
            if (addModifyViewModel.geantPriceTextValue != "") addModifyViewModel.geantPriceTextValue else productModel.geantprice,
            R.string.Géant_prix,
            onValueChange = {
                productModel.geantprice = it
                addModifyViewModel.onGeantPriceTextValue(it)
            }
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))

        EditTextInputComponent(
            modifier = Modifier.weight(weight = 2f, fill = true)
                .clickable {
                    addModifyViewModel.onGeantPromotionTextValue(productModel.geantremarq)
                    addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_Géant))
                    addModifyViewModel.onGeantBonusSurCartFidTextValue(productModel.geantbonusfid)
                    navController.navigate(ListScreens.PriceRemarques.Route)
                },
            if (addModifyViewModel.geantPromotionTextValue != "") addModifyViewModel.geantPromotionTextValue else productModel.geantremarq,
            R.string.promotion_Géant,
            onValueChange = {
                productModel.geantremarq = it
                addModifyViewModel.onGeantPromotionTextValue(it)
            },
            readOnly = false,
            enabled = false
        )
    }

    Spacer(modifier = Modifier.padding(bottom = 12.dp))
}

@Composable
fun EditTextInputComponent(
    modifier: Modifier,
    text: String,
    stringId: Int,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Surface(color = Color.Transparent, modifier = modifier) {
        val context = LocalContext.current
        var isFocused by rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(
            value = text, // shoppingViewModel.quantity.toString(),
            onValueChange = {
                onValueChange(it)
            },
            readOnly = readOnly,
            enabled = enabled,
            modifier = modifier.onFocusChanged { focusState ->
                isFocused = when {
                    focusState.isFocused -> //     println("I'm focused!")
                        true

                    focusState.hasFocus -> // println("A child of mine has focus!")
                        true

                    focusState.isCaptured -> false
                    else -> false
                }
            },
            label = { Text(context.getString(stringId), fontSize = 12.sp, maxLines = 1) },
            trailingIcon = {
                if (isFocused) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(id = R.string.deleteshopinglist)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            )
        )
    }
}

@Composable
fun DropDownMenu(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedItem: String,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    stringId: Int,
    dropdownLabel: String = "",
    onValueChange: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var isFocused by rememberSaveable { mutableStateOf(false) }
    /*  Box (modifier = modifier
         // .fillMaxWidth()
         // .padding(4.dp)
      ){
          Column {
              if (dropdownLabel.isNotEmpty()){
                  Text(dropdownLabel)
              }
              TextField(
                  value = selectedItem,
                  onValueChange = onValueChange,
                  trailingIcon = {
                      Icon(
                        if(!expanded)  Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                          contentDescription = null
                      )
                  },
                  modifier = Modifier.fillMaxWidth()
              )

          }
          Spacer(modifier = Modifier
              .matchParentSize()
             // .background(Color.Transparent)
              .clickable { expanded = true })
          DropdownMenu(
              expanded = expanded,
              onDismissRequest = { expanded = false }
          ) {
              items.forEach {
                      item -> DropdownMenuItem(
                  onClick = {
                      onValueChange(item)
                      expanded = false
                  },
              ) {
                  Text(text = item)
              }
              }
          }
      }


  */

    Box(
        modifier = modifier
        // .fillMaxWidth()
        // .padding(4.dp)
    ) {
        Column {
            if (dropdownLabel.isNotEmpty()) {
                Text(dropdownLabel)
            }

            Surface(color = Color.Transparent, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedItem, // shoppingViewModel.quantity.toString(),
                    onValueChange = onValueChange,
                    readOnly = readOnly,
                    enabled = enabled,
                    modifier = modifier.onFocusChanged { focusState ->
                        isFocused = when {
                            focusState.isFocused -> //     println("I'm focused!")
                                true

                            focusState.hasFocus -> // println("A child of mine has focus!")
                                true

                            focusState.isCaptured -> false
                            else -> false
                        }
                    }
                        .clickable { expanded = true },
                    label = { Text(context.getString(stringId), fontSize = 12.sp, maxLines = 1) },
                    trailingIcon = {
                        Icon(
                            if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
            }
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                // .background(Color.Transparent)
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(item)
                        expanded = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        androidx.compose.material3.Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error
        )
    }
}

fun returnProdType(addModifyViewModel: AddModifyViewModel, productModel: ProductModel): String {
    return if (addModifyViewModel.prodTypeTextValue != "") {
        productModel.type = addModifyViewModel.prodTypeTextValue
        addModifyViewModel.prodTypeTextValue
    } else productModel.type
}

fun returnProdSubType(addModifyViewModel: AddModifyViewModel, productModel: ProductModel): String {
    return if (addModifyViewModel.prodSubTypeTextValue != "") {
        productModel.typesub = addModifyViewModel.prodSubTypeTextValue
        addModifyViewModel.prodSubTypeTextValue
    } else productModel.typesub
}

fun returnProdSubSubType(addModifyViewModel: AddModifyViewModel, productModel: ProductModel): String {
    return if (addModifyViewModel.prodSubSubTypeTextValue != "") {
        productModel.typesubsub = addModifyViewModel.prodSubSubTypeTextValue
        addModifyViewModel.prodSubSubTypeTextValue
    } else productModel.typesubsub
}

/*  navController.backQueue.forEach { entry ->
                 Log.d("TAcxG", "${entry.destination.route}")

             }

             if (navController.backQueue[navController.backQueue.size - 2].destination.route == ListScreens.Courses.Route) {
             }*/
