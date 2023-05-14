@file:OptIn(ExperimentalPermissionsApi::class)

package com.amirami.simapp.priceindicatortunisia.screens.tiket

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.ui.componenet.LottieComposable
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.CameraViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.EMPTY_IMAGE_URI
import com.amirami.simapp.priceindicatortunisia.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.util.Permission
import com.amirami.simapp.priceindicatortunisia.utils.Constants
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@Composable
fun TiketScreen(
    padding: PaddingValues,
    navController: NavHostController,
    tiketViewModel: TiketViewModel = hiltViewModel(),
    cameraViewModel: CameraViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            // .background(Color.White)
            .padding(padding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = context.getString(R.string.tiketadvise)
        )
        Spacer(modifier = Modifier.height(30.dp))

        //     OutlinedEditText(text = tiketViewModel.text,labl = R.string.Commentaires,tiketViewModel.ontext(it))
        OutlinedEditText(tiketViewModel)

        Spacer(modifier = Modifier.height(30.dp))

        if (cameraViewModel.imageUri != EMPTY_IMAGE_URI) {
            AsyncImage(
                model = cameraViewModel.imageUri,
                modifier = Modifier.fillMaxSize(),
                contentDescription = ""
            )
        } else LottieComposable(250.dp, R.raw.mobilecamera)


        Spacer(modifier = Modifier.height(30.dp))
        Permission(
            permission = listOf(Manifest.permission.CAMERA),
            permissionNotAvailableContent = { permissionState->
                Column() {
                    Text("O noes! No Photo Gallery!")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(
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
                            Text("Open Settings")
                        }

                    }
                }
            },
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ButtonWithBorder(
                        modifier = Modifier.wrapContentWidth(),
                        onClicks = {
                            navController.navigate(ListScreens.MainImageTiket.Route)
                        },
                        if (cameraViewModel.imageUri != EMPTY_IMAGE_URI) context.getString(R.string.prendreautrephotos)
                        else context.getString(R.string.prendrephotos)

                    )

                    if (cameraViewModel.imageUri != EMPTY_IMAGE_URI) {
                        ButtonWithBorder(
                            modifier = Modifier.wrapContentWidth(),
                            onClicks = {
                                shareImageandText(context, cameraViewModel, tiketViewModel)
                            },
                            context.getString(R.string.Envoyer)
                        )
                    }
                }
            })

    }
}


@Composable
fun OutlinedEditText(tiketViewModel: TiketViewModel) {
    val context = LocalContext.current

    OutlinedTextField(
        value = tiketViewModel.text,
        onValueChange = { tiketViewModel.ontext(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(context.getString(R.string.Commentaires)) }
    )
}


private fun shareImageandText(
    context: Context,
    cameraViewModel: CameraViewModel,
    tiketViewModel: TiketViewModel
) {
    //  val builder = VmPolicy.Builder()
    // StrictMode.setVmPolicy(builder.build())
    val uri = if (cameraViewModel.imageUri.toString()
        .contains("cache")
    ) getmageToShare(context/*,bitmap: Bitmap*/, cameraViewModel)
    else cameraViewModel.imageUri

    val intent = Intent(Intent.ACTION_SEND)

    // putting uri of image to be shared
    intent.putExtra(Intent.EXTRA_STREAM, uri)

    // adding text to share
    intent.putExtra(
        /* Intent.EXTRA_EMAIL,*/ Intent.EXTRA_TEXT,
        context.getString(R.string.propose_liste_produits, tiketViewModel.text, "user_id")
    )

    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.app_email)))


    // Add subject Here
    intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.Partager_image))

    // setting type to image
    intent.type = "image/png" // "message/rfc822"//"text/plain"
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    // calling startactivity() to share
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.choisir_email)))
}


// Retrieving the url to share
private fun getmageToShare(
    context: Context/*,bitmap: Bitmap*/,
    cameraViewModel: CameraViewModel
): Uri? {
    // val imagefolder = File(context.cacheDir, "images")
    var uri: Uri? = null
    try {
        //   imagefolder.mkdirs()

        /*   val outputStream = FileOutputStream(cameraViewModel.imageUri.path)
           bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
           outputStream.flush()
           outputStream.close()*/
        //  uri = FileProvider.getUriForFile(context, context.getString(R.string.pakege_fileprovider), cameraViewModel.imageUri.toFile())

        /*
           file:///data/user/0/com.plcoding.bottomnavwithbadges/cache/image5674268572578385108jpg
            */
        context.grantUriPermission(
            "com.amirami.simapp.priceindicatortunisia",
            cameraViewModel.imageUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        //   val cacheFile = File(context.cacheDir,  cameraViewModel.imageUri.lastPathSegment!!)

        //     uri = FileProvider.getUriForFile(context, context.getString(R.string.pakege_fileprovider), cacheFile)

        uri = FileProvider.getUriForFile(
            context,
            context.getString(R.string.pakege_fileprovider),
            cameraViewModel.imageUri.toFile().absoluteFile
        )
    } catch (e: java.lang.Exception) {
        e.message?.let { /*errorToast(context, it)*/
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
    return uri
}



