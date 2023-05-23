package com.amirami.simapp.priceindicatortunisia.ui.componenet.getbyproducttype

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
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
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.screens.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ACTION_GET_PROD_BY_TYPES
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ACTION_SHOW_TYPES

@Composable
fun GetProductByTypesListView(
    productsViewModel: ProductsViewModel,
    addModifyViewModel: AddModifyViewModel,
    from: String
) {
    val context = LocalContext.current
    val action = productsViewModel.actionTypesListView
    //  val item: Array<String> = context.resources.getStringArray(R.array.productFeauteredTypeArray)
    var item: Array<String> = productsViewModel.typesArrays
    // if(from == "")
    if (action == ACTION_SHOW_TYPES) item = remove(item, 0)

    //   productsViewModel.onprodDetailDialogVisibilityStatesChanged(context.resources.getStringArray(R.array.productFeauteredTypeArray))
    LazyRow() {
        items(item.size) { position ->
            ProductTypeSingleListItem(
                action = productsViewModel.actionTypesListView,
                item = item[position],
                currentProduct = productsViewModel.selectedProductStates,
                onSelectedProductChanged = {
                    productsViewModel.onSelectedProductChanged(it)
                },
                productsViewModel = productsViewModel,
                from = from,
                getProds = {
                    productsViewModel.getProds("typesubsub", item[position])
                }
            )
        }
    }
}

fun remove(arr: Array<String>, index: Int): Array<String> {
    if (index < 0 || index >= arr.size) {
        return arr
    }

    val result = arr.toMutableList()
    result.removeAt(index)
    return result.toTypedArray()
}

