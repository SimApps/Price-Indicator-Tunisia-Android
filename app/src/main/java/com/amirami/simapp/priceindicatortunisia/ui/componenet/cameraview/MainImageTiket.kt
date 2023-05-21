package com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.ui.componenet.FlashLightComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar.BottomNavigationBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.camera.CameraXScreen
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.gallery.GallerySelect
import com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar.TopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun MainImageTiket(
    navController: NavHostController,
    productsViewModel : ProductsViewModel,
    cameraViewModel: CameraViewModel,
    barCodeViewModel: BarCodeViewModel
) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gallery))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = false,
        //  iterations = Int.MAX_VALUE
    )
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

        if (cameraViewModel.imageUri != EMPTY_IMAGE_URI) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(cameraViewModel.imageUri),
                    contentDescription = "Captured image"
                )

                Row(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    ButtonWithBorder(
                        modifier = Modifier.wrapContentWidth(),
                        onClicks = {
                            cameraViewModel.onimageUri(EMPTY_IMAGE_URI)
                        },
                        text = "Remove image",
                    )

                    ButtonWithBorder(
                        modifier = Modifier.wrapContentWidth(),
                        onClicks = {
                            navController.navigate(ListScreens.Tiket.Route)
                        },
                        text = "Confirm Image",
                    )
                }
            }


        } else {
            if (cameraViewModel.showGallerySelect) {
                GallerySelect(
                    onImageUri = { uri ->
                        cameraViewModel.onshowGallerySelect(false)
                        cameraViewModel.onimageUri(uri)
                    }
                )
            } else {
                Box() {
                    CameraXScreen(
                        onImageFile = { file ->
                            cameraViewModel.onimageUri(file.toUri())
                            //  Log.d("eee","2"+ file.toUri().toString())
                            //  cameraViewModel.onimageUri(createImageFile(context).toUri())

                        },
                        barCodeViewModel = barCodeViewModel
                    )
                    FlashLightComposable(barCodeViewModel)
                    LottieAnimation(
                        modifier = Modifier.size(70.dp)
                            .padding(16.dp)
                            .align(Alignment.BottomEnd)
                            .clickable { cameraViewModel.onshowGallerySelect(true) },
                        composition = composition,
                        progress = { progress }
                    )


                }
            }
        }
    }
}

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")


