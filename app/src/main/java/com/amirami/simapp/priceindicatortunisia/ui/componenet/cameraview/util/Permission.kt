package com.amirami.simapp.priceindicatortunisia.ui.componenet.cameraview.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permission(
    permission: List<String>,
    permissionNotAvailableContent: @Composable (permissionState: MultiplePermissionsState) -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val permissionState = rememberMultiplePermissionsState(permission)
    if (permissionState.allPermissionsGranted) {
        //   Text("Camera permission Granted")
        content()
    } else {

        permissionNotAvailableContent(permissionState)


    }
}



