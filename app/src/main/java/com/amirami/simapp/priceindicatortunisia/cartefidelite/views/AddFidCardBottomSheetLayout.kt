package com.amirami.simapp.priceindicatortunisia.cartefidelite.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity

@Composable
fun AddFidCardBottomSheetLayout(
    onShowAddFidCardChanged : (Boolean) -> Unit,
    upsertFidCard : (FidCardEntity) -> Unit,
    onfidCardInfo : (FidCardEntity) -> Unit,
    fidCardBarCodeInfo : FidCardEntity
) {
    val context = LocalContext.current

    //fidCardRoomViewModel.getFidCardByValue("n")

    Column(
        modifier = Modifier
              .fillMaxWidth()
            .wrapContentHeight()
            // .background(Color.White)
            .padding(16.dp)
            .padding(bottom = 60.dp),
        //    .verticalScroll(rememberScrollState()),
         verticalArrangement = Arrangement.Top,
           horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(context.getString(R.string.Ajoutezcartefidelite) )
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = fidCardBarCodeInfo.name,// if( fidCardRoomViewModel.fidCardByValue.name!="") fidCardRoomViewModel.fidCardByValue.name else fidCardBarCodeInfo.name ,
            onValueChange = {
                onfidCardInfo(
                    fidCardBarCodeInfo.copy(name =it)
                   /* FidCardEntity(
                        name = when(fidCardAction) {
                            FID_CARD_ACTION_DELETE ->  fidCardBarCodeInfo.name
                            else -> it
                        } ,
                        value = fidCardBarCodeInfo.value,
                        barecodeformat = fidCardBarCodeInfo.barecodeformat,
                        barecodetype = fidCardBarCodeInfo.barecodetype
                    )*/
                )
                
            },
            label = { Text(context.getString(R.string.Nom)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = true,
                imeAction = ImeAction.Next
            )
        )


        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = fidCardBarCodeInfo.value,
            onValueChange = {
               onfidCardInfo(
                        FidCardEntity(
                        name = fidCardBarCodeInfo.name,
                        value = it,//fidCardBarCodeInfo.value,//
                        barecodeformat = fidCardBarCodeInfo.barecodeformat,
                        barecodetype = fidCardBarCodeInfo.barecodetype
                    )

                )
            },
            label = { Text(context.getString(R.string.codebarre)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = true,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                if(fidCardBarCodeInfo.name!="" && fidCardBarCodeInfo.value!="")
                clickDone(
                    onShowAddFidCardChanged  = {
                        onShowAddFidCardChanged(false)
                    },
                    fidCardBarCodeInfo = fidCardBarCodeInfo,
                    upsertFidCard = {
                        upsertFidCard(fidCardBarCodeInfo)
                    }
                )
                else Toast.makeText(context, "Entré nom et code à barre !",Toast.LENGTH_LONG).show()
            }
        )
        Spacer(modifier = Modifier.height(30.dp))


            Button(
                enabled = fidCardBarCodeInfo.name!="" && fidCardBarCodeInfo.value!="",
                onClick = {

                    clickDone(
                        onShowAddFidCardChanged  = {
                            onShowAddFidCardChanged(false)
                        },
                    fidCardBarCodeInfo = fidCardBarCodeInfo,
                    upsertFidCard = {
                        upsertFidCard(fidCardBarCodeInfo)
                    }
                    )


            }) {
                Text(text = context.getString(R.string.Ajouter) + " / " +context.getString(R.string.Modifier)
                )
            }




    }

}
fun clickDone(
    onShowAddFidCardChanged : (Boolean) -> Unit,
    fidCardBarCodeInfo : FidCardEntity,
              upsertFidCard : (FidCardEntity) -> Unit
    ) {
    upsertFidCard(fidCardBarCodeInfo)
    onShowAddFidCardChanged(false)
}




