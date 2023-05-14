package com.amirami.simapp.priceindicatortunisia.screens.priceremarqs

import android.content.Context
import android.util.Log
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.screens.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.screens.addmodify.EditTextInputComponent
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getNumberFromString
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getNumbersFromString

@Composable
fun PriceRemarqScreen(navController: NavHostController, addModifyViewModel: AddModifyViewModel) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            // .background(Color.White)
            // .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = addModifyViewModel.magasinValue, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(30.dp))

        MyUI(addModifyViewModel = addModifyViewModel)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EditTextInputComponent(
                modifier = Modifier.width(100.dp),
                getBonusFidelite(context, addModifyViewModel),
                R.string.Prix,
                onValueChange = {
                    onValueChangeBonusFidelite(context, addModifyViewModel, it)
                },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.padding(end = 15.dp))

            Text(text = context.getString(R.string.bonus_dans_la_carte_de_fidélité))
        }

        Spacer(modifier = Modifier.height(30.dp))

        ButtonWithBorder(
            modifier = Modifier.wrapContentWidth().align(Alignment.CenterHorizontally),
            onClicks = {
                 navController.navigate(ListScreens.AddModify.Route)

                //  Log.d("ffffxs", getBonusFidelite(context, addModifyViewModel))
                Log.d("ffffxs", getRemarque(context, addModifyViewModel))
            },
            text = context.getString(R.string.Valider)
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun MyUI(addModifyViewModel: AddModifyViewModel) {
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

    var index = radioBtnSteUp(context, getRemarque(context, addModifyViewModel), radioOptions)
    Text(  "name " + index)
    Text(  "cc " + addModifyViewModel.monoprixPromotionTextValue)
    Text(  "C " + getRemarque(context, addModifyViewModel))

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
                            onValueChangeRemarq(context, addModifyViewModel, label,"", radioBtnSteUp(context, label, radioOptions))
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
                        // val width = if (i == 2 || i == 6) 120.dp else 140.dp
                        EditTextInputComponent(
                            modifier = Modifier.fillMaxWidth(),
                            //  if(index==i)   Functions.getNumberFromString(getRemarque(context, addModifyViewModel)).toString() else i.toString() + label+ index  ,
                            if (index == i) getNumberFromString(getRemarque(context, addModifyViewModel)) else "",
                            //  if(index==i)   getRemarque(context, addModifyViewModel) else i.toString() + "N "+ index  ,

                            if (i == 2) R.string.prixEnPromotion else if (i == 4)R.string.emegratuit else R.string.aveccartefid,
                            onValueChange = {
                                //  onValueChangeRemarq(context, addModifyViewModel, Functions.getNumberFromString(it).toString())
                                onValueChangeRemarq(context, addModifyViewModel, getNumberFromString(it),"", radioBtnSteUp(context, label, radioOptions))
                            },
                            enabled = selectedItem == label,
                            readOnly = selectedItem != label,
                            keyboardType = KeyboardType.Number
                        )
                        // Spacer(modifier = Modifier.padding(end = 15.dp))
                        // Text(text = label)
                    }
                    5 -> {
                        Text(text = context.getString(R.string.tiree))
                        Spacer(modifier = Modifier.padding(end = 10.dp))
                        EditTextInputComponent(
                            modifier = Modifier.width(160.dp),
                            if (index == i) getNumberFromString(getRemarque(context, addModifyViewModel)) else "",
                            R.string.Pourcentage,
                            onValueChange = {
                                onValueChangeRemarq(context, addModifyViewModel, getNumberFromString(it),"", radioBtnSteUp(context, label, radioOptions))
                            },
                            enabled = selectedItem == label,
                            readOnly = selectedItem != label,
                            keyboardType = KeyboardType.Number
                        )
                        Spacer(modifier = Modifier.padding(end = 15.dp))
                        Text(text = label)
                    }
                    7 -> {
                        Text(text = label)
                        Spacer(modifier = Modifier.padding(end = 6.dp))
                        EditTextInputComponent(
                            modifier = Modifier.width(100.dp),
                            if (index == i) getNumbersFromString(
                                getRemarque(context, addModifyViewModel),
                                context.getString(R.string.Leprix),
                                context.getString(R.string.est)
                            ).toInt().toString() else "",
                            R.string.Nombre,
                            onValueChange = {
                                onValueChangeRemarq(context, addModifyViewModel, getNumberFromString(it),"", radioBtnSteUp(context, label, radioOptions))
                            },
                            enabled = selectedItem == label,
                            readOnly = selectedItem != label,
                            keyboardType = KeyboardType.Number
                        )
                        Spacer(modifier = Modifier.padding(end = 6.dp))

                        Text(text = context.getString(R.string.est))

                        Spacer(modifier = Modifier.padding(end = 6.dp))
                        EditTextInputComponent(
                            modifier = Modifier.width(100.dp),
                            if (index == i) getNumbersFromString(
                                getRemarque(context, addModifyViewModel),
                                context.getString(R.string.est),
                                context.getString(R.string.TND)
                            ).toString() else "",
                            R.string.Prix,
                            onValueChange = {
                                Log.d("ffffxs","it  "+ it)
                                Log.d("ffffxs","ccc  "+ getNumbersFromString(getRemarque(context, addModifyViewModel), context.getString(R.string.Leprix), context.getString(R.string.est)).toString())
                                onValueChangeRemarq(context,
                                    addModifyViewModel,
                                    getNumbersFromString(getRemarque(context, addModifyViewModel), context.getString(R.string.Leprix), context.getString(R.string.est)).toString(),
                                    getNumberFromString(it),
                                    radioBtnSteUp(context, label, radioOptions))
                                            },
                            enabled = selectedItem == label,
                            readOnly = selectedItem != label,
                            keyboardType = KeyboardType.Number
                        )

                        Spacer(modifier = Modifier.padding(end = 15.dp))

                        Text(text = context.getString(R.string.TND))
                    }
                }
            }
        }
    }
}

