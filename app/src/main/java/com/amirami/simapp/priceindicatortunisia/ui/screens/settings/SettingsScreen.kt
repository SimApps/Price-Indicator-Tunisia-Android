package com.amirami.simapp.priceindicatortunisia.ui.screens.settings

import com.amirami.simapp.priceindicatortunisia.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.ui.componenet.TextWithIcon

@Composable
fun SettingsScreen() {


    Column(
        modifier = Modifier
            .fillMaxSize()
           // .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(30.dp))
        TextWithIcon(R.string.Connecter, R.drawable.ic_signin)

        Spacer(modifier = Modifier.height(30.dp))
        TextWithIcon(R.string.nous_contacter_par_email, R.drawable.ic_send)

        Spacer(modifier = Modifier.height(30.dp))
        TextWithIcon(R.string.Qu_est_ce_que_application, R.drawable.ic_disclaimer)

        Spacer(modifier = Modifier.height(30.dp))
        TextWithIcon(R.string.more_application, R.drawable.ic_baseline_shop_24)

        Spacer(modifier = Modifier.height(30.dp))
        TextWithIcon(R.string.evaluez_application, R.drawable.ic_rate)

        Spacer(modifier = Modifier.height(30.dp))
        TextWithIcon(R.string.licenses, R.drawable.ic_licenses)

    }


}