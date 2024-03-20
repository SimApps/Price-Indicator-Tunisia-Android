@file:OptIn(ExperimentalMaterial3Api::class)

package com.amirami.simapp.priceindicatortunisia.addmodify

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.productsnames.ProductNameViewModel
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.ProductName
import com.amirami.simapp.priceindicatortunisia.ui.CustomModifiers.customWidth
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ClickableEditTextField
import com.amirami.simapp.priceindicatortunisia.ui.componenet.CustomAlertDialogue
import com.amirami.simapp.priceindicatortunisia.ui.componenet.CustomDropdownMenu
import com.amirami.simapp.priceindicatortunisia.ui.componenet.EditTextField
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ProgressBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes.ProductTypesDialogScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes.ProductTypesDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchView
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.SearchViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.capitalizeWords
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getuserid
import com.amirami.simapp.priceindicatortunisia.utils.Functions.replacesiez
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddModifyScreen(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    // productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel = hiltViewModel(),
    productNameViewModel: ProductNameViewModel = hiltViewModel(),
    addModifyViewModel: AddModifyViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productTypesDialogViewModel: ProductTypesDialogViewModel,
    productsViewModel: ProductsViewModel

) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    val currentProduct = productsViewModel.selectedProductStates//addModifyViewModel.currentProduct


    val productName= currentProduct.name.capitalizeWords()


