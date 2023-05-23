package com.amirami.simapp.priceindicatortunisia.screens.priceremarqs

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.screens.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.ui.componenet.EditTextField
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getNumberFromString
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getNumbersFromString

@Composable
fun PriceRemarqScreen(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    addModifyViewModel: AddModifyViewModel
) {
    val context = LocalContext.current
     val magasinValue = addModifyViewModel.magasinValue
    val currentProduct = productsViewModel.selectedProductStates
    Column(
        modifier = Modifier
            .fillMaxSize()
           //  .padding(padding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = magasinValue,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(30.dp))

        MyUI(
            magasinValue = magasinValue,
            onSelectedProductChanged = {
                productsViewModel.onSelectedProductChanged(it)
                                       },
            currentProduct = currentProduct
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EditTextField(
                modifier = Modifier
                    .width(100.dp),
                text = getBonusFidelite(
                    context = context,
                    currentProduct = currentProduct ,
                    magasinValue = magasinValue
                ),
                errorvalue = null,
                label = stringResource(R.string.Prix),
                onValueChange = {
                    onValueChangeBonusFidelite(
                        context = context,
                        magasinValue = magasinValue,
                      result =   it,
                        onSelectedProductChanged = { product ->
                           productsViewModel.onSelectedProductChanged(product)
                        },
                        currentProduct = currentProduct
                        )
                },
                readOnly = false,
                enabled = true,
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            )


            Spacer(modifier = Modifier.padding(end = 15.dp))

            Text(text = context.getString(R.string.bonus_dans_la_carte_de_fidélité))
        }

        Spacer(modifier = Modifier.height(30.dp))

        ButtonWithBorder(
            modifier = Modifier.wrapContentWidth().align(Alignment.CenterHorizontally),
            onClicks = {
                 navController.navigate(ListScreens.AddModify.Route)
                       },
            text = context.getString(R.string.Valider)
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun MyUI(
    currentProduct: ProductModel,
    onSelectedProductChanged: (ProductModel) -> Unit,
    magasinValue : String
) {
    val context = LocalContext.current

    val radioOptions = listOf(
        context.getString(R.string.pasderemarque),
        context.getString(R.string.prixEnPromotion),
        context.getString(R.string.remisesurlaseuxiemme),
        context.getString(R.string.emegratuit),
        context.getString(R.string.pourcentage),
        context.getString(R.string.aveccartefid),
        context.getString(R.string.Leprix)
    )

    val index = radioBtnSteUp(
        context = context,
       promotion =  getRemarque(
        context = context,
        currentProduct = currentProduct,
        magasinValue = magasinValue),
        radioOptions = radioOptions
    )


    var selectedItem by remember { mutableStateOf(radioOptions[if (index == -1) 0 else index - 1]) }

    var i = 0
    Column(modifier = Modifier.selectableGroup()) {
        radioOptions.forEach { label ->
            i++
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (selectedItem == label),
                        onClick = {
                            selectedItem = label

                            // index = i
                            //  index = radioBtnSteUp(context, label, radioOptions)
                            onValueChangeRemarq(
                                context = context,
                                magasinValue = magasinValue,
                                currentProduct = currentProduct,
                               value =  label,
                                value2 = "", 
                              i =  radioBtnSteUp(context, label, radioOptions),
                                      onSelectedProductChanged = {
                                          onSelectedProductChanged(it)
                                      }
                            )
                        },
                        role = Role.RadioButton
                    ),
                // .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 16.dp),
                    imageVector = if (selectedItem == label) {
                        Icons.Outlined.CheckCircle
                    } else {
                        Icons.Outlined.RadioButtonUnchecked
                    },
                    // screen readers will read the Text() compsable content
                    // if we pass label here, they end up reading the content twice
                    // so, pass null
                    contentDescription = null,
                    tint = Color.Magenta
                )
                when (i) {
                    1, 3 -> Text(text = label)
                    2, 4, 6 -> {
                        EditTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = if (index == i) getNumberFromString(getRemarque(
                                context=context, 
                                currentProduct = currentProduct,
                                magasinValue = magasinValue
                            )) else "",
                            errorvalue = null,
                            label = stringResource(if (i == 2) R.string.prixEnPromotion else if (i == 4)R.string.emegratuit else R.string.aveccartefid),
                            onValueChange = {
                                onValueChangeRemarq(
                                    context = context,
                                    value = getNumberFromString(it),
                                    value2="",
                                    i = radioBtnSteUp(context, label, radioOptions),
                                    onSelectedProductChanged = { prod ->
                                       onSelectedProductChanged(prod)
                                    },
                                    magasinValue = magasinValue,
                                    currentProduct = currentProduct
                                )
                            },
                            enabled = selectedItem == label,
                            readOnly = selectedItem != label,
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                    }
                    5 -> {
                        Text(text = context.getString(R.string.tiree))
                        Spacer(modifier = Modifier.padding(end = 10.dp))
                        EditTextField(
                            modifier = Modifier
                                .width(160.dp),
                            text = if (index == i) getNumberFromString(getRemarque(context=context,
                                currentProduct = currentProduct,
                                magasinValue = magasinValue)) else "",
                            errorvalue = null,
                            label = stringResource(R.string.Pourcentage),
                            onValueChange = {
                                onValueChangeRemarq(
                                    context = context,
                                    onSelectedProductChanged = { prod ->
                                       onSelectedProductChanged(prod)
                                    },
                                    currentProduct = currentProduct,
                                    magasinValue = magasinValue,
                                    value = getNumberFromString(it),
                                   value2 =  "",
                                   i = radioBtnSteUp(context, label, radioOptions))
                            },
                            enabled = selectedItem == label,
                            readOnly = selectedItem != label,
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )

                        Spacer(modifier = Modifier.padding(end = 15.dp))
                        Text(text = label)
                    }
                    7 -> {
                        Text(text = label)
                        Spacer(modifier = Modifier.padding(end = 6.dp))

                        EditTextField(
                            modifier = Modifier
                                .width(160.dp),
                            text =  if (index == i) getNumbersFromString(
                                getRemarque(context=context,
                                    currentProduct = currentProduct,
                                    magasinValue = magasinValue),
                                context.getString(R.string.Leprix),
                                context.getString(R.string.est)
                            ).toInt().toString() else "",
                            errorvalue = null,
                            label = stringResource(R.string.Nombre),
                            onValueChange = {
                                onValueChangeRemarq(
                                    context = context,
                                    onSelectedProductChanged = {prod ->
                                       onSelectedProductChanged(prod)
                                    },
                                    magasinValue = magasinValue,
                                    currentProduct = currentProduct,
                                    value = getNumberFromString(it),
                                   value2 =  "",
                                   i = radioBtnSteUp(context, label, radioOptions)
                                )
                            },
                            enabled = selectedItem == label,
                            readOnly = selectedItem != label,
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )

                        Spacer(modifier = Modifier.padding(end = 6.dp))

                        Text(text = context.getString(R.string.est))

                        Spacer(modifier = Modifier.padding(end = 6.dp))
                        EditTextField(
                            modifier = Modifier
                                .width(100.dp),
                            text =  if (index == i) getNumbersFromString(
                                getRemarque(context=context,
                                    currentProduct = currentProduct,
                                    magasinValue = magasinValue),
                                context.getString(R.string.est),
                                context.getString(R.string.TND)
                            ).toString() else "",
                            errorvalue = null,
                            label = stringResource(R.string.Prix),
                            onValueChange = {
                                onValueChangeRemarq(
                                    context = context,
                                    onSelectedProductChanged = {prod ->
                                       onSelectedProductChanged(prod)
                                    },
                                    magasinValue = magasinValue,
                                    currentProduct = currentProduct,
                                   value = getNumbersFromString(getRemarque(context=context,
                                       currentProduct = currentProduct,
                                       magasinValue = magasinValue), context.getString(R.string.Leprix), context.getString(R.string.est)).toString(),
                                    value2  = getNumberFromString(it),
                                   i = radioBtnSteUp(context, label, radioOptions)
                                )                            },
                            enabled = selectedItem == label,
                            readOnly = selectedItem != label,
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )


                        Spacer(modifier = Modifier.padding(end = 15.dp))

                        Text(text = context.getString(R.string.TND))
                    }
                }
            }
        }
    }
}

