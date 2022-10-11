package com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.amirami.simapp.priceindicatortunisia.ui.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.FidCardRoomViewModel
import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ADD_FID_CARD_SCREEN
import com.amirami.simapp.priceindicatortunisia.utils.generateBarCode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FidCardsScreen(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    fidCardRoomViewModel: FidCardRoomViewModel
) {


    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    ModalBottomSheetLayout(
        sheetContent = {
            AddFidCardBottomSheetLayout(
                barCodeViewModel = barCodeViewModel,
                modalBottomSheetState = modalBottomSheetState,
                fidCardRoomViewModel
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        //sheetBackgroundColor = colorResource(id = R.color.gray),
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
            FidCardsMainScreen(
                modifier = Modifier.padding(padding),
                navController = navController,
                barCodeViewModel = barCodeViewModel,
                modalBottomSheetState = modalBottomSheetState,
                fidCardRoomViewModel = fidCardRoomViewModel
            )


        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FidCardsMainScreen(
    modifier: Modifier,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    modalBottomSheetState: ModalBottomSheetState,
    fidCardRoomViewModel: FidCardRoomViewModel
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp


    val context = LocalContext.current
    val scope = rememberCoroutineScope()


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


        Log.d("ggf", "dd")


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
                               modalBottomSheetState,
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
                    scope.launch {
                        modalBottomSheetState.show()
                      //  fidCardsScreenViewModel.onfidCardActionInfo(Constants.FID_CARD_ACTION_RESETED)
                    }
                }
            }
            Spacer(modifier = modifier.height(30.dp))

            ButtonWithBorder(
                modifier = modifier.fillMaxWidth(),
                onClicks = {
                    barCodeViewModel.onsendBarCodeTo(ADD_FID_CARD_SCREEN)
                    navController.navigate(ListScreens.BarCodeCameraPreview.Route)
                },

                context.getString(R.string.Ajoutezcartefidelite),

                )

            Spacer(modifier = modifier.height(55.dp))


    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FidCardsListItem(
    modifier: Modifier,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    modalBottomSheetState: ModalBottomSheetState,
    barcod: FidCardEntity
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Card(
        elevation = 9.dp,
    ) {
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
                            scope.launch {
                                modalBottomSheetState.show()
                            }

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

                            scope.launch {
                                modalBottomSheetState.show()
                            }

                        }
                    )

                // .border(2.dp, Color.Black, CutCornerShape(12.dp))
                //  .background(Color.DarkGray, CutCornerShape(6.dp))
                //  .padding(18.dp)
            )

        }
    }

}


