package com.amirami.simapp.priceindicatortunisia.ui.screens.priceremarqs

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
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.ui.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.ui.screens.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.ui.screens.addmodify.EditTextInputComponent

@Composable
fun PriceRemarqScreen(addModifyViewModel: AddModifyViewModel) {
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

        MyUI()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EditTextInputComponent(
                modifier = Modifier.width(100.dp),
                "vb",
                R.string.Prix ,
                onValueChange = {
                    "cc"
                }
            )

            Spacer(modifier = Modifier.padding(end = 15.dp))

            Text(text = context.getString(R.string.bonus_dans_la_carte_de_fidélité))
        }

        Spacer(modifier = Modifier.height(30.dp))

        ButtonWithBorder(
            modifier = Modifier.wrapContentWidth(),
            onClicks = {
             //   navController.navigate(ListScreens.AddModify.Route)
            },
            text = context.getString(R.string.Modifier)
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun MyUI() {
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

    var selectedItem by remember { mutableStateOf(radioOptions[0]) }
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
                        onClick = { selectedItem = label },
                        role = Role.RadioButton
                    ),
                    //.padding(horizontal = 16.dp),
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
                        val widh = if(i == 2 || i == 6) 120.dp else 140.dp
                        EditTextInputComponent(
                            modifier = Modifier.width(widh),
                            "vb$i",
                            if(i == 2 || i == 6) R.string.Prix else  R.string.Nombre ,
                            onValueChange = {
                                "cc"
                            }
                        )
                        Spacer(modifier = Modifier.padding(end = 15.dp))
                        Text(text = label)

                    }
                    5 -> {
                        Text(text = context.getString(R.string.tiree))
                        Spacer(modifier = Modifier.padding(end = 10.dp))
                        EditTextInputComponent(
                            modifier = Modifier.width(160.dp),
                            "vb$i",
                            R.string.Pourcentage ,
                            onValueChange = {
                                "cc"
                            }
                        )
                        Spacer(modifier = Modifier.padding(end = 15.dp))
                        Text(text = label)
                    }
                    7 -> {
                        Text(text = label)

                        Spacer(modifier = Modifier.padding(end = 6.dp))
                        EditTextInputComponent(
                            modifier = Modifier.width(100.dp),
                            "vb$i",
                            R.string.Nombre ,
                            onValueChange = {
                                "cc"
                            }
                        )
                        Spacer(modifier = Modifier.padding(end = 6.dp))

                        Text(text = context.getString(R.string.est))

                        Spacer(modifier = Modifier.padding(end = 6.dp))
                        EditTextInputComponent(
                            modifier = Modifier.width(100.dp),
                            "vb$i",
                            R.string.Prix ,
                            onValueChange = {
                                "cc"
                            }
                        )

                        Spacer(modifier = Modifier.padding(end = 15.dp))

                        Text(text = context.getString(R.string.TND))
                    }
                }


            }
        }
    }
}
