package com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ButtonWithBorder
import com.amirami.simapp.priceindicatortunisia.ui.componenet.TextWithIcon
import com.amirami.simapp.priceindicatortunisia.ui.componenet.ToggleBotton
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.logopalcer
import com.amirami.simapp.priceindicatortunisia.utils.Functions.sortPrices

@Composable
fun ProductDetailDilogScreen(
    product: ProductModel,
    onSelectionChange: (String) -> Unit,
    onModifyClick: () -> Unit,
    selectedOption : String
) {
    val context = LocalContext.current


    Column(
        modifier = Modifier
            //   .fillMaxSize()
            .wrapContentHeight()
            .padding(16.dp)
            .padding(bottom = 60.dp)
            .verticalScroll(rememberScrollState())
        //   verticalArrangement = Arrangement.SpaceEvenly,
        //   horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(10.dp)),
                model = product.imageurl,
                contentDescription = "stringResource(R.string.description)"
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator()
                    }
                    is AsyncImagePainter.State.Error -> {
                        SubcomposeAsyncImageContent(
                            painter = painterResource(id = R.drawable.ic_close)
                        )
                    }
                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }

            Column(modifier = Modifier.wrapContentSize()) {
                Text(text = product.sieze)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(text = product.name)
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = typeToTxt(product))

        Spacer(modifier = Modifier.height(10.dp))
        ToggleBotton(
            selectedOption = selectedOption,
            onSelectionChange = {
                onSelectionChange(it)

            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        when (selectedOption) {
            context.getString(R.string.Prix) -> ShowProductWithoutBill(context, product)
            context.getString(R.string.Promotions) -> ShowremarqNoBill(context, product)
            context.getString(R.string.Bonus) -> ShowBonusNoBill(context, product)
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
            Button(
                modifier = Modifier.wrapContentWidth(),
                onClick = {
                    onModifyClick()

                })
               {
             Text(text =  context.getString(R.string.Modifier)  )
                }



            if (product.monoprixPriceHistory.size >= 2 ||
                product.mgpriceHistory.size >= 2 ||
                product.azizaPriceHistory.size >= 2 ||
                product.carrefourPriceHistory.size >= 2 ||
                product.geantPriceHistory.size >= 2
            ) {
                ButtonWithBorder(
                    modifier = Modifier.wrapContentWidth(),
                    onClicks = { /*TODO*/ },
                    text = context.getString(R.string.Historique_des_prix)
                )
            }
        }
    }
}

fun typeToTxt(product: ProductModel): String {
    return if (product.typesub != "" && product.typesubsub != "") product.type + " -> " + product.typesub + " -> " + product.typesubsub
    else if (product.typesub != "") product.type + " -> " + product.typesub
    else product.type
}

