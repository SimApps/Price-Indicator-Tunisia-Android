@file:OptIn(ExperimentalMaterial3Api::class)

package com.amirami.simapp.priceindicatortunisia.cartefidelite.views

import android.Manifest
import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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
import com.amirami.simapp.priceindicatortunisia.cartefidelite.FidCardViewModel
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.core.Constants.SEPARATOR
import com.amirami.simapp.priceindicatortunisia.core.DateUtils
import com.amirami.simapp.priceindicatortunisia.datastore.viewmodel.DataViewModel
import com.amirami.simapp.priceindicatortunisia.domain.model.Response
import com.amirami.simapp.priceindicatortunisia.google_sign.GoogleAuthUiClient
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.ui.componenet.CustomAlertDialogue
import com.amirami.simapp.priceindicatortunisia.ui.componenet.LottieComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar.BottomNavigationBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.util.Permission
import com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar.TopBar
import com.amirami.simapp.priceindicatortunisia.utils.generateBarCode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@Composable
fun FidCardsScreen(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    productsViewModel : ProductsViewModel,
    fidCardViewModel: FidCardViewModel,
    dataViewModel: DataViewModel,
    userId : String
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val listFidCard =  if(fidCardViewModel.fidCards.isNotEmpty())   fidCardViewModel.fidCards as ArrayList
    else ArrayList()

    LaunchedEffect(key1 = fidCardViewModel.updateListFidCardRemoteResponse){
        when(val response =fidCardViewModel.updateListFidCardRemoteResponse) {
            is Response.Loading -> Log.d("okpl","loading")
            is Response.Success -> {
                // fidCardViewModel.getRemoteFidCardBareCode(docID = userId)
                Log.d("okpl","response.message")

                dataViewModel.saveLastDateFidCardSync(DateUtils.getCurrentDateTime())
                dataViewModel.getNbrDaySincFidCardSync()
            }
            is Response.Failure -> {
                Log.d("okpl",response.message)
            }
            else -> {}
        }
    }
    /**
     * to delete when find better solution to show bottom sheet after scan SEE ALSO NAVIGATION
     */
   // if (barCodeViewModel.fidCardAction != Constants.FID_CARD_ACTION_RESETED) {
        LaunchedEffect(key1 = Unit) {
            dataViewModel.getNbrDaySincFidCardSync()
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
        fidCardViewModel.deleteFidCardByValue(barCodeViewModel.fidCardBarCodeInfo.value)


        listFidCard.remove(barCodeViewModel.fidCardBarCodeInfo)
        fidCardViewModel.updateRemoteListFidCard(
            docID = userId,
            listFidCard.associateBy({fidCard -> fidCard.value }, {fidCard -> fidCard.name +SEPARATOR+fidCard.barecodeformat +SEPARATOR+ fidCard.barecodetype })
        )
    }
    )

if(barCodeViewModel.fidCardBarCodeInfo!= FidCardEntity() && barCodeViewModel.showAddFidCard) {
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

                listFidCard.add(it)
                fidCardViewModel.updateRemoteListFidCard(
                    docID = userId,
                    listFidCard.associateBy({fidCard -> fidCard.value }, {fidCard -> fidCard.name +SEPARATOR+fidCard.barecodeformat +SEPARATOR+ fidCard.barecodetype })
                )


                fidCardViewModel.upsertFidCard(it)

            },
            onfidCardInfo = {
                barCodeViewModel.onfidCardInfo(it)
            }
        )
    }
}


    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onItemClick = {
                    productsViewModel.resetErreurValue()
                    barCodeViewModel.onfidCardInfo(FidCardEntity())
                    navController.navigate(it.route)
                },

                productsViewModel = productsViewModel
            )
        }

    ) { padding ->
        FidCardsMainScreen(
            padding = padding,
            navController = navController,
            barCodeViewModel = barCodeViewModel,
            fidCardViewModel = fidCardViewModel,
            userId  = userId,
            googleAuthUiClient = googleAuthUiClient,
            listFidCard = listFidCard,
            dataViewModel = dataViewModel
        )

    }


}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FidCardsMainScreen(
    padding: PaddingValues,
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    fidCardViewModel: FidCardViewModel,
    listFidCard :  ArrayList<FidCardEntity>,
    dataViewModel: DataViewModel,
    userId : String
) {
    val configuration = LocalConfiguration.current


    val context = LocalContext.current





    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

         Text(text = context.getString(R.string.Info_cartefid_activity))
            Spacer(modifier = Modifier.height(30.dp))

        Permission(
            permission = listOf(Manifest.permission.CAMERA),
            permissionNotAvailableContent = { permissionState->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Veuillez autoriser l'utilisation de la caméra")
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    ) {
                        Text("Autoriser l'accès à la caméra ")
                    }


                }
            },
            content = {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp),
                        onClick = {
                            navController.navigate(ListScreens.BarCodeCameraPreview.Route)
                        },

                        content = {
                            Text(text = context.getString(R.string.Ajoutezcartefidelite))
                        },

                        )

                    if(googleAuthUiClient.getSignedInUser() != null &&
                        /*DateUtils.durationSincePast(dateInPast = dataViewModel.getLastDateFidCardSync())*/
                        dataViewModel.nbrDaysSincFidCardSync >0)
                        ButtonWithBorder(
                            modifier = Modifier.fillMaxWidth(),
                            onClicks = {

                                if(userId!="NA"){
                                    fidCardViewModel.getRemoteFidCardBareCode(docID = userId)
                                }

                            },

                            text = "Get "+ DateUtils.durationSincePast(dateInPast = dataViewModel.getLastDateFidCardSync()) +" "+dataViewModel.getLastDateFidCardSync(),

                            )
                }

            }
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Lottie(R.raw.empty)
        fidCardViewModel.fidCards.let {
               if (it.isNotEmpty()) {
                   LazyColumn(
                       horizontalAlignment = Alignment.Start,
                       verticalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       items(it.size) { position ->

                           FidCardsListItem(
                               navController = navController,
                               barCodeViewModel =   barCodeViewModel,
                               barcod =   it[position],
                               fidCardViewModel = fidCardViewModel,
                               userId = userId

                           )
                       }
                   }
               }
               else LottieComposable(
                   size =  250.dp,
                   lottiefile = R.raw.empty
               )
           }












    }
}

@Composable
fun FidCardsListItem(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    barcod: FidCardEntity,
    fidCardViewModel: FidCardViewModel,
    userId : String
) {


    OutlinedCard(modifier = Modifier.padding(start = 9.dp, end = 9.dp)) {
        Row(
            modifier = Modifier
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
            Spacer(modifier = Modifier.width(9.dp))
            Text(text = barcod.name)


            Spacer(Modifier.weight(1f)) // height and background only for demonstration

            Image(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "",
                modifier = Modifier
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
                modifier = Modifier
                    .padding(end = 12.dp)
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


