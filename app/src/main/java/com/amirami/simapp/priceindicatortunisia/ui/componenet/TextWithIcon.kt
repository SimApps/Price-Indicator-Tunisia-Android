package com.amirami.simapp.priceindicatortunisia.ui.componenet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TextWithIcon(
    text: String,
    icon: Int,
    onClick :()->Unit = {}
) {

    Row(
        modifier = Modifier.height(30.dp).fillMaxWidth()
            .clickable {
                onClick()
            }.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = icon),
            // painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "Sing In"
            // tint = MaterialTheme.colorScheme.onPrimary
        )

        //    Spacer(modifier = Modifier.padding(start = 6.dp))

        Text(
            text,
            modifier = Modifier.padding(start = 9.dp)
        )
    }
}