fun getRemarque(context: Context, currentProduct : ProductModel,magasinValue : String): String {
    return when (magasinValue) {
        context.getString(R.string.promotion_monoprix) -> currentProduct.monoprixremarq
        context.getString(R.string.promotion_mg) -> currentProduct.mgremarq
        context.getString(R.string.promotion_carrefour) -> currentProduct.carrefourremarq
        context.getString(R.string.promotion_azziza) -> currentProduct.azzizaremarq
        context.getString(R.string.promotion_Géant) -> currentProduct.geantremarq
        else -> ""
    }

/*
 return   when {
        promotion == context.getString(R.string.pasderemarque) -> {
""
        }
        promotion == context.getString(R.string.remisesurlaseuxiemme) -> {
           ""
        }

        promotion.contains(context.getString(R.string.prixEnPromotion)) -> {
            Functions.getNumberFromString(promotion).toString()
        }

        promotion.contains(context.getString(R.string.emegratuit)) -> {
            Functions.getNumberFromString(promotion).toInt().toString()
        }

        promotion.contains(context.getString(R.string.pourcentage)) -> {
            Functions.getNumberFromString(promotion).toString()
        }
        promotion.contains(context.getString(R.string.aveccartefid)) -> {
           Functions.getNumberFromString(promotion).toString()
        }
        promotion.contains(context.getString(R.string.Leprix)) -> {

                Functions.getNumbersFromString(
                    promotion,
                    context.getString(R.string.Leprix),
                    context.getString(R.string.est)
                ).toInt().toString()

                Functions.getNumbersFromString(
                    promotion,
                    context.getString(R.string.est),
                    context.getString(R.string.TND)
                ).toString()
        }

        else -> {
           ""
        }
    }
*/
}

