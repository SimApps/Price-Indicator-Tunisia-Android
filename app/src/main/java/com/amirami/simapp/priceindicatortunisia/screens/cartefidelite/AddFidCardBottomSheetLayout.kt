package com.amirami.simapp.priceindicatortunisia.screens.cartefidelite

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.core.Constants.FID_CARD_ACTION_ADD
import com.amirami.simapp.priceindicatortunisia.core.Constants.FID_CARD_ACTION_DELETE
import com.amirami.simapp.priceindicatortunisia.core.Constants.FID_CARD_ACTION_MODIFY
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.FidCardRoomViewModel
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import java.math.BigDecimal

@Composable
fun AddFidCardBottomSheetLayout(
    barCodeViewModel: BarCodeViewModel,
    fidCardRoomViewModel: FidCardRoomViewModel
) {
    val context = LocalContext.current

    //fidCardRoomViewModel.getFidCardByValue("n")

    Column(
        modifier = Modifier
            //   .fillMaxSize()
            .wrapContentHeight()
            // .background(Color.White)
            .padding(16.dp)
            .padding(bottom = 60.dp)
        //    .verticalScroll(rememberScrollState()),
        // verticalArrangement = Arrangement.Top,
        //   horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(text =when (barCodeViewModel.fidCardAction) {
            FID_CARD_ACTION_MODIFY -> context.getString(R.string.modifierCarteFIDELITE)
            FID_CARD_ACTION_ADD ->  context.getString(R.string.Ajoutezcartefidelite)
            else -> context.getString(R.string.Voulez_vous_suprimer_cette_carte_fid)
        }
               )
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = barCodeViewModel.fidCardBarCodeInfo.name,// if( fidCardRoomViewModel.fidCardByValue.name!="") fidCardRoomViewModel.fidCardByValue.name else barCodeViewModel.fidCardBarCodeInfo.name ,
            onValueChange = {
                barCodeViewModel.onfidCardInfo(
                     FidCardEntity(
                        name = when(barCodeViewModel.fidCardAction) {
                            FID_CARD_ACTION_DELETE ->  barCodeViewModel.fidCardBarCodeInfo.name
                            else -> it
                        } ,
                        value = barCodeViewModel.fidCardBarCodeInfo.value,
                        barecodeformat = barCodeViewModel.fidCardBarCodeInfo.barecodeformat,
                        barecodetype = barCodeViewModel.fidCardBarCodeInfo.barecodetype
                    )

                )
            },
            label = { Text(context.getString(R.string.Nom)) }
        )


        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = barCodeViewModel.fidCardBarCodeInfo.value,
            onValueChange = {
                barCodeViewModel.onfidCardInfo(
                        FidCardEntity(
                        name = barCodeViewModel.fidCardBarCodeInfo.name,
                        value = when(barCodeViewModel.fidCardAction) {
                            FID_CARD_ACTION_DELETE ->  barCodeViewModel.fidCardBarCodeInfo.value
                            else -> it
                        },//barCodeViewModel.fidCardBarCodeInfo.value,//
                        barecodeformat = barCodeViewModel.fidCardBarCodeInfo.barecodeformat,
                        barecodetype = barCodeViewModel.fidCardBarCodeInfo.barecodetype
                    )

                )
            },
            label = { Text(context.getString(R.string.codebarre)) }
        )
        Spacer(modifier = Modifier.height(30.dp))

        Row(){
            Button(
                enabled = if(barCodeViewModel.fidCardAction!=context.getString(R.string.supprimer))
                    barCodeViewModel.fidCardBarCodeInfo.name!="" && barCodeViewModel.fidCardBarCodeInfo.value!=""
                else true,
                onClick = {

                    when (barCodeViewModel.fidCardAction) {
                        FID_CARD_ACTION_DELETE -> barCodeViewModel.fidCardBarCodeInfo.value.let { fidCardRoomViewModel.deleteFidCardByValue(it) }
                        else -> fidCardRoomViewModel.upsertFidCard(barCodeViewModel.fidCardBarCodeInfo)
                    }

                    barCodeViewModel.onShowAddFidCardChanged(false)

            }) {
                Text(text = when (barCodeViewModel.fidCardAction) {
                    FID_CARD_ACTION_MODIFY -> context.getString(R.string.Modifier)
                    FID_CARD_ACTION_ADD -> context.getString(R.string.Ajouter)
                    else -> context.getString(R.string.supprimer)
                }
                )
            }
            if(barCodeViewModel.fidCardAction == FID_CARD_ACTION_DELETE){
                Spacer(modifier = Modifier.height(30.dp))

                Button(onClick = {
                    barCodeViewModel.onShowAddFidCardChanged(false)
                }) {
                    Text(text =  context.getString(R.string.Annulé) )
                }
            }

        }

    }

}