@Composable
fun ShowProductWithoutBill(context: Context, product: ProductModel) {
    when {
        product.name.contains(context.getString(R.string.carrefour)) -> {
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                product.carrefourprice,
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    product.carrefourprice,
                    true
                ) + Functions.showRestOfString(Functions.shortformateDate(product.carrefourmodifdate)) else context.getString(
                    R.string.NA
                ),

                icon = logopalcer(
                    context,
                    context.getString(R.string.carrefour)
                )
            )
        }
        product.name.contains(context.getString(R.string.monoprix)) -> {
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                product.monoprixprice,
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    product.monoprixprice,
                    true
                ) + Functions.showRestOfString(Functions.shortformateDate(product.monoprixmodifdate)) else context.getString(
                    R.string.NA
                ),

                icon = logopalcer(
                    context,
                    context.getString(R.string.monoprix)
                )
            )
        }
        product.name.contains(context.getString(R.string.Géant)) -> {
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                product.geantprice,
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    product.geantprice,
                    true
                ) + Functions.showRestOfString(Functions.shortformateDate(product.geantmodifdate)) else context.getString(
                    R.string.NA
                ),

                icon = logopalcer(
                    context,
                    context.getString(R.string.Géant)
                )
            )
        }
        product.name.contains(context.getString(R.string.mg)) -> {
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                product.mgprice,
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    product.mgprice,
                    true
                ) + Functions.showRestOfString(Functions.shortformateDate(product.mgmodifdate)) else context.getString(
                    R.string.NA
                ),

                icon = logopalcer(
                    context,
                    context.getString(R.string.mg)
                )
            )
        }
        product.name.contains(context.getString(R.string.azziza)) -> {
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                product.azzizaprice,
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    product.azzizaprice,
                    true
                ) + Functions.showRestOfString(Functions.shortformateDate(product.azzizamodifdate)) else context.getString(
                    R.string.NA
                ),

                icon = logopalcer(
                    context,
                    context.getString(R.string.azziza)
                )
            )
        }
        else -> {
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                sortPrices(context, product)[0].first.toString(),
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    sortPrices(context, product)[0].first.toString(),
                    true
                ) + priceDateTowhomNoBill(
                    context,
                    sortPrices(context, product)[0].second,
                    product
                ) else context.getString(R.string.NA),
                icon = logopalcer(context, sortPrices(context, product)[0].second)
            )
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                sortPrices(context, product)[1].first.toString(),
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    sortPrices(context, product)[1].first.toString(),
                    true
                ) + priceDateTowhomNoBill(
                    context,
                    sortPrices(context, product)[1].second,
                    product
                ) else context.getString(R.string.NA),
                icon = logopalcer(context, sortPrices(context, product)[1].second)
            )
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                sortPrices(context, product)[2].first.toString(),
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    sortPrices(context, product)[2].first.toString(),
                    true
                ) + priceDateTowhomNoBill(
                    context,
                    sortPrices(context, product)[2].second,
                    product
                ) else context.getString(R.string.NA),
                icon = logopalcer(context, sortPrices(context, product)[2].second)
            )
            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                sortPrices(context, product)[3].first.toString(),
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    sortPrices(context, product)[3].first.toString(),
                    true
                ) + priceDateTowhomNoBill(
                    context,
                    sortPrices(context, product)[3].second,
                    product
                ) else context.getString(R.string.NA),
                icon = logopalcer(context, sortPrices(context, product)[3].second)
            )

            TextWithIcon(
                text = if (checkifPrixIsNorixtEmpty(
                        context,
                        Functions.pricenotdefined(
                                context,
                                sortPrices(context, product)[4].first.toString(),
                                true
                            )
                    )
                ) Functions.pricenotdefined(
                    context,
                    sortPrices(context, product)[4].first.toString(),
                    true
                ) + priceDateTowhomNoBill(
                    context,
                    sortPrices(context, product)[4].second,
                    product
                ) else context.getString(R.string.NA),
                icon = logopalcer(context, sortPrices(context, product)[4].second)
            )
        }
    }
}

@Composable
fun ShowremarqNoBill(context: Context, product: ProductModel) {
    when {
        product.name.contains(context.getString(R.string.carrefour)) -> {
            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.carrefourremarq
                    )
                ) Functions.showStringifNotEmpty(
                    product.carrefourremarq,
                    ""
                ) + " : " + Functions.shortformateDate(product.carrefourmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.logo_carrefour
            )
        }
        product.name.contains(context.getString(R.string.monoprix)) -> {
            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.monoprixremarq
                    )
                ) product.monoprixremarq + " : " + Functions.shortformateDate(
                    product.monoprixremarqmodifdate
                ) else context.getString(R.string.NA),
                icon = R.drawable.ic_monoprix_logo
            )
        }
        product.name.contains(context.getString(R.string.Géant)) -> {
            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.geantremarq
                    )
                ) Functions.showStringifNotEmpty(
                    product.geantremarq,
                    ""
                ) + " : " + Functions.shortformateDate(product.geantremarqmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.geantlogo
            )
        }
        product.name.contains(context.getString(R.string.mg)) -> {
            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.mgremarq
                    )
                ) Functions.showStringifNotEmpty(
                    product.mgremarq,
                    ""
                ) + " : " + Functions.shortformateDate(product.mgremarqmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.mglogo
            )
        }
        product.name.contains(context.getString(R.string.azziza)) -> {
            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.azzizaremarq
                    )
                ) Functions.showStringifNotEmpty(
                    product.azzizaremarq,
                    ""
                ) + " : " + Functions.shortformateDate(product.azzizaremarqmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.azizalogo
            )
        }
        else -> {
            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.monoprixremarq
                    )
                ) product.monoprixremarq + " : " + Functions.shortformateDate(
                    product.monoprixremarqmodifdate
                ) else context.getString(R.string.NA),
                icon = R.drawable.ic_monoprix_logo
            )

            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.mgremarq
                    )
                ) Functions.showStringifNotEmpty(
                    product.mgremarq,
                    ""
                ) + " : " + Functions.shortformateDate(product.mgremarqmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.mglogo
            )

            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.carrefourremarq
                    )
                ) Functions.showStringifNotEmpty(
                    product.carrefourremarq,
                    ""
                ) + " : " + Functions.shortformateDate(product.carrefourmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.logo_carrefour
            )

            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.azzizaremarq
                    )
                ) Functions.showStringifNotEmpty(
                    product.azzizaremarq,
                    ""
                ) + " : " + Functions.shortformateDate(product.azzizaremarqmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.azizalogo
            )

            TextWithIcon(
                text = if (checkifPromtionIsNotEmpty(
                        context,
                        product.geantremarq
                    )
                ) Functions.showStringifNotEmpty(
                    product.geantremarq,
                    ""
                ) + " : " + Functions.shortformateDate(product.geantremarqmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.geantlogo
            )
        }
    }
}

