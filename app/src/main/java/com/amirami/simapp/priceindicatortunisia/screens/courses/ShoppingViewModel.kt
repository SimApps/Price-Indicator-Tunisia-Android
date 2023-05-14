package com.amirami.simapp.priceindicatortunisia.screens.courses

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.core.Magazin
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import java.math.BigDecimal
import javax.inject.Inject


class ShoppingViewModel @Inject constructor() : ViewModel() {


    var calculateDiscount by mutableStateOf(false)
        private set

    fun onCalculateDiscountChange(value: Boolean) {
        calculateDiscount = value
    }


    var prices = mutableStateListOf<Prices>()
        private set

    //calculate price begin
    /*     var monoprixpriceBill by mutableStateOf("")
           private set
       var mgBill by mutableStateOf("")
           private set
       var azizaBill by mutableStateOf("")
           private set
       var carrefourBill by mutableStateOf("")
           private set

       var geantBill by mutableStateOf("")
           private set


       var nbrMissing_monoprixprice by mutableStateOf(0)
           private set

       var nbrMissing_mgprice by mutableStateOf(0)
           private set

       var nbrMissing_carrefourprice by mutableStateOf(0)
           private set

       var nbrMissing_azzizaprice by mutableStateOf(0)
           private set


       var nbrMissing_geantprice by mutableStateOf(0)
           private set

       var monoprixBonusfid by mutableStateOf(0.0)
           private set

       var mgBonusfid by mutableStateOf(0.0)
           private set
       var carrefourBonusfid by mutableStateOf(0.0)
           private set
       var azzizaBonusfid by mutableStateOf(0.0)
           private set
       var geantBonusfid by mutableStateOf(0.0)
           private set*/

    fun calculatePrices(
        context: Context,
        product: List<ProductModel>
    ) {
        prices.clear()

        if (product.isNotEmpty()) {


            prices.add(
                Prices(
                    magasin = Magazin.MONOPRIX.magazin,
                    priceBill = String.format(
                        "%.3f",
                        monoprixPrice(
                            context = context,
                            products = product,
                            calculateWithDiscount = calculateDiscount
                        ).toBigDecimal()
                    ),
                    nbrMissingPrice = 0,
                    bonusfid = 0.0
                )
            )

            prices.add(
                Prices(
                    magasin = Magazin.MG.magazin,
                    priceBill = String.format(
                        "%.3f",
                        mgPrice(
                            context = context,
                            products = product,
                            calculateWithDiscount = calculateDiscount
                        ).toBigDecimal()
                    ),
                    nbrMissingPrice = 0,
                    bonusfid = 0.0
                )
            )
            prices.add(
                Prices(
                    magasin = Magazin.AZIZA.magazin,
                    priceBill = String.format(
                        "%.3f",
                        azzizaPrice(
                            context = context,
                            products = product,
                            calculateWithDiscount = calculateDiscount
                        ).toBigDecimal()
                    ),
                    nbrMissingPrice = 0,
                    bonusfid = 0.0
                )
            )
            prices.add(
                Prices(
                    magasin = Magazin.CARREFOUR.magazin,
                    priceBill = String.format(
                        "%.3f",
                        carrefourPrice(
                            context = context,
                            products = product,
                            calculateWithDiscount = calculateDiscount
                        ).toBigDecimal()
                    ),
                    nbrMissingPrice = 0,
                    bonusfid = 0.0
                )
            )

            prices.add(
                Prices(
                    magasin = Magazin.GEANT.magazin,
                    priceBill = String.format(
                        "%.3f",
                        geantPrice(
                            context = context,
                            products = product,
                            calculateWithDiscount = calculateDiscount
                        ).toBigDecimal()
                    ),
                    nbrMissingPrice = 0,
                    bonusfid = 0.0
                )

            )


            nbrMissingPrices(product)
            bonusFid(product)
        }
    }

