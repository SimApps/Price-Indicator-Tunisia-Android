package com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.util.Permission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.plcoding.bottomnavwithbadges.ui.componenet.cameraview.camera.executor
import com.plcoding.bottomnavwithbadges.ui.componenet.cameraview.camera.getCameraProvider
import com.plcoding.bottomnavwithbadges.ui.componenet.cameraview.camera.takePicture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalPermissionsApi
@ExperimentalCoroutinesApi
@Composable
fun CameraXScreen(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { },
    barCodeViewModel: BarCodeViewModel
) {
    val context = LocalContext.current

    Permission(
        permission = Manifest.permission.CAMERA,
        permissionNotAvailableContent = {
            Column(modifier) {
                Text("O noes! No Camera!")
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                        )
                    }
                ) {
                    Text("Open Settings")
                }
            } },
        content = {
            Box(modifier = modifier) {
                val lifecycleOwner = LocalLifecycleOwner.current
                val coroutineScope = rememberCoroutineScope()
                var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
                val imageCaptureUseCase by remember {
                    mutableStateOf(
                        ImageCapture.Builder()
                            .setFlashMode(ImageCapture.FLASH_MODE_ON)
                            .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                            .build()
                    )
                }
                if (barCodeViewModel.flashState) {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        onUseCase = {
                            previewUseCase = it
                        }
                    )
                } else CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onUseCase = {
                        previewUseCase = it
                    }
                )
                CapturePictureButton(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    onClick = {
                        coroutineScope.launch {
                            imageCaptureUseCase.takePicture(context.executor).let {
                                onImageFile(it)
                                //  onImageFile(createImageFile(context))

                                //     Log.d("eee","3"+ createImageFile(context).toUri().toString())
                            }
                        }
                    }
                )

                LaunchedEffect(previewUseCase) {
                    val cameraProvider = context.getCameraProvider()
                    try {
                        // Must unbind the use-cases before rebinding them.
                        cameraProvider.unbindAll()
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            previewUseCase,
                            imageCaptureUseCase
                        )
                        camera.cameraControl.enableTorch(barCodeViewModel.flashState)
                    } catch (ex: Exception) {
                        Log.e("CameraCapture", "Failed to bind camera use cases", ex)
                    }
                }
            }
        }
    )
}

fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageFileName = "JPEG_" + /*timeStamp + */"_"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )

    // Save a file: path for use with ACTION_VIEW intents
    //  mCurrentPhotoPath = image.absolutePath
    return image
}