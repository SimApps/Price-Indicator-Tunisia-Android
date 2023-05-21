package com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.ui.componenet.FlashLightComposable
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




    Box(modifier = Modifier.padding(12.dp)) {
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
                            barCodeViewModel.onShowAddFidCardChanged(true)
                         navController.navigateUp()

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