    private fun monoprixPrice(
        context: Context,
        products: List<ProductModel>,
        calculateWithDiscount: Boolean
    ): String {

        var finalPrice = 0.0
        products.indices.forEach { i ->
            val product = products[i]
            if (calculateWithDiscount) {
                when {
                    product.monoprixremarq == context.getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice += halfdiscountOnthesecond(
                            product.quantity.toString(),
                            product.monoprixprice,
                            product.typesub
                        )
                    }

                    product.monoprixremarq.contains(context.getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(
                            product.quantity.toString(), Functions.getNumberFromString(
                                product.monoprixremarq
                            )
                        )
                    }

                    product.monoprixremarq.contains(context.getString(R.string.emegratuit)) -> {

                        finalPrice += nemeGratuit(
                            Functions.getNumberFromString(product.monoprixremarq).toInt(),
                            product.quantity.toString(),
                            product.monoprixprice,
                            product.typesub
                        )
                    }

                    product.monoprixremarq.contains(context.getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(
                            Functions.getNumberFromString(product.monoprixremarq).toInt(),
                            product.quantity.toString(),
                            product.monoprixprice
                        )
                    }

                    product.monoprixremarq.contains(context.getString(R.string.aveccartefid)) -> {
                        finalPrice += priceWithFidCard(
                            Functions.getNumberFromString(product.monoprixremarq).toInt(),
                            product.quantity.toString()
                        )
                    }

                    product.monoprixremarq.contains(context.getString(R.string.Leprix)) -> {
                        finalPrice += packprice(
                            Functions.getNumbersFromString(
                                product.monoprixremarq,
                                context.getString(R.string.Leprix),
                                context.getString(R.string.est)
                            ),
                            Functions.getNumbersFromString(
                                product.monoprixremarq,
                                context.getString(R.string.est),
                                context.getString(R.string.TND)
                            ), product.quantity.toString(), product.monoprixprice, product.typesub
                        )

                    }

                    else -> {
                        finalPrice += pricenotdefinedBill(product.monoprixprice).toDouble() * product.quantity
                    }
                }
            } else {
                finalPrice += pricenotdefinedBill(product.monoprixprice).toDouble() * product.quantity
            }
        }

