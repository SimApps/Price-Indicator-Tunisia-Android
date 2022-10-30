package com.amirami.simapp.priceindicatortunisia.ui.componenet

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.core.Constants
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.productinfodialog.ProductDetailDialogViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.logopalcer
import com.amirami.simapp.priceindicatortunisia.utils.Functions.pricenotdefined
import com.amirami.simapp.priceindicatortunisia.utils.Functions.sortPrices

@Composable
fun ProductList(
    prodsResponse: MutableList<ProductModel>,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productsViewModel: ProductsViewModel
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .height(500.dp)
            // .fillMaxWidth()
            .padding(bottom = 60.dp),
        //   .fillMaxHeight()
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(prodsResponse.size) { position ->

            ProductTiket(
                prodsResponse = prodsResponse[position],
                productDetailDialogViewModel = productDetailDialogViewModel,
                productsViewModel = productsViewModel)
        }
    }
}

@Composable
fun ProductTiket(
    prodsResponse: ProductModel,
    productDetailDialogViewModel: ProductDetailDialogViewModel,
    productsViewModel: ProductsViewModel
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            //  .fillMaxWidth()
            // .fillMaxHeight()
            .fillMaxWidth()
            .clickable {
                if (productDetailDialogViewModel.prodDetailDialogVisibilityStates) {
                    productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(false)
                } else productDetailDialogViewModel.onprodDetailDialogVisibilityStatesChanged(true)

                productDetailDialogViewModel.onprodDetailDialogStatesChanged(product = prodsResponse)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .weight(2f)
                .size(85.dp)
                .clip(RoundedCornerShape(6.dp)),
            model = prodsResponse.imageurl,
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

        //  Spacer(modifier = Modifier.padding(start = 12.dp))

        Column(
            modifier = Modifier
                .weight(5f)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = prodsResponse.name,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 9.dp),
                softWrap = true
            )

            TextWithIconString(
                PricesAndLogos(context, prodsResponse)[0].first,
                PricesAndLogos(
                    context,
                    prodsResponse
                )[0].second
            )
        }

        //     Spacer(modifier = Modifier.padding(start = 12.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_add_to_cart),
            contentDescription = "",
            Modifier
                .weight(1f)
                // .size(35.dp)
                .clickable {
                    if (prodsResponse.typesub == Constants.PRODUITS_FRAIS) prodsResponse.quantity = 0.1
                    else prodsResponse.quantity = 1.0
                    productsViewModel.AddProdToShopList(prodsResponse)
                }
        )

        //  Spacer(Modifier.weight(1f))
    }
}

fun PricesAndLogos(
    context: Context,
    prodsResponse: ProductModel
): MutableList<Pair<String, Int>> {
    return when {
        prodsResponse.name.contains(context.getString(R.string.carrefour)) ->
            mutableListOf(
                pricenotdefined(
                    context,
                    prodsResponse.carrefourprice,
                    true
                ) to R.drawable.logo_carrefour
            )

        prodsResponse.name.contains(context.getString(R.string.monoprix)) ->
            mutableListOf(
                pricenotdefined(
                    context,
                    prodsResponse.monoprixprice,
                    true
                ) to R.drawable.ic_monoprix_logo
            )

        prodsResponse.name.contains(context.getString(R.string.Géant)) ->
            mutableListOf(
                pricenotdefined(
                    context,
                    prodsResponse.geantprice,
                    true
                ) to R.drawable.geantlogo
            )

        prodsResponse.name.contains(context.getString(R.string.mg)) ->
            mutableListOf(
                pricenotdefined(
                    context,
                    prodsResponse.mgprice,
                    true
                ) to R.drawable.mglogo
            )

        prodsResponse.name.contains(context.getString(R.string.azziza)) ->
            mutableListOf(
                pricenotdefined(
                    context,
                    prodsResponse.azzizaprice,
                    true
                ) to R.drawable.azizalogo
            )

        else -> {
            mutableListOf(
                pricenotdefined(
                    context,
                    sortPrices(context, prodsResponse)[0].first.toString(),
                    true
                ) + Functions.showRestOfString(
                    priceDate(
                        context,
                        sortPrices(context, prodsResponse)[0].second,
                        prodsResponse
                    )
                ) to logopalcer(
                    context,
                    sortPrices(context, prodsResponse)[0].second
                )
            )
        }
    }
}

fun priceDate(context: Context, magasin: String, product: ProductModel): String {
    var remarque = ""

    when (magasin) {
        context.getString(R.string.monoprix) -> {
            remarque = Functions.showStringifNotEmpty(
                Functions.shortformateDate(product.monoprixmodifdate),
                ""
            )
        }
        context.getString(R.string.mg) -> {
            remarque = Functions.showStringifNotEmpty(
                Functions.shortformateDate(product.mgmodifdate),
                ""
            )
        }
        context.getString(R.string.carrefour) -> {
            remarque = Functions.showStringifNotEmpty(
                Functions.shortformateDate(product.carrefourmodifdate),
                ""
            )
        }
        context.getString(R.string.azziza) -> {
            remarque = Functions.showStringifNotEmpty(
                Functions.shortformateDate(product.azzizamodifdate),
                ""
            )
        }
        context.getString(R.string.Géant) -> {
            remarque = Functions.showStringifNotEmpty(
                Functions.shortformateDate(product.geantmodifdate),
                ""
            )
        }
    }

    return remarque
}
