package com.amirami.simapp.priceindicatortunisia.ui.componenet.producttype

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel

@Composable
fun ProductTypesListView(productsViewModel: ProductsViewModel){
    val context = LocalContext.current
    val item: Array<String> = context.resources.getStringArray(R.array.productFeauteredTypeArray)

    LazyRow(){
        items(item.size) { position ->
            ProductTypeSingleListItem(item[position],context,productsViewModel)
        }
    }

}

@Composable
fun ProductTypeSingleListItem(item: String, context: Context, productsViewModel: ProductsViewModel){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(imageTypeLoader(item,context)))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = false,
        isPlaying = false,
        iterations = Int.MAX_VALUE
    )

    Column(modifier= Modifier
        .clickable {

            productsViewModel.getProds("typesubsub", item)

            /**
            if (item == "Tous les Catégories") goToProductTypesDialog()
            else {
            searchtext = item
            searchtype = "typesubsub"
            recyclerview()

            _binding.mainTxVwInfo.visibility = View.GONE
            }
             */

            /**
            if (item == "Tous les Catégories") goToProductTypesDialog()
            else {
            searchtext = item
            searchtype = "typesubsub"
            recyclerview()

            _binding.mainTxVwInfo.visibility = View.GONE
            }
             */
        }
        .width(110.dp)
        .height(120.dp)) {

            LottieAnimation(
                modifier = Modifier
                    .size(75.dp),
                composition = composition,
                progress = { progress }
            )


            Text(
                text = item, modifier = Modifier.width(75.dp), fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

}

private fun imageTypeLoader(type:String,ctx: Context):Int{


    return if(type in ctx.resources.getStringArray(R.array.unit_of_mesur) &&type!="Oeufs" ) R.raw.unitmeasure
    else when (type){
        "Alimentation" -> R.raw.type_alimentation
        "Entretient de la Maison" -> R.raw.type_entretientdelamaison
        "Hygiène et Beauté" -> R.raw.type_hygieneetbeaute
        "Bébé" -> R.raw.type_bebe
        "Cuisine" -> R.raw.type_cuisine
        "Gros électroménager" -> R.raw.type_groselectromenager
        "Petit électroménager" -> R.raw.type_petitelectromenager
        "Animalerie" -> R.raw.type_animalerie
        "High-Tech" -> R.raw.type_hightech
        "Jardin" -> R.raw.type_jardin
        "Brico et Accessoires Auto" -> R.raw.type_bricoetaccessoiresauto
        "Fournitures Scolaires" -> R.raw.type_fournituresscolaires
        "Linge de Maison" -> R.raw.type_lingedemaison
        "Jeux et Jouets" -> R.raw.type_jeuxetjouets
        "Bagagerie" -> R.raw.type_bagagerie
        "Sport et Loisir" -> R.raw.type_sportetloisir
        "Oeufs"-> R.raw.subtype_oeufs
        //  in ctx.resources.getStringArray(R.array.unit_of_mesur)->R.raw.unitmeasure
        "Tous les Catégories"-> R.raw.allcategories
        else -> {
            R.raw.type_default
        }
    }
}
