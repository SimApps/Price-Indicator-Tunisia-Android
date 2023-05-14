package com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.core.Constants.FID_CARD_ACTION_ADD
import com.amirami.simapp.priceindicatortunisia.ui.componenet.FlashLightComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.util.Permission
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ADD_FID_CARD_SCREEN
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.HOME_SCREEN
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.SHOPPING_LIST_SCREEN
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun BarCodeCameraPreviewScreen(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel
) {
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition()
    val infinitelyAnimatedFloat = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(),
            // The value will infinitely repeat from 0 to 1 and 1 to 0
            repeatMode = RepeatMode.Reverse
        )
    )


    Box() {
        // Call FlashLightComposable
        if (barCodeViewModel.flashState) {
            BareCodeCameraView(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                barCodeViewModel = barCodeViewModel
            )
        } else BareCodeCameraView(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            barCodeViewModel = barCodeViewModel
        )

        FlashLightComposable(barCodeViewModel)

        Divider(
            color = Color.Red,
            modifier = Modifier
                //    .alpha(infinitelyAnimatedFloat.value)
                .fillMaxWidth()
                .align(Alignment.Center)
                .width(1.dp)

        )


        Text(
            modifier = Modifier
                //  .size(100.dp)
                .padding(bottom = 60.dp)
                .align(Alignment.BottomCenter),
            textAlign = TextAlign.Center,
            //  color = Color.Blue,
            text = context.getString(R.string.Placé_ligner_parallèle_numéro_code_barres_pour_scanner)
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BareCodeCameraView(
    modifier: Modifier,
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    AndroidView(
        factory = { AndroidViewContext ->
            PreviewView(AndroidViewContext).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { previewView ->


            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                    barcodes.forEach { barcode ->

                        barcode.rawValue?.let { barcodeValue ->
                            val fidcard = FidCardEntity(barcodeValue, barcode.format, barcode.valueType)
                            barCodeViewModel.onfidCardInfo(fidcard)

                            when (barCodeViewModel.sendBarCodeTo) {
                                ADD_FID_CARD_SCREEN -> {
                                    navController.navigate(ListScreens.CarteFidelite.Route)
                                    barCodeViewModel.onfidCardActionInfo(FID_CARD_ACTION_ADD)
                                }
                                HOME_SCREEN -> navController.navigate(ListScreens.Accueil.Route)
                                SHOPPING_LIST_SCREEN -> navController.navigate(ListScreens.Courses.Route)
                            }
                        }
                    }
                }
                val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                    }


                try {
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )


                    if (camera.cameraInfo.hasFlashUnit()) {
                        camera.cameraControl.enableTorch(barCodeViewModel.flashState)
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )

}


