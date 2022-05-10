package com.amirami.simapp.priceindicatortunisia.dialoguesfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.ProductTypesDialogBinding
import com.amirami.simapp.priceindicatortunisia.rvadapters.ProductTypesAdapter
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductInfoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductTypesDialog : BottomSheetDialogFragment(), ProductTypesAdapter.OnItemClickListener {
    private lateinit var productTypesAdapter: ProductTypesAdapter
    private lateinit var productTypesSubAdapter: ProductTypesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductTypesDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    private var _binding: ProductTypesDialogBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()
    val argsFrom: ProductTypesDialogArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        if (argsFrom.fromType == "size") {
            setUpProductSizeRv()
            _binding!!.CategoriesTxVw.text = getString(R.string.Unite)
            _binding!!.subCategoriesTxVw.visibility = View.GONE
            _binding!!.subsubCategoriesTxVw.visibility = View.GONE
        } else if (argsFrom.topCategorie == "" && (argsFrom.fromType == "type" || argsFrom.fromType == "typeFromMain")) {
            setUpProductTypesRv()
            _binding!!.subCategoriesTxVw.visibility = View.GONE
            _binding!!.subsubCategoriesTxVw.visibility = View.GONE
        } else if (argsFrom.topCategorie != "" && (argsFrom.fromType == "subtype" || argsFrom.fromType == "subsubtype")) {
            _binding!!.CategoriesTxVw.visibility = View.GONE
            _binding!!.subsubCategoriesTxVw.visibility = View.GONE
            _binding!!.subCategoriesTxVw.text = argsFrom.topCategorie

            setUpProductSubTypesRv(argsFrom.topCategorie/*.replace(" ".toRegex(), "")*/)
            // setUpProductTypesRv()
        } else {
            _binding!!.CategoriesTxVw.text = getString(R.string.définirSousCatégorie)
            _binding!!.subCategoriesTxVw.visibility = View.GONE
            _binding!!.subsubCategoriesTxVw.visibility = View.GONE
        }


    }

    private fun setUpProductSizeRv() {

        productTypesAdapter = ProductTypesAdapter(
            resources.getStringArray(R.array.unit_of_mesur)/*, popularImagetagList*/,
            this
        )

        _binding!!.productTypeRv.apply {
            adapter = productTypesAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)

            setHasFixedSize(true)
        }
    }

    private fun setUpProductTypesRv() {

        productTypesAdapter = ProductTypesAdapter(
            resources.getStringArray(R.array.productTypeArray)/*, popularImagetagList*/,
            this
        )

        _binding!!.productTypeRv.apply {
            adapter = productTypesAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)

            setHasFixedSize(true)
        }
    }

    private fun setUpProductSubTypesRv(topcategorie: String) {
        /* val uri = "@array/" + "subTypeArray"+ topcategorie.replace("-".toRegex(), "")
         val arrayResource = resources.getIdentifier(uri, null, requireContext().packageName)*/
        productTypesSubAdapter =
            ProductTypesAdapter(resources.getStringArray(arrayTypeLoader(topcategorie)), this)
        // DynamicToast.makeWarning(requireContext(),topcategorie+ isSubsubType(topcategorie).toString() , 9).show()
        _binding!!.productSubTypeRv.apply {
            adapter = productTypesSubAdapter
            layoutManager = if (resources.getStringArray(arrayTypeLoader(topcategorie)).size > 9)
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            else GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)

            setHasFixedSize(true)
        }


    }


    private fun setUpProductSubsubTypesRv(topcategorie: String) {
        /* val uri = "@array/" + "subTypeArray"+ topcategorie.replace("-".toRegex(), "")
         val arrayResource = resources.getIdentifier(uri, null, requireContext().packageName)*/
        productTypesSubAdapter =
            ProductTypesAdapter(resources.getStringArray(arrayTypeLoader(topcategorie)), this)
        // DynamicToast.makeWarning(requireContext(),topcategorie+ isSubsubType(topcategorie).toString() , 9).show()
        _binding!!.productSubsubTypeRv.apply {
            adapter = productTypesSubAdapter
            layoutManager = if (resources.getStringArray(arrayTypeLoader(topcategorie)).size > 8)
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            else GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)

            setHasFixedSize(true)

            requestFocus()
        }


        //_binding!!.nested.parent.requestChildFocus(_binding!!.nested, _binding!!.nested)
    }

    override fun onItemTagsClick(item: String) {
        if (argsFrom.topCategorie == "" && argsFrom.fromType == "typeFromMain") {

            if (isInSubsubTypeArrays(item)) {
                //DynamicToast.makeWarning(requireContext(),isInSubsubTypeArrays(item).toString() , 9).show()

                productInfoViewModel.putproductSubSubTypesInfo(item)
                dismiss()
            }
            else if (!isInSubTypeArrays(item)) {

                _binding!!.subCategoriesTxVw.text = item
                _binding!!.subCategoriesTxVw.visibility = View.VISIBLE
                _binding!!.subsubCategoriesTxVw.visibility = View.GONE
                _binding!!.productSubsubTypeRv.visibility = View.GONE
                setUpProductSubTypesRv(item)
            }
            else {

                _binding!!.subsubCategoriesTxVw.text = item
                _binding!!.subsubCategoriesTxVw.visibility = View.VISIBLE
                _binding!!.productSubsubTypeRv.visibility = View.VISIBLE
                setUpProductSubsubTypesRv(item)
                // _binding!!.nested.smoothScrollBy(0, 100);
                //  _binding!!.nested.fullScroll(ScrollView.FOCUS_DOWN)
                //   _binding!!.nested.smoothScrollTo(0, _binding!!.nested.getChildAt(0).height)
                //_binding!!.nested.smoothScrollTo(0, _binding!!.nested.bottom);

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

    }


    private fun arrayTypeLoader(type: String): Int {
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
            "Jardin " -> R.array.subsubTypeArrayJardin  //maybe add more subsubtype
            "Automobile" -> R.array.subsubTypeArrayAutomobile
            "Bricolage" -> R.array.subsubTypeArrayBricolage
            "Électricité" -> R.array.subsubTypeArrayÉlectricité
            "Fournitures Scolaires " -> R.array.subTypeArrayFournituresScolaires //maybe add more subsubtype
            "Linge de Maison " -> R.array.subsubTypeArrayLingedeMaison //maybe add more subsubtype
            "Jeux et Jouets " -> R.array.subsubTypeArrayJeuxetJouets //maybe add more subsubtype
            "Bagagerie " -> R.array.subsubTypeArrayBagagerie //maybe add more subsubtype
             "Sport" -> R.array.subsubTypeArraySport //maybe add more subsubtype
            "Loisir" -> R.array.subsubTypeArrayLoisir //maybe add more subsubtype


            else -> {
                R.array.error
            }
        }
    }

    private fun isInSubTypeArrays(type: String): Boolean {


        return type in resources.getStringArray(R.array.subTypeArrayAlimentation) ||
                type in resources.getStringArray(R.array.subTypeArrayEntretientdelaMaison) ||
                type in resources.getStringArray(R.array.subTypeArrayHygièneetBeauté) ||
                type in resources.getStringArray(R.array.subTypeArrayBébé) ||
                type in resources.getStringArray(R.array.subTypeArrayCuisine) ||
                type in resources.getStringArray(R.array.subTypeArrayGrosElectroménager) ||
                type in resources.getStringArray(R.array.subTypeArrayPetitElectroménager) ||
                type in resources.getStringArray(R.array.subTypeArrayAnimalerie) ||
                type in resources.getStringArray(R.array.subTypeArrayHighTech) ||
                type in resources.getStringArray(R.array.subTypeJardin) ||
                type in resources.getStringArray(R.array.subTypeArrayBricoetaccessoiresauto) ||
                type in resources.getStringArray(R.array.subTypeArrayFournituresScolaires) ||
                type in resources.getStringArray(R.array.subTypeArrayLingedeMaison) ||
                type in resources.getStringArray(R.array.subTypeArrayJeuxetJouets) ||
                type in resources.getStringArray(R.array.subTypeArrayBagagerie) ||
                type in resources.getStringArray(R.array.subTypeArraySportetLoisir)


    }

    private fun isInSubsubTypeArrays(type: String): Boolean {


        return type in resources.getStringArray(R.array.subsubTypeArrayBoissons) ||
                type in resources.getStringArray(R.array.subsubTypeArrayProduitsLaitiers) ||
                type in resources.getStringArray(R.array.subsubTypeArrayÉpicerieSalée) ||
                type in resources.getStringArray(R.array.subsubTypeArrayÉpicerieSucrée) ||
                type in resources.getStringArray(R.array.subsubTypeArrayBiosansGlutenDiététique) ||
                type in resources.getStringArray(R.array.subsubTypeArrayCrèmerieetFromages) ||
                type in resources.getStringArray(R.array.subsubTypeArrayCharcuterie) ||
                type in resources.getStringArray(R.array.subsubTypeArrayBoulangerieetPâtisserie) ||
                type in resources.getStringArray(R.array.subsubTypeArrayProduitsFrais) ||
                type in resources.getStringArray(R.array.subsubTypeArraySurgelés) ||
                type in resources.getStringArray(R.array.subsubTypeArrayOeufs) ||
                type in resources.getStringArray(R.array.subsubTypeArrayProduitsMénagers) ||
                type in resources.getStringArray(R.array.subsubTypeArrayLessivesetSoinsduLinge) ||
                type in resources.getStringArray(R.array.subsubTypeArrayDésodorisants) ||
                type in resources.getStringArray(R.array.subsubTypeArrayInsecticides) ||
                type in resources.getStringArray(R.array.subsubTypeArrayPapieretLingettesNettoyantes) ||
                type in resources.getStringArray(R.array.subsubTypeArrayAccessoiresMénagers) ||
                type in resources.getStringArray(R.array.subsubTypeArrayRangementduLinge) ||
                type in resources.getStringArray(R.array.subsubTypeArrayEntretiendesChaussures) ||
                type in resources.getStringArray(R.array.subsubTypeArrayCheveux) ||
                type in resources.getStringArray(R.array.subsubTypeArrayCorpsetvisage) ||
                type in resources.getStringArray(R.array.subsubTypeArrayPapieretCoton) ||
                type in resources.getStringArray(R.array.subsubTypeArrayCouchesetLingettes) ||
                type in resources.getStringArray(R.array.subsubTypeArrayToilettesetSoins) ||
                type in resources.getStringArray(R.array.subsubTypeArrayRepas) ||
                type in resources.getStringArray(R.array.subsubTypeArrayPuériculture) ||
                type in resources.getStringArray(R.array.subsubTypeArrayArtdelaTable) ||
                type in resources.getStringArray(R.array.subsubTypeArrayCuisson) ||
                type in resources.getStringArray(R.array.subsubTypeArrayBoitesdeConservation) ||
                type in resources.getStringArray(R.array.subsubTypeArrayUstensilesdeCuisine) ||
                type in resources.getStringArray(R.array.subsubTypeArrayPapieretEmballagesAlimentaires) ||
                type in resources.getStringArray(R.array.subsubTypeArrayRangementdelaCuisine) ||
                type in resources.getStringArray(R.array.subsubTypeArrayClimatisationetChauffage) ||
                type in resources.getStringArray(R.array.subsubTypeArrayRéfrigérateuretMiniBar) ||
                type in resources.getStringArray(R.array.subsubTypeArrayCongélateur) ||
                type in resources.getStringArray(R.array.subsubTypeArrayLaveLinge) ||
                type in resources.getStringArray(R.array.subsubTypeArrayLaveVaisselle) ||
                type in resources.getStringArray(R.array.subsubTypeArrayCuisinièreetEncastrable) ||
                type in resources.getStringArray(R.array.subsubTypeArrayMicroOndeetMiniFour) ||
                type in resources.getStringArray(R.array.subsubTypeArrayPetitDéjeuner) ||
                type in resources.getStringArray(R.array.subsubTypeArrayPréparationCulinaire) ||
                type in resources.getStringArray(R.array.subsubTypeArrayAppareildeCuisson) ||
                type in resources.getStringArray(R.array.subsubTypeArrayBeautéetSanté) ||
                type in resources.getStringArray(R.array.subsubTypeArrayEntretienSolsetSurfaces) ||
                type in resources.getStringArray(R.array.subsubTypeArraySoinduLinge) ||
                type in resources.getStringArray(R.array.subsubTypeArrayAlimentationetRécompenses) ||
                type in resources.getStringArray(R.array.subsubTypeArrayHygièneetaccessoires) ||
                type in resources.getStringArray(R.array.subsubTypeArrayImageetSon) ||
                type in resources.getStringArray(R.array.subsubTypeArrayInformatiqueetAccessoires) ||
                type in resources.getStringArray(R.array.subsubTypeArrayTéléphonieetAccessoires) ||
                type in resources.getStringArray(R.array.subsubTypeArrayJardin) ||
                type in resources.getStringArray(R.array.subsubTypeArrayAutomobile) ||
                type in resources.getStringArray(R.array.subsubTypeArrayBricolage) ||
                type in resources.getStringArray(R.array.subsubTypeArrayÉlectricité) ||
                type in resources.getStringArray(R.array.subsubTypeArrayLingedeMaison) ||
                type in resources.getStringArray(R.array.subsubTypeArrayJeuxetJouets) ||
                type in resources.getStringArray(R.array.subsubTypeArrayBagagerie) ||
                type in resources.getStringArray(R.array.subsubTypeArrayLoisir)||
                type in resources.getStringArray(R.array.subsubTypeArraySport)

    }
}