        return finalPrice.toString()
    }

    private fun mgPrice(
        context: Context,
        products: List<ProductModel>,
        calculateWithDiscount: Boolean
    ): String {
        var finalPrice = 0.0
        products.indices.forEach { i ->
            val product = products[i]
            if (calculateWithDiscount) {
                when {
                    product.mgremarq == context.getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice += halfdiscountOnthesecond(
                            product.quantity.toString(),
                            product.mgprice,
                            product.typesub
                        )
                    }

                    product.mgremarq.contains(context.getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(
                            product.quantity.toString(),
                            Functions.getNumberFromString(product.mgremarq)
                        )
                    }

                    product.mgremarq.contains(context.getString(R.string.emegratuit)) -> {
                        finalPrice += nemeGratuit(
                            Functions.getNumberFromString(product.mgremarq).toInt(),
                            product.quantity.toString(),
                            product.mgprice,
                            product.typesub
                        )
                    }

                    product.mgremarq.contains(context.getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(
                            Functions.getNumberFromString(product.mgremarq).toInt(),
                            product.quantity.toString(),
                            product.mgprice
                        )
                    }

                    product.mgremarq.contains(context.getString(R.string.aveccartefid)) -> {
                        finalPrice += priceWithFidCard(
                            Functions.getNumberFromString(product.mgremarq).toInt(),
                            product.quantity.toString()
                        )
                    }

                    product.mgremarq.contains(context.getString(R.string.Leprix)) -> {

                        finalPrice += packprice(
                            Functions.getNumbersFromString(
                                product.mgremarq,
                                context.getString(R.string.Leprix),
                                context.getString(R.string.est)
                            ),
                            Functions.getNumbersFromString(
                                product.mgremarq,
                                context.getString(R.string.est),
                                context.getString(R.string.TND)
                            ), product.quantity.toString(), product.mgprice, product.typesub
                        )

                    }

                    else /*product.ProductMGRemarqueDB==""*/ -> {
                        finalPrice += pricenotdefinedBill(product.mgprice).toDouble() * product.quantity
                    }
                }
            } else {
                finalPrice += pricenotdefinedBill(product.mgprice).toDouble() * product.quantity
            }
        }

        return finalPrice.toString()
    }

    private fun carrefourPrice(
        context: Context,
        products: List<ProductModel>,
        calculateWithDiscount: Boolean
    ): String {

        var finalPrice = 0.0

        products.indices.forEach { i ->

            val product = products[i]
            if (calculateWithDiscount) {
                when {
                    product.carrefourremarq == context.getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice += halfdiscountOnthesecond(
                            product.quantity.toString(),
                            product.carrefourprice,
                            product.typesub
                        )
                    }

                    product.carrefourremarq.contains(context.getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(
                            product.quantity.toString(),
                            Functions.getNumberFromString(product.carrefourremarq)
                        )
                    }

                    product.carrefourremarq.contains(context.getString(R.string.emegratuit)) -> {
                        finalPrice += nemeGratuit(
                            Functions.getNumberFromString(product.carrefourremarq).toInt(),
                            product.quantity.toString(),
                            product.carrefourprice,
                            product.typesub
                        )
                    }

                    product.carrefourremarq.contains(context.getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(
                            Functions.getNumberFromString(product.carrefourremarq).toInt(),
                            product.quantity.toString(),
                            product.carrefourprice
                        )
                    }

                    product.carrefourremarq.contains(context.getString(R.string.aveccartefid)) -> {
                        finalPrice += priceWithFidCard(
                            Functions.getNumberFromString(product.carrefourremarq).toInt(),
                            product.quantity.toString()
                        )
                    }

                    product.carrefourremarq.contains(context.getString(R.string.Leprix)) -> {
                        finalPrice += packprice(
                            Functions.getNumbersFromString(
                                product.carrefourremarq,
                                context.getString(R.string.Leprix),
                                context.getString(R.string.est)
                            ),
                            Functions.getNumbersFromString(
                                product.carrefourremarq,
                                context.getString(R.string.est),
                                context.getString(R.string.TND)
                            ), product.quantity.toString(), product.carrefourprice, product.typesub
                        )
                    }

                    else /* product.ProductCarrefourRemarqueDB=="" */ -> {
                        finalPrice += pricenotdefinedBill(product.carrefourprice).toDouble() * product.quantity
                    }
                }
            } else {
                finalPrice += pricenotdefinedBill(product.carrefourprice).toDouble() * product.quantity
            }
        }

        return finalPrice.toString()
    }

    private fun azzizaPrice(
        context: Context,
        products: List<ProductModel>,
        calculateWithDiscount: Boolean
    ): String {

        var finalPrice = 0.0

        products.indices.forEach { i ->
            val product = products[i]
            if (calculateWithDiscount) {
                when {
                    product.azzizaremarq == context.getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice += halfdiscountOnthesecond(
                            product.quantity.toString(),
                            product.azzizaprice,
                            product.typesub
                        )
                    }

                    product.azzizaremarq.contains(context.getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(
                            product.quantity.toString(),
                            Functions.getNumberFromString(product.azzizaremarq)
                        )
                    }

                    product.azzizaremarq.contains(context.getString(R.string.emegratuit)) -> {
                        finalPrice += nemeGratuit(
                            Functions.getNumberFromString(product.azzizaremarq).toInt(),
                            product.quantity.toString(),
                            product.azzizaprice,
                            product.typesub
                        )
                    }

                    product.azzizaremarq.contains(context.getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(
                            Functions.getNumberFromString(product.azzizaremarq).toInt(),
                            product.quantity.toString(),
                            product.azzizaprice
                        )
                    }

                    product.azzizaremarq.contains(context.getString(R.string.aveccartefid)) -> {
                        finalPrice += priceWithFidCard(
                            Functions.getNumberFromString(product.azzizaremarq).toInt(),
                            product.quantity.toString()
                        )
                    }

                    product.azzizaremarq.contains(context.getString(R.string.Leprix)) -> {
                        finalPrice += packprice(
                            Functions.getNumbersFromString(
                                product.azzizaremarq,
                                context.getString(R.string.Leprix),
                                context.getString(R.string.est)
                            ),
                            Functions.getNumbersFromString(
                                product.azzizaremarq,
                                context.getString(R.string.est),
                                context.getString(R.string.TND)
                            ),
                            product.quantity.toString(), product.azzizaprice, product.typesub
                        )
                    }

                    else /* product.ProductAzzizaRemarqueDB=="" */ -> {
                        finalPrice += pricenotdefinedBill(product.azzizaprice).toDouble() * product.quantity
                    }
                }
            } else {
                finalPrice += pricenotdefinedBill(product.azzizaprice).toDouble() * product.quantity
            }

        }



        return finalPrice.toString()
    }

    private fun geantPrice(
        context: Context,
        products: List<ProductModel>,
        calculateWithDiscount: Boolean
    ): String {

        var finalPrice = 0.0

        products.indices.forEach { i ->
            val product = products[i]

            if (calculateWithDiscount) {
                when {
                    product.geantremarq == context.getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice += halfdiscountOnthesecond(
                            product.quantity.toString(),
                            product.geantprice,
                            product.typesub
                        )
                    }

                    product.geantremarq.contains(context.getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(
                            product.quantity.toString(), Functions.getNumberFromString(
                                product.geantremarq
                            )
                        )
                    }

                    product.geantremarq.contains(context.getString(R.string.emegratuit)) -> {
                        finalPrice += nemeGratuit(
                            Functions.getNumberFromString(product.geantremarq).toInt(),
                            product.quantity.toString(),
                            product.geantprice,
                            product.typesub
                        )
                    }

                    product.geantremarq.contains(context.getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(
                            Functions.getNumberFromString(product.geantremarq).toInt(),
                            product.quantity.toString(),
                            product.geantprice
                        )
                    }

                    product.geantremarq.contains(context.getString(R.string.aveccartefid)) -> {
                        finalPrice += priceWithFidCard(
                            Functions.getNumberFromString(product.geantremarq).toInt(),
                            product.quantity.toString()
                        )
                    }

                    product.geantremarq.contains(context.getString(R.string.Leprix)) -> {
                        finalPrice += packprice(
                            Functions.getNumbersFromString(
                                product.geantremarq,
                                context.getString(R.string.Leprix),
                                context.getString(R.string.est)
                            ),
                            Functions.getNumbersFromString(
                                product.geantremarq,
                                context.getString(R.string.est),
                                context.getString(R.string.TND)
                            ), product.quantity.toString(), product.geantprice, product.typesub
                        )
                    }

                    else /*product.ProductGeantRemarqueDB==""*/ -> {
                        finalPrice += pricenotdefinedBill(product.geantprice).toDouble() * product.quantity
                    }
                }
            } else {
                finalPrice += pricenotdefinedBill(product.geantprice).toDouble() * product.quantity
            }
        }

        return finalPrice.toString()
    }

    private fun nemeGratuit(
        indiceGratuit: Int,
        quantity: String,
        price: String,
        subtype: String
    ): Double {
        return if (!Functions.customUnitmesure(subtype)) pricenotdefinedBill(price).toDouble() * quantity.toDouble()
        else quantity.toDouble() % indiceGratuit * pricenotdefinedBill(price).toDouble()

    }

    private fun packprice(
        nbrPack: Double,
        pricePack: Double,
        quantity: String,
        price: String,
        subtype: String
    ): Double {

        return if (!Functions.customUnitmesure(subtype)) pricenotdefinedBill(price).toDouble() * quantity.toDouble()
        else quantity.toDouble() / nbrPack * pricePack + quantity.toDouble() % nbrPack * pricenotdefinedBill(
            price
        ).toDouble()


    }

    private fun discountprice(quantity: String, price: String): Double {
        return quantity.toDouble() * pricenotdefinedBill(price).toDouble()
    }

    private fun halfdiscountOnthesecond(qantity: String, price: String, subtype: String): Double {
        return if (!Functions.customUnitmesure(subtype)) {
            pricenotdefinedBill(price).toDouble() * qantity.toDouble()
        } else {
            if (qantity.toDouble() % 2 == 0.0) {
                //get -50% off
                (qantity.toDouble() / 2) * pricenotdefinedBill(price).toDouble() + ((qantity.toDouble() / 2) * pricenotdefinedBill(
                    price
                ).toDouble()) / 2

            } else {
                ((qantity.toDouble() / 2) + 1) * pricenotdefinedBill(price).toDouble() + ((qantity.toDouble() / 2) * pricenotdefinedBill(
                    price
                ).toDouble()) / 2
            }
        }
    }

    private fun dicsountPourcentage(
        discountPourcentage: Int,
        quantity: String,
        price: String
    ): Double {
        return (pricenotdefinedBill(price).toDouble() - (pricenotdefinedBill(price).toDouble() * discountPourcentage / 100)) * quantity.toDouble()
    }

    private fun priceWithFidCard(priceWithFidCard: Int, quantity: String): Double {
        return pricenotdefinedBill(priceWithFidCard.toString()).toDouble() * quantity.toDouble()
    }

    private fun bonusFid(products: List<ProductModel>) {
        var monoprixBonusfid = 0.0
        var mgBonusfid = 0.0
        var carrefourBonusfid = 0.0
        var azzizaBonusfid = 0.0
        var geantBonusfid = 0.0

        products.indices.forEach { i ->
            val product = products[i]

            monoprixBonusfid += pricenotdefinedBill(product.monoprixbonusfid).toDouble() * product.quantity
            mgBonusfid += pricenotdefinedBill(product.mgbonusfid).toDouble() * product.quantity
            carrefourBonusfid += pricenotdefinedBill(product.carrefourbonusfid).toDouble() * product.quantity
            azzizaBonusfid += pricenotdefinedBill(product.azzizabonusfid).toDouble() * product.quantity
            geantBonusfid += pricenotdefinedBill(product.geantbonusfid).toDouble() * product.quantity

        }

        prices.first { it.magasin == Magazin.MG.magazin }.bonusfid = mgBonusfid
        prices.first { it.magasin == Magazin.CARREFOUR.magazin }.bonusfid = carrefourBonusfid
        prices.first { it.magasin == Magazin.AZIZA.magazin }.bonusfid = azzizaBonusfid
        prices.first { it.magasin == Magazin.MONOPRIX.magazin }.bonusfid = monoprixBonusfid
        prices.first { it.magasin == Magazin.GEANT.magazin }.bonusfid = geantBonusfid


    }

    private fun nbrMissingPrices(products: List<ProductModel>) {
        var nbrMissing_monoprixprice = 0
        var nbrMissing_mgprice = 0
        var nbrMissing_carrefourprice = 0
        var nbrMissing_azzizaprice = 0
        var nbrMissing_geantprice = 0

        products.indices.forEach { i ->
            val product = products[i]

            if (pricenotdefinedBill(product.monoprixprice) == "0") nbrMissing_monoprixprice += 1


            if (pricenotdefinedBill(product.mgprice) == "0") nbrMissing_mgprice += 1


            if (pricenotdefinedBill(product.carrefourprice) == "0") nbrMissing_carrefourprice += 1


            if (pricenotdefinedBill(product.azzizaprice) == "0") nbrMissing_azzizaprice += 1


            if (pricenotdefinedBill(product.geantprice) == "0") nbrMissing_geantprice += 1

        }


        prices.first { it.magasin == Magazin.MONOPRIX.magazin }.nbrMissingPrice =
            nbrMissing_monoprixprice
        prices.first { it.magasin == Magazin.CARREFOUR.magazin }.nbrMissingPrice =
            nbrMissing_carrefourprice
        prices.first { it.magasin == Magazin.GEANT.magazin }.nbrMissingPrice = nbrMissing_geantprice
        prices.first { it.magasin == Magazin.AZIZA.magazin }.nbrMissingPrice =
            nbrMissing_azzizaprice
        prices.first { it.magasin == Magazin.MG.magazin }.nbrMissingPrice = nbrMissing_mgprice


    }

    private fun pricenotdefinedBill(price: String): String {
        return if (price != "99999.0" && price != "" && price.isNotEmpty()) {
            val firstNummonoprixprice: BigDecimal? = try {
                BigDecimal(price)
            } catch (e: NumberFormatException) {
                null
            }

            if (!Functions.factor100(firstNummonoprixprice))
                firstNummonoprixprice.toString()
            else
                String.format("%.3f", firstNummonoprixprice)
        } else {
            "0"
        }
    }


    private fun showNbrMissingPrice(context: Context, nbr: Int): String {
        return if (nbr == 0) {
            ""
        } else {
            " (- $nbr ${context.getString(R.string.Prix)})"
        }
    }
//calculate prices end


}

