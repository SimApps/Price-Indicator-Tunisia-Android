
package com.amirami.simapp.priceindicatortunisia.dialoguesfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getNumberFromString
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getNumbersFromString
import com.amirami.simapp.priceindicatortunisia.utils.Functions.isDouble
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductInfoViewModel
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentPriceRemarqueBinding
import com.amirami.simapp.priceindicatortunisia.utils.Functions.dynamicToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PriceRemarqueDialogue : BottomSheetDialogFragment() {
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()

    lateinit var _binding: FragmentPriceRemarqueBinding

    val argsFrom: PriceRemarqueDialogueArgs by navArgs()

    var magasin = ""
    var discount = ""
    var bonusfid = ""

    var radioId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriceRemarqueBinding.inflate(inflater, container, false)
        return _binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        magasin= argsFrom.magasin
        discount= argsFrom.discount
        bonusfid= argsFrom.bonusfid

        setInputs()

        var result = ""


        _binding.apply {
            radiogroupePriceRemarque.setOnCheckedChangeListener { group, checkedId ->
                result = ""
                when (checkedId) {

                    R.id.radioBtn_null -> {
                        radioId = R.id.radioBtn_null
                        result = ""  //getString(R.string.pasderemarque)
                    }

                    R.id.radioBtn_promo_price -> {
                        radioId = R.id.radioBtn_promo_price
                        result = promoPrice.text.toString() + " " + getString(R.string.TND) + " " + getString(R.string.prixEnPromotion)
                    }
                    R.id.radioBtn_remise50pour2eme -> {
                        radioId = R.id.radioBtn_remise50pour2eme
                        result = getString(R.string.remisesurlaseuxiemme)
                    }
                    R.id.radioBtn_NemeGratuite -> {
                        if (numberWishIsfreeTxvw.text.toString() != "0" && numberWishIsfreeTxvw.text.toString() != "1") {
                            radioId = R.id.radioBtn_NemeGratuite
                            result = numberWishIsfreeTxvw.text.toString() + " " + getString(R.string.emegratuit)
                        }
                        else DynamicToast.makeError(requireContext(), "0 et 1 ne sont pas accepté", 9).show()

                    }
                    R.id.radioBtn_discount -> {
                        radioId = R.id.radioBtn_discount
                        result = getString(R.string.tiree) + discountPoucentageTxVw.text.toString() + getString(R.string.pourcentage)
                    }
                    R.id.radioBtn_pricewith_cartFidelite -> {
                        radioId = R.id.radioBtn_pricewith_cartFidelite
                        result = priceWithFidCardTxVw.text.toString() + " " + getString(R.string.aveccartefid)
                    }
                    R.id.radioBtn_prix_pack -> {
                        radioId = R.id.radioBtn_prix_pack
                        result = getString(R.string.Leprix) + " " + nbrDanslepackEdittex.text.toString() + " " + getString(R.string.est) + " " + prixPourPackTxVw.text.toString() + " " + getString(R.string.TND)
                    }
                    else -> {
                        radioId = -1
                        result = ""

                        DynamicToast.makeError(requireContext(), getString(R.string.Sélectionner_une_option), 9).show()
                    }
                }
            }

            validerBtn.setSafeOnClickListener {

                when (radioId) {
                    R.id.radioBtn_null -> {
                        result = "NO_PROMO"

                        productInfoViewModel.putproductRemarqueInfo(magasin, result,bonusSurCartFidEditText.text.toString())
                        goToAddProductFragmenet()
                    }
                    R.id.radioBtn_promo_price -> {
                        if(promoPrice.text.toString()!=""){
                            if(isDouble(promoPrice.text.toString())){
                                if(promoPrice.text.toString().toDouble()>0.0){
                                    result = promoPrice.text.toString() + " " + getString(R.string.TND) + " " + getString(R.string.prixEnPromotion)

                                    productInfoViewModel.putproductRemarqueInfo(magasin, result, bonusSurCartFidEditText.text.toString())
                                    goToAddProductFragmenet()
                                }

                            }
                            else errorToast(requireContext(), getString(R.string.prix_invalid))
                        }
                        else  errorToast(requireContext(), getString(R.string.entrer_une_valeur))
                    }
                    R.id.radioBtn_remise50pour2eme -> {
                        result = getString(R.string.remisesurlaseuxiemme)

                        productInfoViewModel.putproductRemarqueInfo(magasin, result, bonusSurCartFidEditText.text.toString())
                        goToAddProductFragmenet()
                    }
                    R.id.radioBtn_NemeGratuite -> {
                        if (numberWishIsfreeTxvw.text.toString() != "") {
                            if(isDouble(numberWishIsfreeTxvw.text.toString())){
                                if (numberWishIsfreeTxvw.text.toString().toDouble()>0.0) {
                                    result = numberWishIsfreeTxvw.text.toString() + " " + getString(R.string.emegratuit)

                                    productInfoViewModel.putproductRemarqueInfo(magasin, result, bonusSurCartFidEditText.text.toString())
                                    goToAddProductFragmenet()
                                }
                            }
                            else errorToast(requireContext(), getString(R.string.ne_sont_pas_accepté))
                        }
                        else errorToast(requireContext(), getString(R.string.entrer_une_valeur))
                    }
                    R.id.radioBtn_discount -> {
                        if (discountPoucentageTxVw.text.toString() != "") {
                            if(isDouble(discountPoucentageTxVw.text.toString())){
                                if(discountPoucentageTxVw.text.toString().toDouble()>0.0){
                                    result = getString(R.string.tiree) + discountPoucentageTxVw.text.toString() + getString(R.string.pourcentage)

                                    productInfoViewModel.putproductRemarqueInfo(magasin, result, bonusSurCartFidEditText.text.toString())
                                    goToAddProductFragmenet()
                                }
                            }
                            else errorToast(requireContext(), getString(R.string.valeur_invalid))
                        }
                        else errorToast(requireContext(), getString(R.string.entrer_une_valeur))
                    }
                    R.id.radioBtn_pricewith_cartFidelite -> {
                        if (priceWithFidCardTxVw.text.toString() != "") {
                            if(isDouble(priceWithFidCardTxVw.text.toString())){
                                if(priceWithFidCardTxVw.text.toString().toDouble()>0.0){
                                    result = priceWithFidCardTxVw.text.toString() + " " + getString(R.string.aveccartefid)

                                    productInfoViewModel.putproductRemarqueInfo(magasin, result, bonusSurCartFidEditText.text.toString())
                                    goToAddProductFragmenet()
                                }
                            }
                            else errorToast(requireContext(), getString(R.string.prix_invalid))
                        }
                        else errorToast(requireContext(), getString(R.string.entrer_une_valeur))
                    }
                    R.id.radioBtn_prix_pack -> {
                        if (nbrDanslepackEdittex.text.toString() != "" && prixPourPackTxVw.text.toString() != "") {

                            if(isDouble(prixPourPackTxVw.text.toString()) && isDouble(nbrDanslepackEdittex.text.toString())){
                                if(nbrDanslepackEdittex.text.toString().toDouble()>0.0 && prixPourPackTxVw.text.toString().toDouble()>0.0){
                                    result = getString(R.string.Leprix) + " " + nbrDanslepackEdittex.text.toString() + " " + getString(R.string.est) + " " + prixPourPackTxVw.text.toString() + " " + getString(R.string.TND)

                                    productInfoViewModel.putproductRemarqueInfo(magasin, result, bonusSurCartFidEditText.text.toString())
                                    goToAddProductFragmenet()
                                }

                            }
                            else errorToast(requireContext(), getString(R.string.valeur_invalid))
                        }
                        else errorToast(requireContext(), getString(R.string.entrer_une_valeur))
                    }
                    else -> {
                        result = ""
                        dynamicToast(requireContext(), getString(R.string.aucune_radio_sélectionnée))
                    }
                }
            }


        }

    }

    fun setInputs()  {
        _binding.bonusSurCartFidEditText.setText(bonusfid)
        radioBtnSteUp()
    }
    fun radioBtnSteUp() {
        _binding.apply {
            when {
                discount== getString(R.string.remisesurlaseuxiemme) -> {
                    radioBtnRemise50pour2eme.isChecked = true
                    radioId = R.id.radioBtn_remise50pour2eme
                }

                discount.contains(getString(R.string.prixEnPromotion)) -> {
                    radioBtnPromoPrice.isChecked = true
                    promoPrice.setText(getNumberFromString(discount).toString())
                    radioId = R.id.radioBtn_promo_price
                }

                discount.contains(getString(R.string.emegratuit)) -> {
                    radioBtnNemeGratuite.isChecked = true
                    numberWishIsfreeTxvw.setText(getNumberFromString(discount).toInt().toString())
                    radioId = R.id.radioBtn_NemeGratuite
                }

                discount.contains(getString(R.string.pourcentage)) -> {
                    radioBtnDiscount.isChecked = true
                    discountPoucentageTxVw.setText(getNumberFromString(discount).toString())
                    radioId = R.id.radioBtn_discount
                }
                discount.contains(getString(R.string.aveccartefid)) -> {
                    radioBtnPricewithCartFidelite.isChecked = true
                    priceWithFidCardTxVw.setText(getNumberFromString(discount).toString())
                    radioId = R.id.radioBtn_pricewith_cartFidelite
                }
                discount.contains(getString(R.string.Leprix)) -> {
                    radioBtnPrixPack.isChecked = true
                    nbrDanslepackEdittex.setText(getNumbersFromString(discount, getString(R.string.Leprix), getString(R.string.est)).toInt().toString())
                    prixPourPackTxVw.setText(getNumbersFromString(discount, getString(R.string.est), getString(R.string.TND)).toString())
                    radioId = R.id.radioBtn_prix_pack
                }

                else  -> {
                    radioBtnNull.isChecked = true
                    radioId = R.id.radioBtn_null
                }
            }
        }

    }


    private fun goToAddProductFragmenet() {
      dismiss()
        /*  val action =
              PriceRemarqueFragmentDirections.actionPriceRemarqueFragmentToAddProductFragment()
          NavHostFragment.findNavController(this).navigate(action)*/
    }

}