@Composable
fun ShowBonusNoBill(context: Context, product: ProductModel) {
    when {
        product.name.contains(context.getString(R.string.carrefour)) -> {
            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.carrefourbonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.carrefourbonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.carrefourbonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.logo_carrefour
            )
        }
        product.name.contains(context.getString(R.string.monoprix)) -> {
            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.monoprixbonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.monoprixbonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.monoprixbonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.ic_monoprix_logo
            )
        }
        product.name.contains(context.getString(R.string.Géant)) -> {
            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.geantbonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.geantbonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.geantbonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.geantlogo
            )
        }
        product.name.contains(context.getString(R.string.mg)) -> {
            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.mgbonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.mgbonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.mgbonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.mglogo
            )
        }
        product.name.contains(context.getString(R.string.azziza)) -> {
            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.azzizabonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.azzizabonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.azzizabonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.azizalogo
            )
        }
        else -> {
            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.monoprixbonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.monoprixbonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.monoprixbonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.ic_monoprix_logo
            )

            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.mgbonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.mgbonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.mgbonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.mglogo
            )

            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.carrefourbonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.carrefourbonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.carrefourbonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.logo_carrefour
            )

            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.azzizabonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.azzizabonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.azzizabonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.azizalogo
            )

            TextWithIcon(
                text = if (checkifBonusIsNotEmpty(product.geantbonusfid)) Functions.showStringifNotEmpty(
                    Functions.pricenotdefined(context, product.geantbonusfid, false),
                    "fidbonustring"
                ) + " : " + Functions.shortformateDate(product.geantbonusfidmodifdate) else context.getString(
                    R.string.NA
                ),
                icon = R.drawable.geantlogo
            )
        }
    }
}

private fun checkifBonusIsNotEmpty(remarque: String): Boolean {
    return remarque != ""
}

private fun checkifPromtionIsNotEmpty(context: Context, remarque: String): Boolean {
    return !(remarque == context.getString(R.string.ajouter_promotions) || remarque == "")
}