fun getBonusFidelite(context: Context,  currentProduct : ProductModel, magasinValue : String): String {
    return when (magasinValue) {
        context.getString(R.string.promotion_monoprix) -> currentProduct.monoprixbonusfid
        context.getString(R.string.promotion_mg) -> currentProduct.mgbonusfid
        context.getString(R.string.promotion_carrefour) -> currentProduct.carrefourbonusfid
        context.getString(R.string.promotion_azziza) -> currentProduct.azzizabonusfid
        context.getString(R.string.promotion_Géant) -> currentProduct.geantbonusfid
        else -> ""
    }
}

fun onValueChangeRemarq(
    context: Context,
    onSelectedProductChanged :(ProductModel) ->Unit,
    currentProduct : ProductModel,
    magasinValue : String,
    value: String,
    value2: String,
    i: Int) {
    var valu = ""

    var valu2 = ""

    if (getNumberFromString(value) != "" && i != 3) valu = getNumberFromString(value)

    if (getNumberFromString(valu2) != "" && i == 7) valu2 = getNumberFromString(value2)

    val result = when (i) {
        1 -> ""
        2 -> valu + " " + context.getString(R.string.TND) + " " + context.getString(R.string.prixEnPromotion)
        3 -> context.getString(R.string.remisesurlaseuxiemme)
        4 ->
            if (valu != "0" && valu != "1") {
                valu + " " + context.getString(R.string.emegratuit)
            } else "" // DynamicToast.makeError(requireContext(), "0 et 1 ne sont pas accepté", 9).show()

        5 -> context.getString(R.string.tiree) + valu + context.getString(R.string.pourcentage)
        6 -> valu + " " + context.getString(R.string.aveccartefid)
        7 -> context.getString(R.string.Leprix) + " " + valu + " " + context.getString(R.string.est) + " " + valu2 + " " + context.getString(R.string.TND)
        else -> ""
    }

    when (magasinValue) {
        context.getString(R.string.promotion_monoprix) -> onSelectedProductChanged(currentProduct.copy(monoprixremarq = result))
        context.getString(R.string.promotion_mg) -> onSelectedProductChanged(currentProduct.copy(mgremarq = result))
        context.getString(R.string.promotion_carrefour) -> onSelectedProductChanged(currentProduct.copy(carrefourremarq = result))
        context.getString(R.string.promotion_azziza) -> onSelectedProductChanged(currentProduct.copy(azzizaremarq = result))
        context.getString(R.string.promotion_Géant) -> onSelectedProductChanged(currentProduct.copy(geantremarq = result))
        else -> ""
    }
}

fun onValueChangeBonusFidelite(
    context: Context, 
    magasinValue : String,
    currentProduct : ProductModel,
    result: String,
    onSelectedProductChanged :(ProductModel) ->Unit,
    ) {
    when (magasinValue) {
        context.getString(R.string.promotion_monoprix) -> onSelectedProductChanged(currentProduct.copy(monoprixbonusfid = result))
        context.getString(R.string.promotion_mg) -> onSelectedProductChanged(currentProduct.copy(mgbonusfid = result))
        context.getString(R.string.promotion_carrefour) -> onSelectedProductChanged(currentProduct.copy(carrefourbonusfid = result))
        context.getString(R.string.promotion_azziza) -> onSelectedProductChanged(currentProduct.copy(azzizabonusfid = result))
        context.getString(R.string.promotion_Géant) -> onSelectedProductChanged(currentProduct.copy(geantbonusfid = result))
        else -> ""
    }
}

fun radioBtnSteUp(context: Context, promotion: String, radioOptions: List<String>): Int {
    var i = -1
   /* for (item in radioOptions) {
        if (promotion.contains(item)) break
        else i++
    }*/

    for (a in radioOptions.indices) {
        if (promotion.contains(radioOptions[a])) {
            i = a + 1
            break
        }
    }
    // return if (i > 7) 0 else i+1
    return i
}
