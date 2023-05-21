@file:OptIn(ExperimentalMaterial3Api::class)

package com.amirami.simapp.priceindicatortunisia.screens.cartefidelite

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.FidCardRoomViewModel
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.ui.componenet.CustomAlertDialogue
import com.amirami.simapp.priceindicatortunisia.ui.componenet.LottieComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.util.Permission
import com.amirami.simapp.priceindicatortunisia.utils.generateBarCode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@Composable
fun FidCardsScreen(
    padding: PaddingValues,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    fidCardRoomViewModel: FidCardRoomViewModel
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    /**
     * to delete when find better solution to show bottom sheet after scan SEE ALSO NAVIGATION
     */
   // if (barCodeViewModel.fidCardAction != Constants.FID_CARD_ACTION_RESETED) {
        LaunchedEffect(key1 = Unit) {

            if(barCodeViewModel.fidCardBarCodeInfo!=FidCardEntity()){

                barCodeViewModel.getFidCardByValue(barCodeViewModel.fidCardBarCodeInfo)
            }

        }
  //  }

    CustomAlertDialogue(
        title = context.getString(R.string.Voulez_vous_suprimer_cette_carte_fid),
        msg = barCodeViewModel.fidCardBarCodeInfo.value,
        openDialog  = barCodeViewModel.showDeleteFidCard,
    setDialogueVisibility = {
        barCodeViewModel.onShowDeleteFidCardChanged(false)
    },
    customAction = {
        fidCardRoomViewModel.deleteFidCardByValue(barCodeViewModel.fidCardBarCodeInfo.value)
    }
    )

if(barCodeViewModel.fidCardBarCodeInfo!=FidCardEntity() && barCodeViewModel.showAddFidCard)
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }
            barCodeViewModel.onShowAddFidCardChanged(false)
            barCodeViewModel.onfidCardInfo(FidCardEntity())

        },
    ) {
        AddFidCardBottomSheetLayout(
            onShowAddFidCardChanged = {
                barCodeViewModel.onShowAddFidCardChanged(it)
            },
            fidCardBarCodeInfo =barCodeViewModel.fidCardBarCodeInfo,
            upsertFidCard = {
                fidCardRoomViewModel.upsertFidCard(it)
            },
            onfidCardInfo = {
                barCodeViewModel.onfidCardInfo(it)
            }
        )
    }

    FidCardsMainScreen(
        padding = padding,
        modifier = Modifier,
        navController = navController,
        barCodeViewModel = barCodeViewModel,
        fidCardRoomViewModel = fidCardRoomViewModel
    )


}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FidCardsMainScreen(
    padding: PaddingValues,
    modifier: Modifier,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    fidCardRoomViewModel: FidCardRoomViewModel
) {
    val configuration = LocalConfiguration.current


    val context = LocalContext.current





    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

         Text(text = context.getString(R.string.Info_cartefid_activity))
            Spacer(modifier = Modifier.height(30.dp))



        // Lottie(R.raw.empty)
        fidCardRoomViewModel.fidCards.let {
               if (it.isNotEmpty()) {
                   LazyColumn(
                       modifier = modifier
                           .height(300.dp),
                       verticalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       items(it.size) { position ->

                           FidCardsListItem(
                               modifier,
                               navController,
                               barCodeViewModel,
                               it[position]

                           )
                       }
                   }
               }
               else LottieComposable(
                   size =  250.dp,
                   lottiefile = R.raw.empty
               )
           }





            Spacer(modifier = modifier.height(30.dp))
        Permission(
            permission = listOf(Manifest.permission.CAMERA),
            permissionNotAvailableContent = { permissionState->
                Column(modifier) {
                    Text("Il Faut Accepter la permission d'utiliser la caméra")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                permissionState.launchMultiplePermissionRequest()
                            }
                        ) {
                            Text("Accé caméra ")
                        }

                    }
                }
            },
            content = {
                ButtonWithBorder(
                    modifier = modifier.fillMaxWidth(),
                    onClicks = {
                        navController.navigate(ListScreens.BarCodeCameraPreview.Route)
                    },

                   text = context.getString(R.string.Ajoutezcartefidelite),

                    )
            })


            Spacer(modifier = modifier.height(55.dp))


    }
}

@Composable
fun FidCardsListItem(
    modifier: Modifier,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    barcod: FidCardEntity
) {


    Card() {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(onClick = {
                    barCodeViewModel.onfidCardInfo(barcod)
                    generateBarCode(barcod, barCodeViewModel)
                    navController.navigate(ListScreens.GeneratedBarcodeImage.Route)

                })
                //  .height(55.dp)
                //    .background(color = Color.LightGray)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //   Icon(painter = painterResource(id = icon), contentDescription = "Share", tint = Color.White)
            Spacer(modifier = modifier.width(9.dp))
            Text(text = barcod.name)


            Spacer(modifier.weight(1f)) // height and background only for demonstration

            Image(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "",
                modifier = modifier
                    .padding(18.dp)
                    .clickable(
                        enabled = true,
                        onClickLabel = "Delete Fid card",
                        onClick = {
                            barCodeViewModel.onfidCardInfo(barcod)
                            barCodeViewModel.onShowDeleteFidCardChanged(true)

                        }
                    )

                // .border(2.dp, Color.Black, CutCornerShape(12.dp))
                //  .background(Color.DarkGray, CutCornerShape(6.dp))
                //  .padding(18.dp)
            )
            //  Spacer(modifier = Modifier.width(20.dp))


            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "",
                modifier = modifier
                    .padding(1.dp)
                    .clickable(
                        enabled = true,
                        onClickLabel = "Edit Fid card",
                        onClick = {
                            barCodeViewModel.onfidCardInfo(barcod)

                            barCodeViewModel.onShowAddFidCardChanged(true)

                        }
                    )
            )

        }
    }

}