/*
private fun pricesTxtVisibilite(context: Context,product: Product, from:String){
    if(from=="prix") {
        if(product.name.contains(context.getString(R.string.carrefour)) ||
            product.name.contains(context.getString(R.string.monoprix))||
            product.name.contains(context.getString(R.string.azziza))||
            product.name.contains(context.getString(R.string.mg))||
            product.name.contains(context.getString(R.string.Géant))){

            _binding?.apply {

                //    fiestpriceImagevwPanel.visibility = View.VISIBLE
                secondpriceImagevwPanel.visibility = View.GONE
                thirdpriceImagevwPanel.visibility = View.GONE
                fourthpriceImagevwPanel.visibility = View.GONE
                fifthpriceImagevwPanel.visibility = View.GONE

                //   prodFirstpriceSpanel.visibility = View.VISIBLE
                prodSecondpriceSpanel.visibility = View.GONE
                prodThirdpriceSpanel.visibility = View.GONE
                prodForthpriceSpanel.visibility = View.GONE
                prodFifthpriceSpanel.visibility = View.GONE
            }

        }
    }
    else {
        _binding?.apply {
            fiestpriceImagevwPanel.setImageResource(R.drawable.azizalogo)
            secondpriceImagevwPanel.setImageResource(R.drawable.logo_carrefour)
            thirdpriceImagevwPanel.setImageResource(R.drawable.ic_monoprix_logo)
            fourthpriceImagevwPanel.setImageResource(R.drawable.mglogo)
            fifthpriceImagevwPanel.setImageResource(R.drawable.geantlogo)
        }
        when {
            product.name.contains(context.getString(R.string.carrefour)) -> {
                _binding?.apply {
                    fiestpriceImagevwPanel.visibility = View.GONE
                    prodFirstpriceSpanel.visibility = View.GONE

                    thirdpriceImagevwPanel.visibility = View.GONE
                    prodThirdpriceSpanel.visibility = View.GONE

                    fourthpriceImagevwPanel.visibility = View.GONE
                    prodForthpriceSpanel.visibility = View.GONE

                    fifthpriceImagevwPanel.visibility = View.GONE
                    prodFifthpriceSpanel.visibility = View.GONE

                }
            }
            product.name.contains(context.getString(R.string.monoprix)) -> {
                _binding?.apply {

                    fiestpriceImagevwPanel.visibility = View.GONE
                    prodFirstpriceSpanel.visibility = View.GONE

                    secondpriceImagevwPanel.visibility = View.GONE
                    prodSecondpriceSpanel.visibility = View.GONE

                    fourthpriceImagevwPanel.visibility = View.GONE
                    prodForthpriceSpanel.visibility = View.GONE

                    fifthpriceImagevwPanel.visibility = View.GONE
                    prodFifthpriceSpanel.visibility = View.GONE

                }
            }
            product.name.contains(context.getString(R.string.Géant)) -> {
                _binding?.apply {
                    fiestpriceImagevwPanel.visibility = View.GONE
                    prodFirstpriceSpanel.visibility = View.GONE

                    secondpriceImagevwPanel.visibility = View.GONE
                    prodSecondpriceSpanel.visibility = View.GONE

                    fourthpriceImagevwPanel.visibility = View.GONE
                    prodForthpriceSpanel.visibility = View.GONE

                    thirdpriceImagevwPanel.visibility = View.GONE
                    prodThirdpriceSpanel.visibility = View.GONE


                }
            }
            product.name.contains(context.getString(R.string.mg)) -> {
                _binding?.apply {
                    thirdpriceImagevwPanel.visibility = View.GONE
                    prodThirdpriceSpanel.visibility = View.GONE

                    secondpriceImagevwPanel.visibility = View.GONE
                    prodSecondpriceSpanel.visibility = View.GONE

                    fourthpriceImagevwPanel.visibility = View.GONE
                    prodForthpriceSpanel.visibility = View.GONE

                    fifthpriceImagevwPanel.visibility = View.GONE
                    prodFifthpriceSpanel.visibility = View.GONE

                }
            }
            product.name.contains(context.getString(R.string.azziza)) -> {
                _binding?.apply {
                    thirdpriceImagevwPanel.visibility = View.GONE
                    prodThirdpriceSpanel.visibility = View.GONE

                    secondpriceImagevwPanel.visibility = View.GONE
                    prodSecondpriceSpanel.visibility = View.GONE

                    fourthpriceImagevwPanel.visibility = View.GONE
                    prodForthpriceSpanel.visibility = View.GONE

                    fifthpriceImagevwPanel.visibility = View.GONE
                    prodFifthpriceSpanel.visibility = View.GONE

                }
            }
            else -> {
                _binding?.apply {
                    fiestpriceImagevwPanel.visibility = View.VISIBLE
                    prodFirstpriceSpanel.visibility = View.VISIBLE

                    thirdpriceImagevwPanel.visibility = View.VISIBLE
                    prodThirdpriceSpanel.visibility = View.VISIBLE

                    secondpriceImagevwPanel.visibility = View.VISIBLE
                    prodSecondpriceSpanel.visibility = View.VISIBLE

                    fourthpriceImagevwPanel.visibility = View.VISIBLE
                    prodForthpriceSpanel.visibility = View.VISIBLE

                    fifthpriceImagevwPanel.visibility = View.VISIBLE
                    prodFifthpriceSpanel.visibility = View.VISIBLE

                }
            }


        }

    }

}




*/
private fun priceDateTowhomNoBill(
    context: Context,
    magasin: String,
    product: ProductModel
): String {
    return when (magasin) {
        context.getString(R.string.monoprix) -> Functions.showRestOfString(
            Functions.shortformateDate(
                product.monoprixmodifdate
            )
        )
        context.getString(R.string.mg) -> Functions.showRestOfString(
            Functions.shortformateDate(
                product.mgmodifdate
            )
        )
        context.getString(R.string.carrefour) -> Functions.showRestOfString(
            Functions.shortformateDate(
                product.carrefourmodifdate
            )
        )
        context.getString(R.string.azziza) -> Functions.showRestOfString(
            Functions.shortformateDate(
                product.azzizamodifdate
            )
        )
        context.getString(R.string.Géant) -> Functions.showRestOfString(
            Functions.shortformateDate(
                product.geantmodifdate
            )
        )
        else -> {
            ""
        }
    }
}

private fun checkifPrixIsNorixtEmpty(context: Context, remarque: String): Boolean {
    return remarque != context.getString(R.string.NA)
}
