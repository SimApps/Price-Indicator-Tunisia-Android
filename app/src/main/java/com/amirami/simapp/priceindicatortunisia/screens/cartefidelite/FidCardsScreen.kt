@file:OptIn(ExperimentalMaterial3Api::class)

package com.amirami.simapp.priceindicatortunisia.screens.cartefidelite

import android.Manifest
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.FidCardRoomViewModel
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.EMPTY_IMAGE_URI
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.util.Permission
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ADD_FID_CARD_SCREEN
import com.amirami.simapp.priceindicatortunisia.utils.generateBarCode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@Composable
fun FidCardsScreen(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    fidCardRoomViewModel: FidCardRoomViewModel
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
if(barCodeViewModel.showAddFidCard)
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }
            barCodeViewModel.onShowAddFidCardChanged(false)

        },
    ) {
        AddFidCardBottomSheetLayout(
            barCodeViewModel = barCodeViewModel,
            fidCardRoomViewModel
        )
    }

    FidCardsMainScreen(
        modifier = Modifier,
        navController = navController,
        barCodeViewModel = barCodeViewModel,
        fidCardRoomViewModel = fidCardRoomViewModel
    )


}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FidCardsMainScreen(
    modifier: Modifier,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    fidCardRoomViewModel: FidCardRoomViewModel
) {
    val configuration = LocalConfiguration.current


    val context = LocalContext.current


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = true,
        iterations = Int.MAX_VALUE
    )

    Column(
        modifier = modifier
            .fillMaxSize()
         //   .background(Color.White)
            .padding(16.dp),
            //  .verticalScroll(rememberScrollState())
          //  .wrapContentHeight(),
        verticalArrangement = Arrangement.Top,
        //   horizontalAlignment = Alignment.Start
    ) {

         Text(text = context.getString(R.string.Info_cartefid_activity))
            Spacer(modifier = Modifier.height(30.dp))



        // Lottie(R.raw.empty)
        fidCardRoomViewModel.fidCards.let {
               if (it.isNotEmpty()) {
                   LazyColumn(
                       modifier = modifier
                           .height(300.dp)
                           //.fillMaxSize()
                         //  .wrapContentHeight()
                         //  .background(Color.Gray)
                       //    .padding(10.dp)
                   ,verticalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       items(it.size) { position ->

                           FidCardsListItem(
                               //  icon = R.drawable.ic_share,
                               modifier,
                               navController,
                               barCodeViewModel,
                               it[position]

                           )
                       }
                   }
               }
               else LottieAnimation(
                   modifier= modifier.size(250.dp),
                   composition = composition,
                   progress = { progress },
               )
           }




            /**
             * to delete when find better solution to show bottom sheet after scan SEE ALSO NAVIGATION
             */
            if (barCodeViewModel.fidCardAction != Constants.FID_CARD_ACTION_RESETED) {
                LaunchedEffect(key1 = context) {
                    barCodeViewModel.onShowAddFidCardChanged(true)
                }
            }
            Spacer(modifier = modifier.height(30.dp))
        Permission(
            permission = listOf(Manifest.permission.CAMERA),
            permissionNotAvailableContent = { permissionState->
                Column(modifier) {
                    androidx.compose.material.Text("O noes! No Photo Gallery!")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        androidx.compose.material.Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                permissionState.launchMultiplePermissionRequest()

                                /*  context.startActivity(
                                      Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                          data = Uri.fromParts("package", context.packageName, null)
                                      }
                                  )*/
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
                        barCodeViewModel.onsendBarCodeTo(ADD_FID_CARD_SCREEN)
                        navController.navigate(ListScreens.BarCodeCameraPreview.Route)
                    },

                    context.getString(R.string.Ajoutezcartefidelite),

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
                            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_DELETE)
                            barCodeViewModel.onShowAddFidCardChanged(true)

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
                            barCodeViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_MODIFY)

                            barCodeViewModel.onShowAddFidCardChanged(true)

                        }
                    )

                // .border(2.dp, Color.Black, CutCornerShape(12.dp))
                //  .background(Color.DarkGray, CutCornerShape(6.dp))
                //  .padding(18.dp)
            )

        }
    }

}


