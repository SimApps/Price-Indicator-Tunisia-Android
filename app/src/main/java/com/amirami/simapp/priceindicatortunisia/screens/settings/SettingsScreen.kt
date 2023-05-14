package com.amirami.simapp.priceindicatortunisia.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.ui.componenet.TextWithIcon
import com.amirami.simapp.priceindicatortunisia.ui.theme.Theme

@Composable
fun SettingsScreen(settingViewModel : SettingViewModel) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssistChip(
                onClick = { settingViewModel.setThemeMode(Theme.SYSTEM_THEME.theme) },
                label = { Text("System") },

                leadingIcon = {
                    AnimatedVisibility(visible = settingViewModel.darkTheme == Theme.SYSTEM_THEME.theme) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "System",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            AssistChip(
                onClick = { settingViewModel.setThemeMode(Theme.DARK.theme) },
                label = { Text("Dark") },
                leadingIcon = {
                    AnimatedVisibility(visible = settingViewModel.darkTheme == Theme.DARK.theme) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription ="Dark",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            AssistChip(
                onClick = { settingViewModel.setThemeMode(Theme.LIGHT.theme) },
                label = { Text("Light") },
                leadingIcon = {
                    AnimatedVisibility(visible = settingViewModel.darkTheme == Theme.LIGHT.theme) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Light",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
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