val productLocalNamesList = productNameViewModel.productLocalNamesList
    if (productTypesDialogViewModel.prodTypesDialogVisibilityStates) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
                productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(false)

            },
        ) {
            ProductTypesDialogScreen(
                productTypesDialogViewModel = productTypesDialogViewModel,
                navController = navController,
                addModifyViewModel = addModifyViewModel
            )

        }
    }


    LaunchedEffect(key1 = barCodeViewModel.fidCardBarCodeInfo.value){

        if (barCodeViewModel.fidCardBarCodeInfo.value != "") {
            val barecodeValue = if (Functions.isNumber(barCodeViewModel.fidCardBarCodeInfo.value)) {
                Functions.removeLeadingZeroes(barCodeViewModel.fidCardBarCodeInfo.value)
            } else barCodeViewModel.fidCardBarCodeInfo.value
            barecodeValue.let {
                searchViewModel.onsearchValue(it)
                productsViewModel.getProds(Functions.searchType(it), it.capitalizeWords(),from = "add mod")

                val fidcard =
                    FidCardEntity(name = "", value = "", barecodeformat = -1, barecodetype = -1)
                barCodeViewModel.onfidCardInfo(fidcard)
            }
        }
    }


    when(val addBookResponse = productsViewModel.deleteProdResponse) {
        is Response.NotInit -> {
            Text(text = "NOT INIT DELETE")
        }
        is Response.Loading -> {
            ProgressBar()
            Text(text = "DELETE LOAD")
        }
        is Response.Success -> {
            Log.d("tyhuji",productLocalNamesList.size.toString())
            productNameViewModel.deleteLocalProdName(currentProduct.id)


            LaunchedEffect(key1 = productLocalNamesList.size){
                Log.d("tyhuji","n " +productLocalNamesList.size.toString())
                productsViewModel.updateRemoteListProductsNamesBareCode(productLocalNamesList.associateBy({ it.id }, { it.name!! }))

            }



            Text(text = "DELETE Succ")
        }
        is Response.Failure -> {
            Text(text = addBookResponse.message)
        }
    }

    when(val addBookResponse = productsViewModel.addProdResponse) {
        is Response.NotInit -> {
            Text(text = "NOT INIT")
        }
        is Response.Loading -> {
            ProgressBar()
            Text(text = "Add Modify prod")
        }
        is Response.Success -> {

            productNameViewModel.addLocalProdNames(
                ProductName(
                    id = currentProduct.id,
                    name = productName
                )
            )



                LaunchedEffect(key1 = productLocalNamesList){
                    productsViewModel.updateRemoteListProductsNamesBareCode(productLocalNamesList.associateBy({ it.id }, { it.name!! }))
                }


            Text(text = "Add Modify Succ")
        }
        is Response.Failure -> {
            Text(text = addBookResponse.message)
        }
    }


    when(val booksResponse = productsViewModel.getProductsResponse) {
        is Response.NotInit ->{

        }
        is Response.Loading -> ProgressBar()
        is Response.Success ->{
            LaunchedEffect(key1 = booksResponse.data.first().id){
                productsViewModel.onSelectedProductChanged(booksResponse.data.first())
            }


        }

        is Response.Failure -> {
            Functions.errorToast(context,booksResponse.message)
            if(booksResponse.message.contains("Code Barre"))
                productsViewModel.onShowAddNewProductChange(true)

            productsViewModel.resetGetProductsResponse()
        }
        else -> {}
    }


    //LaunchedEffect(key1 = productsViewModel.selectedProductStates){
    //    onSelectedProductChanged(productsViewModel.selectedProductStates)

   // }

    Scaffold(
        topBar = {
            if (productLocalNamesList.isNotEmpty()) {
                SearchView(
                    navController = navController,
                    barCodeViewModel = barCodeViewModel,
                    prodname = productLocalNamesList.map { it.name?:"N/A" },
                    productsViewModel = productsViewModel,
                    searchViewModel = searchViewModel
                )
            }
        },

        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        productsViewModel.onShowDeleteProductChange(true)

                    }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = ""
                    )
                }

                FloatingActionButton(
                    onClick = {

                        val product = ProductModel(
                            id = "1",
                            date = "",
                            name = "Test 1 V",
                            namearabe = "compose",
                            marques = "COMPOSE",
                            marquesarabe = "",
                            quantity = "2.0",
                            description = "",
                            descriptionarabe = "",
                            imageurl = "",
                            type = "Alimentation",
                            typesub = "Boissons",
                            typesubsub = "Eaux",
                            sieze = "ml",
                            monoprixprice = "",
                            monoprixremarq = "",
                            mgprice = "",
                            mgremarq = "",
                            carrefourprice = "",
                            carrefourremarq = "",
                            azzizaprice = "",
                            azzizaremarq = "",
                            geantprice = "",
                            geantremarq = "",
                            monoprixremarqmodifdate = "",
                            mgremarqmodifdate = "",
                            carrefourremarqmodifdate = "",
                            azzizaremarqmodifdate = "",
                            geantremarqmodifdate = "",
                            monoprixmodifdate = "",
                            mgmodifdate = "",
                            carrefourmodifdate = "",
                            azzizamodifdate = "",
                            geantmodifdate = "",
                            monoprixbonusfid = "",
                            mgbonusfid = "",
                            carrefourbonusfid = "",
                            azzizabonusfid = "",
                            geantbonusfid = "",
                            monoprixbonusfidmodifdate = "",
                            mgbonusfidmodifdate = "",
                            carrefourbonusfidmodifdate = "",
                            azzizabonusfidmodifdate = "",
                            geantbonusfidmodifdate = "",
                            monoprixPricHistory = "",
                            mgpricHistory = "",
                            azizaPricHistory = "",
                            carrefourPricHistory = "",
                            geantPricHistory = ""
                        )

                        //  product.userid = getuserid()



                        currentProduct.userid =getuserid()

                        //  if(productsViewModel.selectedProductStates.id == prod.id && productsViewModel.selectedProductStates != prod)
                        productsViewModel.addProductRemote(product = currentProduct)
                    }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = ""
                    )
                }
            }

        }

    ) { padding ->
        CustomAlertDialogue(
            title = context.getString(R.string.Voulez_vous_suprimer_ce_produit),
            msg = searchViewModel.searchValue.capitalizeWords(),
            openDialog  = productsViewModel.showDeleteProduct,
            setDialogueVisibility = {

                productsViewModel.onShowDeleteProductChange(false)
            },
            customAction = {
                productsViewModel.deleteProdRemote(currentProduct.id)
            }
        )

        CustomAlertDialogue(
            title = context.getString(R.string.Voulez_vous_ajouter_produit),
            msg = searchViewModel.searchValue.capitalizeWords(),
            openDialog  = productsViewModel.showAddNewProduct,
            setDialogueVisibility = {
                productsViewModel.resetGetProductsResponse()
                productsViewModel.onShowAddNewProductChange(false)
            },
            customAction = {
                productsViewModel.resetGetProductsResponse()
                productsViewModel.onSelectedProductChanged(ProductModel(id = searchViewModel.searchValue))
             //   navController.navigate(ListScreens.AddModify.Route)
            }
        )




        AddModifyScreenContent(
            padding = padding,
            navController = navController,
            barCodeViewModel = barCodeViewModel,
            //  productsViewModel = productsViewModel,
            searchViewModel = searchViewModel,
            productNameViewModel = productNameViewModel,
            addModifyViewModel = addModifyViewModel,
            productDetailDialogViewModel = productDetailDialogViewModel,
            productTypesDialogViewModel = productTypesDialogViewModel,
            productsViewModel = productsViewModel,
            currentProduct = currentProduct,
        )

    }



}