@Composable
fun ProductTypeSingleListItem(
    item: String,
    action: String,
    currentProduct: ProductModel,
    productsViewModel: ProductsViewModel,
    onSelectedProductChanged: (ProductModel) -> Unit,
    getProds: () -> Unit,
    from: String
) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            imageTypeLoader(
                item,
                context
            )
        )
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = true,
        isPlaying = true,
        iterations = 1,
        ignoreSystemAnimatorScale = true,
        speed = 3f
    )
    
    Column(
        modifier = Modifier
            .clickable {
                if (action == ACTION_GET_PROD_BY_TYPES) {
                    //  if(item != "Tous les Catégories") productsViewModel.getProds("typesubsub", item)
                    if (isInSubsubTypeArrays(context, item) && !isNotBackInSubsubTypeArrays(
                            context,
                            item
                        )
                    ) {
                        //   productsViewModel.onprodDetailDialogVisibilityStatesChanged(context.resources.getStringArray(arrayTypeLoader(item)))
                        //   Toast.makeText(context,"typesubsub"+ item+"c", Toast.LENGTH_LONG).show()
                        getProds()
                        
                    } else {
                        /* if (isInSubTypeArrays(context, item)){
                             productsViewModel.onprodDetailDialogVisibilityStatesChanged(context.resources.getStringArray(R.array.productTypeArray))
 
                         }*/

                        productsViewModel.onTypesArraysChange(
                            context.resources.getStringArray(
                                arrayTypeLoader(item)
                            )
                        )

                        //    Toast.makeText(context, item+"c", Toast.LENGTH_LONG).show()
                    }
                } else if (action == ACTION_SHOW_TYPES) {
                    when (from) {
                        context.resources.getString(R.string.Catégories) -> {
                            onSelectedProductChanged(currentProduct.copy(type = item))
                            onSelectedProductChanged(currentProduct.copy(typesub = " "))
                            onSelectedProductChanged(currentProduct.copy(typesubsub = " "))
                        }

                        context.resources.getString(R.string.SousCatégories) -> {
                            onSelectedProductChanged(currentProduct.copy(typesub = item))
                            onSelectedProductChanged(currentProduct.copy(typesubsub = " "))

                        }

                        context.resources.getString(R.string.sousousCatégories) -> {
                            onSelectedProductChanged(currentProduct.copy(typesubsub = item))
                        }
                    }


                    // Toast.makeText(context, item + "d", Toast.LENGTH_LONG).show()
                }

                /*if (argsFrom.topCategorie == "" && argsFrom.fromType == "typeFromMain") {
                    if (isInSubsubTypeArrays(context = context,item)) {
                        // DynamicToast.makeWarning(requireContext(),isInSubsubTypeArrays(item).toString() , 9).show()

                        productInfoViewModel.putproductSubSubTypesInfo(item)
                        dismiss()
                    } else if (!isInSubTypeArrays(context = context,item)) {
                        _binding!!.subCategoriesTxVw.text = item
                        _binding!!.subCategoriesTxVw.visibility = View.VISIBLE
                        _binding!!.subsubCategoriesTxVw.visibility = View.GONE
                        _binding!!.productSubsubTypeRv.visibility = View.GONE
                        setUpProductSubTypesRv(item)
                    } else {
                        _binding!!.subsubCategoriesTxVw.text = item
                        _binding!!.subsubCategoriesTxVw.visibility = View.VISIBLE
                        _binding!!.productSubsubTypeRv.visibility = View.VISIBLE
                        setUpProductSubsubTypesRv(item)
                        // _binding!!.nested.smoothScrollBy(0, 100);
                        //  _binding!!.nested.fullScroll(ScrollView.FOCUS_DOWN)
                        //   _binding!!.nested.smoothScrollTo(0, _binding!!.nested.getChildAt(0).height)
                        // _binding!!.nested.smoothScrollTo(0, _binding!!.nested.bottom);
                    }
                }
                else if (argsFrom.topCategorie == "" && argsFrom.fromType == "type") {
                    productInfoViewModel.putproductTypesInfo(item)
                    dismiss()
                }
                else if (argsFrom.topCategorie != "" && argsFrom.fromType == "subtype") {
                    productInfoViewModel.putproductSubTypesInfo(item)
                    dismiss()
                }
                else if (argsFrom.topCategorie != "" && argsFrom.fromType == "subsubtype") {
                    productInfoViewModel.putproductSubSubTypesInfo(item)
                    dismiss()
                }
                else if (argsFrom.fromType == "size") {
                    productInfoViewModel.putproductSizeInfo(item)
                    dismiss()
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
            .height(120.dp)
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(75.dp),
            composition = composition,
            progress = { progress }
        )

        Text(
            //  text = if(itemm =="")item else itemm,
            text = item,
            modifier = Modifier.width(75.dp),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun imageTypeLoader(type: String, ctx: Context): Int {
    return if (type in ctx.resources.getStringArray(R.array.unit_of_mesur) && type != "Oeufs") R.raw.unitmeasure
    else when (type) {
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
        "Oeufs" -> R.raw.subtype_oeufs
        //  in ctx.resources.getStringArray(R.array.unit_of_mesur)->R.raw.unitmeasure

        "Tous les Catégories" -> R.raw.allcategories
        "Catégories Par Défaut" -> R.raw.allcategories
        "Alimentation " -> R.raw.allcategories
        "Entretient de la Maison " -> R.raw.allcategories
        "Hygiène et Beauté " -> R.raw.allcategories
        "Bébé " -> R.raw.allcategories
        "Cuisine " -> R.raw.allcategories
        "Gros électroménager " -> R.raw.allcategories
        "Petit électroménager " -> R.raw.allcategories
        "Animalerie " -> R.raw.allcategories
        "High-Tech " -> R.raw.allcategories
        "Jardin  " -> R.raw.allcategories
        "Brico et Accessoires Auto " -> R.raw.allcategories
        "Fournitures Scolaires  " -> R.raw.allcategories
        "Linge de Maison  " -> R.raw.allcategories
        "Jeux et Jouets  " -> R.raw.allcategories
        "Bagagerie  " -> R.raw.allcategories
        "Sport et Loisir " -> R.raw.allcategories

        // ALIMENTATION
        "Boissons " -> R.raw.allcategories
        "Produits Laitiers " -> R.raw.allcategories
        "Conserves " -> R.raw.allcategories
        "Épicerie Salée " -> R.raw.allcategories
        "Épicerie Sucrée " -> R.raw.allcategories
        "Bio sans Gluten Diététique " -> R.raw.allcategories
        "Crèmerie et Fromages " -> R.raw.allcategories
        "Charcuterie " -> R.raw.allcategories
        "Boulangerie et Pâtisserie " -> R.raw.allcategories
        "Produits Frais " -> R.raw.allcategories
        "Surgelés " -> R.raw.allcategories
        "Oeufs " -> R.raw.allcategories
        // ENTRETIENT MAISON
        "Produits Ménagers " -> R.raw.allcategories
        "Lessives et Soins du Linge " -> R.raw.allcategories
        "Désodorisants " -> R.raw.allcategories
        "Insecticides " -> R.raw.allcategories
        "Papier et Lingettes Nettoyantes " -> R.raw.allcategories
        "Accessoires Ménagers " -> R.raw.allcategories
        "Rangement du Linge " -> R.raw.allcategories
        "Entretien des Chaussures " -> R.raw.allcategories
        // HIGIENE ET BEAUTE
        "Cheveux " -> R.raw.allcategories
        "Corps et Visage " -> R.raw.allcategories
        "Papier et Coton " -> R.raw.allcategories
        // BEBE
        "Couches et Lingettes " -> R.raw.allcategories
        "Toilettes et Soins " -> R.raw.allcategories
        "Repas " -> R.raw.allcategories
        "Puériculture " -> R.raw.allcategories
        // CUISINE
        "Art de la Table " -> R.raw.allcategories
        "Cuisson " -> R.raw.allcategories
        "Boites de Conservation " -> R.raw.allcategories
        "Ustensiles de Cuisine " -> R.raw.allcategories
        "Papier et Emballages Alimentaires " -> R.raw.allcategories
        "Rangement de la Cuisine subTypeArrayBébé" -> R.raw.allcategories
        // GROS ELECTROMENAGER
        "Climatisation et Chauffage " -> R.raw.allcategories
        "Réfrigérateur et Mini-Bar " -> R.raw.allcategories
        "Congélateur " -> R.raw.allcategories
        "Lave Linge " -> R.raw.allcategories
        "Lave Vaisselle " -> R.raw.allcategories
        "Cuisinière et Encastrable " -> R.raw.allcategories
        "Micro-Onde et Mini-Four " -> R.raw.allcategories
        // PETI ELECTROMENAGER
        "Petit Déjeuner " -> R.raw.allcategories
        "Préparation Culinaire " -> R.raw.allcategories
        "Appareil de Cuisson " -> R.raw.allcategories
        "Beauté et Santé " -> R.raw.allcategories
        "Entretien Sols et Surfaces " -> R.raw.allcategories
        "Soin du Linge " -> R.raw.allcategories
        // ANIMALERIE
        "Alimentation et Récompenses " -> R.raw.allcategories
        "Hygiène et Accessoires " -> R.raw.allcategories
        // HIGH TECH
        "Image et Son " -> R.raw.allcategories
        "Informatique et Accessoires " -> R.raw.allcategories
        "Téléphonie et Accessoires " -> R.raw.allcategories
        // JARDIN
        // "Jardin  " -> R.array.subTypeJardin
        // BRICO ACCESSOIR AUTO
        "Automobile " -> R.raw.allcategories
        "Bricolage " -> R.raw.allcategories
        "Électricité " -> R.raw.allcategories
        // FOURNITURE SCOLAIRE
        // "Fournitures Scolaires   " -> R.array.subTypeArrayFournituresScolaires
        // LINGE DE MAISON
        //   "Linge de Maison  " -> R.array.subTypeArrayLingedeMaison
        // Jeux et Jouets
        "Jeux et Jouets   " -> R.raw.allcategories
        // Bagagerie
        //   "Bagagerie  " -> R.array.subTypeArrayBagagerie
        // Sport et loisir
        "Sport " -> R.raw.allcategories
        "Loisir " -> R.raw.allcategories
        else -> {
            R.raw.type_default
        }
    }
}

fun arrayTypeLoader(type: String): Int {
    return when (type) {
        "Alimentation" -> R.array.subTypeArrayAlimentation
        "Entretient de la Maison" -> R.array.subTypeArrayEntretientdelaMaison
        "Hygiène et Beauté" -> R.array.subTypeArrayHygièneetBeauté
        "Bébé" -> R.array.subTypeArrayBébé
        "Cuisine" -> R.array.subTypeArrayCuisine
        "Gros électroménager" -> R.array.subTypeArrayGrosElectroménager
        "Petit électroménager" -> R.array.subTypeArrayPetitElectroménager
        "Animalerie" -> R.array.subTypeArrayAnimalerie
        "High-Tech" -> R.array.subTypeArrayHighTech
        "Jardin" -> R.array.subTypeJardin
        "Brico et Accessoires Auto" -> R.array.subTypeArrayBricoetaccessoiresauto
        "Fournitures Scolaires" -> R.array.subTypeArrayFournituresScolaires
        "Linge de Maison" -> R.array.subTypeArrayLingedeMaison
        "Jeux et Jouets" -> R.array.subTypeArrayJeuxetJouets
        "Bagagerie" -> R.array.subTypeArrayBagagerie
        "Sport et Loisir" -> R.array.subTypeArraySportetLoisir

        "Boissons" -> R.array.subsubTypeArrayBoissons
        "Produits Laitiers" -> R.array.subsubTypeArrayProduitsLaitiers
        "Conserves" -> R.array.subsubTypeArrayConserves
        "Épicerie Salée" -> R.array.subsubTypeArrayÉpicerieSalée
        "Épicerie Sucrée" -> R.array.subsubTypeArrayÉpicerieSucrée
        "Bio sans Gluten Diététique" -> R.array.subsubTypeArrayBiosansGlutenDiététique
        "Crèmerie et Fromages" -> R.array.subsubTypeArrayCrèmerieetFromages
        "Charcuterie" -> R.array.subsubTypeArrayCharcuterie
        "Boulangerie et Pâtisserie" -> R.array.subsubTypeArrayBoulangerieetPâtisserie
        "Produits Frais" -> R.array.subsubTypeArrayProduitsFrais
        "Surgelés" -> R.array.subsubTypeArraySurgelés
        "Oeufs" -> R.array.subsubTypeArrayOeufs
        "Produits Ménagers" -> R.array.subsubTypeArrayProduitsMénagers
        "Lessives et Soins du Linge" -> R.array.subsubTypeArrayLessivesetSoinsduLinge
        "Désodorisants" -> R.array.subsubTypeArrayDésodorisants
        "Insecticides" -> R.array.subsubTypeArrayInsecticides
        "Papier et Lingettes Nettoyantes" -> R.array.subsubTypeArrayPapieretLingettesNettoyantes
        "Accessoires Ménagers" -> R.array.subsubTypeArrayAccessoiresMénagers
        "Rangement du Linge" -> R.array.subsubTypeArrayRangementduLinge
        "Entretien des Chaussures" -> R.array.subsubTypeArrayEntretiendesChaussures
        "Cheveux" -> R.array.subsubTypeArrayCheveux
        "Corps et Visage" -> R.array.subsubTypeArrayCorpsetvisage
        "Papier et Coton" -> R.array.subsubTypeArrayPapieretCoton
        "Couches et Lingettes" -> R.array.subsubTypeArrayCouchesetLingettes
        "Toilettes et Soins" -> R.array.subsubTypeArrayToilettesetSoins
        "Repas" -> R.array.subsubTypeArrayRepas
        "Puériculture" -> R.array.subsubTypeArrayPuériculture
        "Art de la Table" -> R.array.subsubTypeArrayArtdelaTable
        "Cuisson" -> R.array.subsubTypeArrayCuisson
        "Boites de Conservation" -> R.array.subsubTypeArrayBoitesdeConservation
        "Ustensiles de Cuisine" -> R.array.subsubTypeArrayUstensilesdeCuisine
        "Papier et Emballages Alimentaires" -> R.array.subsubTypeArrayPapieretEmballagesAlimentaires
        "Rangement de la Cuisine" -> R.array.subsubTypeArrayRangementdelaCuisine
        "Climatisation et Chauffage" -> R.array.subsubTypeArrayClimatisationetChauffage
        "Réfrigérateur et Mini-Bar" -> R.array.subsubTypeArrayRéfrigérateuretMiniBar
        "Congélateur" -> R.array.subsubTypeArrayCongélateur
        "Lave Linge" -> R.array.subsubTypeArrayLaveLinge
        "Lave Vaisselle" -> R.array.subsubTypeArrayLaveVaisselle
        "Cuisinière et Encastrable" -> R.array.subsubTypeArrayCuisinièreetEncastrable
        "Micro-Onde et Mini-Four" -> R.array.subsubTypeArrayMicroOndeetMiniFour
        "Petit Déjeuner" -> R.array.subsubTypeArrayPetitDéjeuner
        "Préparation Culinaire" -> R.array.subsubTypeArrayPréparationCulinaire
        "Appareil de Cuisson" -> R.array.subsubTypeArrayAppareildeCuisson
        "Beauté et Santé" -> R.array.subsubTypeArrayBeautéetSanté
        "Entretien Sols et Surfaces" -> R.array.subsubTypeArrayEntretienSolsetSurfaces
        "Soin du Linge" -> R.array.subsubTypeArraySoinduLinge
        "Alimentation et Récompenses" -> R.array.subsubTypeArrayAlimentationetRécompenses
        "Hygiène et Accessoires" -> R.array.subsubTypeArrayHygièneetaccessoires
        "Image et Son" -> R.array.subsubTypeArrayImageetSon
        "Informatique et Accessoires" -> R.array.subsubTypeArrayInformatiqueetAccessoires
        "Téléphonie et Accessoires" -> R.array.subsubTypeArrayTéléphonieetAccessoires
        "Jardin " -> R.array.subsubTypeArrayJardin // maybe add more subsubtype
        "Automobile" -> R.array.subsubTypeArrayAutomobile
        "Bricolage" -> R.array.subsubTypeArrayBricolage
        "Électricité" -> R.array.subsubTypeArrayÉlectricité
        "Fournitures Scolaires " -> R.array.subTypeArrayFournituresScolaires // maybe add more subsubtype
        "Linge de Maison " -> R.array.subsubTypeArrayLingedeMaison // maybe add more subsubtype
        "Jeux et Jouets " -> R.array.subsubTypeArrayJeuxetJouets // maybe add more subsubtype
        "Bagagerie " -> R.array.subsubTypeArrayBagagerie // maybe add more subsubtype
        "Sport" -> R.array.subsubTypeArraySport // maybe add more subsubtype
        "Loisir" -> R.array.subsubTypeArrayLoisir // maybe add more subsubtype

        "Tous les Catégories" -> R.array.productTypeArray

        "Alimentation " -> R.array.productTypeArray
        "Entretient de la Maison " -> R.array.productTypeArray
        "Hygiène et Beauté " -> R.array.productTypeArray
        "Bébé " -> R.array.productTypeArray
        "Cuisine " -> R.array.productTypeArray
        "Gros électroménager " -> R.array.productTypeArray
        "Petit électroménager " -> R.array.productTypeArray
        "Animalerie " -> R.array.productTypeArray
        "High-Tech " -> R.array.productTypeArray
        "Jardin " -> R.array.productTypeArray
        "Brico et Accessoires Auto " -> R.array.productTypeArray
        "Fournitures Scolaires  " -> R.array.productTypeArray
        "Linge de Maison  " -> R.array.productTypeArray
        "Jeux et Jouets  " -> R.array.productTypeArray
        "Bagagerie  " -> R.array.productTypeArray
        "Sport et Loisir " -> R.array.productTypeArray

// ALIMENTATION
        "Boissons " -> R.array.subTypeArrayAlimentation
        "Produits Laitiers " -> R.array.subTypeArrayAlimentation
        "Conserves " -> R.array.subTypeArrayAlimentation
        "Épicerie Salée " -> R.array.subTypeArrayAlimentation
        "Épicerie Sucrée " -> R.array.subTypeArrayAlimentation
        "Bio sans Gluten Diététique " -> R.array.subTypeArrayAlimentation
        "Crèmerie et Fromages " -> R.array.subTypeArrayAlimentation
        "Charcuterie " -> R.array.subTypeArrayAlimentation
        "Boulangerie et Pâtisserie " -> R.array.subTypeArrayAlimentation
        "Produits Frais " -> R.array.subTypeArrayAlimentation
        "Surgelés " -> R.array.subTypeArrayAlimentation
        "Oeufs " -> R.array.subTypeArrayAlimentation
        // ENTRETIENT MAISON
        "Produits Ménagers " -> R.array.subTypeArrayEntretientdelaMaison
        "Lessives et Soins du Linge " -> R.array.subTypeArrayEntretientdelaMaison
        "Désodorisants " -> R.array.subTypeArrayEntretientdelaMaison
        "Insecticides " -> R.array.subTypeArrayEntretientdelaMaison
        "Papier et Lingettes Nettoyantes " -> R.array.subTypeArrayEntretientdelaMaison
        "Accessoires Ménagers " -> R.array.subTypeArrayEntretientdelaMaison
        "Rangement du Linge " -> R.array.subTypeArrayEntretientdelaMaison
        "Entretien des Chaussures " -> R.array.subTypeArrayEntretientdelaMaison
        // HIGIENE ET BEAUTE
        "Cheveux " -> R.array.subTypeArrayHygièneetBeauté
        "Corps et Visage " -> R.array.subTypeArrayHygièneetBeauté
        "Papier et Coton " -> R.array.subTypeArrayHygièneetBeauté
        // BEBE
        "Couches et Lingettes " -> R.array.subTypeArrayBébé
        "Toilettes et Soins " -> R.array.subTypeArrayBébé
        "Repas " -> R.array.subTypeArrayBébé
        "Puériculture " -> R.array.subTypeArrayBébé
        // CUISINE
        "Art de la Table " -> R.array.subTypeArrayCuisine
        "Cuisson " -> R.array.subTypeArrayCuisine
        "Boites de Conservation " -> R.array.subTypeArrayCuisine
        "Ustensiles de Cuisine " -> R.array.subTypeArrayCuisine
        "Papier et Emballages Alimentaires " -> R.array.subTypeArrayCuisine
        "Rangement de la Cuisine subTypeArrayBébé" -> R.array.subTypeArrayCuisine
        // GROS ELECTROMENAGER
        "Climatisation et Chauffage " -> R.array.subTypeArrayGrosElectroménager
        "Réfrigérateur et Mini-Bar " -> R.array.subTypeArrayGrosElectroménager
        "Congélateur " -> R.array.subTypeArrayGrosElectroménager
        "Lave Linge " -> R.array.subTypeArrayGrosElectroménager
        "Lave Vaisselle " -> R.array.subTypeArrayGrosElectroménager
        "Cuisinière et Encastrable " -> R.array.subTypeArrayGrosElectroménager
        "Micro-Onde et Mini-Four " -> R.array.subTypeArrayGrosElectroménager
        // PETI ELECTROMENAGER
        "Petit Déjeuner " -> R.array.subTypeArrayPetitElectroménager
        "Préparation Culinaire " -> R.array.subTypeArrayPetitElectroménager
        "Appareil de Cuisson " -> R.array.subTypeArrayPetitElectroménager
        "Beauté et Santé " -> R.array.subTypeArrayPetitElectroménager
        "Entretien Sols et Surfaces " -> R.array.subTypeArrayPetitElectroménager
        "Soin du Linge " -> R.array.subTypeArrayPetitElectroménager
        // ANIMALERIE
        "Alimentation et Récompenses " -> R.array.subsubTypeArrayAlimentationetRécompenses
        "Hygiène et Accessoires " -> R.array.subsubTypeArrayHygièneetaccessoires
        // HIGH TECH
        "Image et Son " -> R.array.subTypeArrayHighTech
        "Informatique et Accessoires " -> R.array.subTypeArrayHighTech
        "Téléphonie et Accessoires " -> R.array.subTypeArrayHighTech
        // JARDIN
        // "Jardin  " -> R.array.subTypeJardin
        // BRICO ACCESSOIR AUTO
        "Automobile " -> R.array.subTypeArrayBricoetaccessoiresauto
        "Bricolage " -> R.array.subTypeArrayBricoetaccessoiresauto
        "Électricité " -> R.array.subTypeArrayBricoetaccessoiresauto
        // FOURNITURE SCOLAIRE
        // "Fournitures Scolaires   " -> R.array.subTypeArrayFournituresScolaires
        // LINGE DE MAISON
        //   "Linge de Maison  " -> R.array.subTypeArrayLingedeMaison
        // Jeux et Jouets
        "Jeux et Jouets   " -> R.array.subTypeArrayJeuxetJouets
        // Bagagerie
        //   "Bagagerie  " -> R.array.subTypeArrayBagagerie
        // Sport et loisir
        "Sport " -> R.array.subTypeArraySportetLoisir
        "Loisir " -> R.array.subTypeArraySportetLoisir

        "Catégories Par Défaut" -> R.array.productFeauteredTypeArray

        else -> {
            R.array.error
        }
    }
}

private fun isInSubTypeArrays(context: Context, type: String): Boolean {
    return type in context.resources.getStringArray(R.array.subTypeArrayAlimentation) ||
            type in context.resources.getStringArray(R.array.subTypeArrayEntretientdelaMaison) ||
            type in context.resources.getStringArray(R.array.subTypeArrayHygièneetBeauté) ||
            type in context.resources.getStringArray(R.array.subTypeArrayBébé) ||
            type in context.resources.getStringArray(R.array.subTypeArrayCuisine) ||
            type in context.resources.getStringArray(R.array.subTypeArrayGrosElectroménager) ||
            type in context.resources.getStringArray(R.array.subTypeArrayPetitElectroménager) ||
            type in context.resources.getStringArray(R.array.subTypeArrayAnimalerie) ||
            type in context.resources.getStringArray(R.array.subTypeArrayHighTech) ||
            type in context.resources.getStringArray(R.array.subTypeJardin) ||
            type in context.resources.getStringArray(R.array.subTypeArrayBricoetaccessoiresauto) ||
            type in context.resources.getStringArray(R.array.subTypeArrayFournituresScolaires) ||
            type in context.resources.getStringArray(R.array.subTypeArrayLingedeMaison) ||
            type in context.resources.getStringArray(R.array.subTypeArrayJeuxetJouets) ||
            type in context.resources.getStringArray(R.array.subTypeArrayBagagerie) ||
            type in context.resources.getStringArray(R.array.subTypeArraySportetLoisir)
}

private fun isInSubsubTypeArrays(context: Context, type: String): Boolean {
    return type in context.resources.getStringArray(R.array.subsubTypeArrayBoissons) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayProduitsLaitiers) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayConserves) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayÉpicerieSalée) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayÉpicerieSucrée) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayBiosansGlutenDiététique) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayCrèmerieetFromages) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayCharcuterie) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayBoulangerieetPâtisserie) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayProduitsFrais) ||
            type in context.resources.getStringArray(R.array.subsubTypeArraySurgelés) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayOeufs) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayProduitsMénagers) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayLessivesetSoinsduLinge) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayDésodorisants) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayInsecticides) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayPapieretLingettesNettoyantes) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayAccessoiresMénagers) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayRangementduLinge) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayEntretiendesChaussures) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayCheveux) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayCorpsetvisage) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayPapieretCoton) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayCouchesetLingettes) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayToilettesetSoins) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayRepas) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayPuériculture) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayArtdelaTable) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayCuisson) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayBoitesdeConservation) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayUstensilesdeCuisine) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayPapieretEmballagesAlimentaires) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayRangementdelaCuisine) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayClimatisationetChauffage) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayRéfrigérateuretMiniBar) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayCongélateur) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayLaveLinge) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayLaveVaisselle) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayCuisinièreetEncastrable) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayMicroOndeetMiniFour) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayPetitDéjeuner) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayPréparationCulinaire) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayAppareildeCuisson) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayBeautéetSanté) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayEntretienSolsetSurfaces) ||
            type in context.resources.getStringArray(R.array.subsubTypeArraySoinduLinge) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayAlimentationetRécompenses) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayHygièneetaccessoires) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayImageetSon) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayInformatiqueetAccessoires) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayTéléphonieetAccessoires) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayJardin) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayAutomobile) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayBricolage) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayÉlectricité) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayLingedeMaison) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayJeuxetJouets) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayBagagerie) ||
            type in context.resources.getStringArray(R.array.subsubTypeArrayLoisir) ||
            type in context.resources.getStringArray(R.array.subsubTypeArraySport)
}

private fun isNotBackInSubsubTypeArrays(context: Context, type: String): Boolean {
    return type == context.resources.getStringArray(R.array.subsubTypeArrayBoissons)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayProduitsLaitiers)[0] ||

            type == context.resources.getStringArray(R.array.subsubTypeArrayConserves)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayÉpicerieSalée)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayÉpicerieSucrée)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayBiosansGlutenDiététique)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayCrèmerieetFromages)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayCharcuterie)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayBoulangerieetPâtisserie)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayProduitsFrais)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArraySurgelés)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayOeufs)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayProduitsMénagers)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayLessivesetSoinsduLinge)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayDésodorisants)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayInsecticides)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayPapieretLingettesNettoyantes)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayAccessoiresMénagers)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayRangementduLinge)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayEntretiendesChaussures)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayCheveux)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayCorpsetvisage)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayPapieretCoton)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayCouchesetLingettes)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayToilettesetSoins)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayRepas)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayPuériculture)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayArtdelaTable)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayCuisson)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayBoitesdeConservation)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayUstensilesdeCuisine)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayClimatisationetChauffage)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayRéfrigérateuretMiniBar)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayCongélateur)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayLaveLinge)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayLaveVaisselle)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayCuisinièreetEncastrable)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayMicroOndeetMiniFour)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayPetitDéjeuner)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayPréparationCulinaire)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayAppareildeCuisson)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayBeautéetSanté)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayEntretienSolsetSurfaces)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArraySoinduLinge)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayAlimentationetRécompenses)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayHygièneetaccessoires)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayImageetSon)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayInformatiqueetAccessoires)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayTéléphonieetAccessoires)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayJardin)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayAutomobile)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayBricolage)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayÉlectricité)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayFournituresScolaires)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayLingedeMaison)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayJeuxetJouets)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayBagagerie)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArraySport)[0] ||
            type == context.resources.getStringArray(R.array.subsubTypeArrayLoisir)[0]
}
