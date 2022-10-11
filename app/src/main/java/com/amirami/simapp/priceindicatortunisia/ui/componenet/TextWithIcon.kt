package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextWithIcon(text: Int, icon: Int) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            //    .background(color = MaterialTheme.colorScheme.primary)
            .clickable { }.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            // painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "Sing In"
            // tint = MaterialTheme.colorScheme.onPrimary
        )

        //    Spacer(modifier = Modifier.padding(start = 6.dp))

        Text(
            context.getString(text),
            modifier = Modifier.padding(start = 9.dp)
        )
    }
}

@Composable
fun TextWithIconString(text: String, icon: Int) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = icon),
            // painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "Sing In"
        )

        //  Spacer(modifier = Modifier.padding(start = 6.dp))
        Text(text, modifier = Modifier.padding(start = 9.dp), fontSize = 12.sp)
    }
}