@Composable
fun AddModifyScreenContent(
    padding: PaddingValues,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel,
    productNameViewModel: ProductNameViewModel,
    addModifyViewModel: AddModifyViewModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productTypesDialogViewModel: ProductTypesDialogViewModel,
    currentProduct: ProductModel,
) {
    val context = LocalContext.current



    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {




            Spacer(modifier = Modifier.padding(top = 15.dp))







            ScreenContent(
                navController = navController,
                addModifyViewModel = addModifyViewModel,
                productTypesDialogViewModel = productTypesDialogViewModel,
                currentProduct = currentProduct,
                onSelectedProductChanged = {
                    productsViewModel.onSelectedProductChanged(it)
                }
            )


    }
}

@Composable
fun ScreenContent(
    navController: NavHostController,
    addModifyViewModel: AddModifyViewModel,
    onSelectedProductChanged :(ProductModel) ->Unit,
    productTypesDialogViewModel: ProductTypesDialogViewModel,
    currentProduct: ProductModel
) {
    val context = LocalContext.current

    EditTextField(
        modifier = Modifier
            .fillMaxWidth(),
        text = currentProduct.id,
        errorvalue = null,
        label = stringResource(R.string.ScanproductIdoradditmanually),
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(id = it))
        },
        readOnly = true,
        enabled = false,
        showLeadingIcon = true,
        leadingIcon = Icons.Default.QrCodeScanner,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    )

    SubcomposeAsyncImage(
        modifier = Modifier
            //  .weight(2f)
            .size(85.dp)
            .clip(RoundedCornerShape(6.dp)),
        model = currentProduct.imageurl,
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

    EditTextField(
        modifier = Modifier
            .fillMaxWidth(),
        text = currentProduct.name,
        errorvalue = null,
        label = stringResource(R.string.Nomduproduit),
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(name = it.capitalizeWords()))
        },
        readOnly = false,
        enabled = true,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    )

    EditTextField(
        modifier = Modifier
            .fillMaxWidth(),
        text = currentProduct.namearabe,
        errorvalue = null,
        label = stringResource(R.string.NomduproduitenArabe),
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(namearabe = it))
                        },
        readOnly = false,
        enabled = true,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    )


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment= Alignment.CenterVertically,
    ) {
        EditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.5f),
            text = currentProduct.marques,
            errorvalue = null,
            label = stringResource(R.string.marques),
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(marques = it))
            },
            readOnly = false,
            enabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))
        EditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.45f),
            text = currentProduct.marquesarabe,
            errorvalue = null,
            label = stringResource(R.string.marquesenArabe),
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(marquesarabe = it))
            },
            readOnly = false,
            enabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )

    }

    ClickableEditTextField(
    text  = currentProduct.type,
    errorValue = null,
    label =  context.getString(R.string.Catégories),
    readOnly = true,
    onClick= {
        productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(!productTypesDialogViewModel.prodTypesDialogVisibilityStates)

        productTypesDialogViewModel.onprodTypesDialogStatesChanged(
            title = context.getString(
                R.string.Catégories
            ), content =  context.getString(R.string.Tous_les_Catégories)
        )
    },
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(type = it))

        }
    )


    ClickableEditTextField(
        text  = currentProduct.typesub,
        errorValue = null,
        label =  context.getString(R.string.SousCatégories),
        readOnly = true,
        onClick= {
            productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(!productTypesDialogViewModel.prodTypesDialogVisibilityStates)

            productTypesDialogViewModel.onprodTypesDialogStatesChanged(
                title = context.getString(
                    R.string.SousCatégories
                ), content =  currentProduct.type
            )
        },
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(typesub = it))

        }
    )


    ClickableEditTextField(
        text  = currentProduct.typesubsub,
        errorValue = null,
        label =  context.getString(R.string.sousousCatégories),
        readOnly = true,
        onClick= {
            productTypesDialogViewModel.onprodTypesDialogVisibilityStatesChanged(!productTypesDialogViewModel.prodTypesDialogVisibilityStates)

            productTypesDialogViewModel.onprodTypesDialogStatesChanged(
                title = context.getString(
                    R.string.sousousCatégories
                ), content = currentProduct.typesub
            )
        },
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(typesubsub = it))
        }
    )



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment= Alignment.CenterVertically,
    ) {
        EditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.5f),
            text = replacesiez(currentProduct.sieze),
            errorvalue = null,
            label = stringResource(R.string.Volumepoidproduit),
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(sieze = it))
            },
            readOnly = false,
            enabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.padding(end = 9.dp))

     if(replacesiez(currentProduct.sieze).isNotEmpty())   CustomDropdownMenu(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.45f),
            text =  if (addModifyViewModel.unitselected != "") addModifyViewModel.unitselected
            else Functions.removeAllDigitExeptX(currentProduct.sieze),
            errorValue = null,
            label = stringResource(R.string.Unite),
            readOnly =  true,
            onClickExpand = {
                addModifyViewModel.onUnitExpandedChange(true)
            },
            dropDownMenu = {
                DropdownMenu(
                    expanded = addModifyViewModel.unitExpanded,
                    onDismissRequest = {  addModifyViewModel.onUnitExpandedChange(false) }) {
                    addModifyViewModel.mesureUnitList.forEach { item ->
                        DropdownMenuItem(text = {
                            Text(text = item)
                        }, onClick = {
                            addModifyViewModel.onUnitselectedChange(item)

                            onSelectedProductChanged(currentProduct.copy(sieze = currentProduct.sieze + item))
                            addModifyViewModel.onUnitExpandedChange(false)
                        })
                    }
                }

            }
        )
    }
    EditTextField(
        modifier = Modifier
            .fillMaxWidth(),
        text = currentProduct.imageurl,
        errorvalue = null,
        label = stringResource(R.string.Liendimage),
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(imageurl = it))
        },
        readOnly = false,
        enabled = true,
        keyboardType = KeyboardType.Uri,
        imeAction = ImeAction.Next
    )

    EditTextField(
        modifier = Modifier
            .fillMaxWidth(),
        text = currentProduct.description,
        errorvalue = null,
        label = stringResource(R.string.Descriptionduproduit),
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(description = it))
        },
        readOnly = false,
        enabled = true,
        keyboardType = KeyboardType.Uri,
        imeAction = ImeAction.Next
    )

    EditTextField(
        modifier = Modifier
            .fillMaxWidth(),
        text = currentProduct.descriptionarabe,
        errorvalue = null,
        label = stringResource(R.string.DescriptionduproduitenArabe),
        onValueChange = {
            onSelectedProductChanged(currentProduct.copy(descriptionarabe = it))
        },
        readOnly = false,
        enabled = true,
        keyboardType = KeyboardType.Uri,
        imeAction = ImeAction.Next
    )


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment= Alignment.CenterVertically,
    ) {

        EditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.3f),
            text = currentProduct.monoprixprice,
            errorvalue = null,
            label = stringResource(R.string.monoprix_prix),
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(monoprixprice = it))
            },
            readOnly = false,
            enabled = true,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.padding(end = 9.dp))
        ClickableEditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.65f),
            text  = currentProduct.monoprixremarq,
            errorValue = null,
            label =  context.getString(R.string.promotion_monoprix),
            readOnly = true,
            onClick= {
                addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_monoprix))
                navController.navigate(ListScreens.PriceRemarques.Route)
            },
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(monoprixremarq = it))
            }
        )

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
        ,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment= Alignment.CenterVertically,
    ) {
        EditTextField(
            modifier = Modifier
                .customWidth(LocalConfiguration.current, 0.3f),
            text = currentProduct.mgprice,
            errorvalue = null,
            label = stringResource(R.string.mg_prix),
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(mgprice = it))
            },
            readOnly = false,
            enabled = true,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.padding(end = 9.dp))
        ClickableEditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.65f),
            text  = currentProduct.mgremarq,
            errorValue = null,
            label =  context.getString(R.string.promotion_mg),
            readOnly = true,
            onClick= {
                addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_mg))
                navController.navigate(ListScreens.PriceRemarques.Route)
            },
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(mgremarq = it))
            }
        )

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment= Alignment.CenterVertically,
    ) {
        EditTextField(
            modifier = Modifier
                .customWidth(LocalConfiguration.current, 0.3f),
            text = currentProduct.carrefourprice,
            errorvalue = null,
            label = stringResource(R.string.carrefour_prix),
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(carrefourprice = it))
            },
            readOnly = false,
            enabled = true,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.padding(end = 9.dp))
        ClickableEditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.65f),
            text  = currentProduct.carrefourremarq,
            errorValue = null,
            label =  context.getString(R.string.promotion_carrefour),
            readOnly = true,
            onClick= {
                addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_carrefour))
                navController.navigate(ListScreens.PriceRemarques.Route)
            },
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(carrefourremarq = it))
            }
        )

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
                horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment= Alignment.CenterVertically,
    ) {
        EditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.3f),
            text = currentProduct.azzizaprice,
            errorvalue = null,
            label = stringResource(R.string.azziza_prix),
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(azzizaprice = it))
            },
            readOnly = false,
            enabled = true,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.padding(end = 9.dp))

        ClickableEditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.65f),
            text  = currentProduct.azzizaremarq,
            errorValue = null,
            label =  context.getString(R.string.promotion_azziza),
            readOnly = true,
            onClick= {
                addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_azziza))
                navController.navigate(ListScreens.PriceRemarques.Route)
            },
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(azzizaremarq = it))
            }
        )

    }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment= Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
    ) {
        EditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.3f),
            text = currentProduct.geantprice,
            errorvalue = null,
            label = stringResource(R.string.Géant_prix),
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(geantprice = it))
            },
            readOnly = false,
            enabled = true,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.padding(end = 9.dp))
        ClickableEditTextField(
            modifier = Modifier.customWidth(LocalConfiguration.current, 0.65f),
            text  = currentProduct.geantremarq,
            errorValue = null,
            label =  context.getString(R.string.promotion_Géant),
            readOnly = true,
            onClick= {
                addModifyViewModel.onMagasinValue(context.getString(R.string.promotion_Géant))
                navController.navigate(ListScreens.PriceRemarques.Route)
            },
            onValueChange = {
                onSelectedProductChanged(currentProduct.copy(geantremarq = it))
            }
        )

    }

    Spacer(modifier = Modifier.padding(bottom = 12.dp))
}