fun getRemarque(context: Context, addModifyViewModel: AddModifyViewModel): String {
    return when (addModifyViewModel.magasinValue) {
        context.getString(R.string.promotion_monoprix) -> addModifyViewModel.monoprixPromotionTextValue
        context.getString(R.string.promotion_mg) -> addModifyViewModel.mgPromotionTextValue
        context.getString(R.string.promotion_carrefour) -> addModifyViewModel.carrefourPromotionTextValue
        context.getString(R.string.promotion_azziza) -> addModifyViewModel.azizaPromotionTextValue
        context.getString(R.string.promotion_Géant) -> addModifyViewModel.geantPromotionTextValue
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

fun getBonusFidelite(context: Context, addModifyViewModel: AddModifyViewModel): String {
    return when (addModifyViewModel.magasinValue) {
        context.getString(R.string.promotion_monoprix) -> addModifyViewModel.monoprixBonusSurCartFidTextValue
        context.getString(R.string.promotion_mg) -> addModifyViewModel.mgBonusSurCartFidTextValue
        context.getString(R.string.promotion_carrefour) -> addModifyViewModel.carrefourBonusSurCartFidTextValue
        context.getString(R.string.promotion_azziza) -> addModifyViewModel.azizaBonusSurCartFidTextValue
        context.getString(R.string.promotion_Géant) -> addModifyViewModel.geantBonusSurCartFidTextValue
        else -> ""
    }
}

fun onValueChangeRemarq(context: Context, addModifyViewModel: AddModifyViewModel, value: String, value2: String, i: Int) {
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
    Log.d("ffffxs","i = "+i+ "value "+ value)
    Log.d("ffffxs","valu "+ valu)
    Log.d("ffffxs", "result  "+result)
    when (addModifyViewModel.magasinValue) {
        context.getString(R.string.promotion_monoprix) -> addModifyViewModel.onmonoprixPromotionTextValue(result)
        context.getString(R.string.promotion_mg) -> addModifyViewModel.onMgPromotionTextValue(result)
        context.getString(R.string.promotion_carrefour) -> addModifyViewModel.onCarrefouPromotionTextValue(result)
        context.getString(R.string.promotion_azziza) -> addModifyViewModel.onAzizaPromotionTextValue(result)
        context.getString(R.string.promotion_Géant) -> addModifyViewModel.onGeantPromotionTextValue(result)
        else -> ""
    }
}

fun onValueChangeBonusFidelite(context: Context, addModifyViewModel: AddModifyViewModel, value: String) {
    when (addModifyViewModel.magasinValue) {
        context.getString(R.string.promotion_monoprix) -> addModifyViewModel.onMonoprixBonusSurCartFidTextValue(value)
        context.getString(R.string.promotion_mg) -> addModifyViewModel.onMgBonusSurCartFidTextValue(value)
        context.getString(R.string.promotion_carrefour) -> addModifyViewModel.onCarrefouBonusSurCartFidTextValue(value)
        context.getString(R.string.promotion_azziza) -> addModifyViewModel.onAzizaBonusSurCartFidTextValue(value)
        context.getString(R.string.promotion_Géant) -> addModifyViewModel.onGeantBonusSurCartFidTextValue(value)
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
