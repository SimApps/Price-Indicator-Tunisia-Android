package com.amirami.simapp.priceindicatortunisia.dialoguesfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardRoomViewModel
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductInfoViewModel
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.shopingfragment.ShopListRoomViewModel
import com.amirami.simapp.priceindicatortunisia.databinding.YesNoDialogueBinding
import com.amirami.simapp.priceindicatortunisia.fidcard.Barecode
import com.amirami.simapp.priceindicatortunisia.model.Product
import com.amirami.simapp.priceindicatortunisia.utils.Functions.dynamicToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.prod_name_array
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.narify.netdetect.NetDetect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogueFragment : BottomSheetDialogFragment() {
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()
    private val shopListRoomViewModel: ShopListRoomViewModel by activityViewModels()
    private val fidCardRoomViewModel: FidCardRoomViewModel by activityViewModels()
    private val productsViewModel: ProductsViewModel by activityViewModels()

    lateinit var _binding: YesNoDialogueBinding

    // This property is only valid between onCreateView and
// onDestroyView.

    val args: DialogueFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = YesNoDialogueBinding.inflate(inflater, container, false)
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        args.barecodetype
        _binding.apply {
            if (Functions.isNumber(args.action)) {
                messageTxtVw.text = getString(
                    R.string.ID_Name,
                    args.action,
                    args.searchtext
                )
            }
            else when (args.action) {

                "deleteAll" -> {
                    messageTxtVw.visibility = View.GONE
                }

                "deleteprodFirestore" -> messageTxtVw.text =
                    getString(R.string.ID_Name, args.searchtext, args.barecodetype)

                "deleteOneItem" -> messageTxtVw.text =
                    getString(R.string.ID_Name, args.searchtext, args.barecodetype)


                "add" -> messageTxtVw.text = getString(R.string.codebarre_ID, args.searchtext)


                "addFROMADDFRAG" -> messageTxtVw.text =
                    getString(R.string.codebarre_ID, args.searchtext)


                "deleteFidCard" -> messageTxtVw.text ="Nom : "+args.barecodename

                "addFidCard" -> messageTxtVw.visibility = View.GONE
                "editFidCard"-> messageTxtVw.visibility = View.GONE

                "signinOut" -> messageTxtVw.text = getString(R.string.Voulez_vous_vous_déconnecter)

                "whatIsthisApp" -> messageTxtVw.text = getString(
                    R.string.Qu_est_ce_que_cette_application,
                    prod_name_array.size.toString()
                )

                "contactus" -> messageTxtVw.text = getString(R.string.jai_un_probleme_message)


                /*
                else -> {
                    messageTxtVw.text= getString(R.string.ID_Name,
                        Functions.globalid ,
                        Functions.globalname
                    )
                }*/
            }



            if (Functions.isNumber(args.action)) {
                TitleTxtVw.text = getString(R.string.Voulez_vous_suprimer_ce_produit)
            }
            else when (args.action) {

                "add" -> TitleTxtVw.text =
                    getString(R.string.Ce_produit_nexiste_pas_Voulez_vous_ajoutez)

                "deleteprodFirestore" -> TitleTxtVw.text =
                    getString(R.string.Voulez_vous_suprimer_ce_produit)

                "addFROMADDFRAG" -> TitleTxtVw.text =
                    getString(R.string.Ce_produit_nexiste_pas_Voulez_vous_ajoutez)


                "deleteAll" -> TitleTxtVw.text =
                    getString(R.string.Voulez_vous_suprimer_toute_liste_courses)

                "deleteOneItem" -> TitleTxtVw.text =
                    getString(R.string.Voulez_vous_suprimer_produit_liste_courses)

                "deleteFidCard" -> TitleTxtVw.text =
                    getString(R.string.Voulez_vous_suprimer_cette_carte_fid)

                "addFidCard" -> {
                    TitleTxtVw.text = getString(R.string.ajouterCarte)
                    btnOui.text = getString(R.string.Ajouter)
                    btnNon.visibility = View.GONE
                    productFidcardnameInputLayout.visibility = View.VISIBLE
                    productCodebarreInputLayout.visibility = View.VISIBLE
                    productCodebarreInput.setText(args.searchtext)

                }
                "editFidCard"-> {
                    TitleTxtVw.text = getString(R.string.modifierCarte)
                    btnOui.text = getString(R.string.Modifier)
                    btnNon.visibility = View.GONE
                    productFidcardnameInputLayout.visibility = View.VISIBLE
                    productCodebarreInputLayout.visibility = View.VISIBLE
                    productFidcardnameInput.setText(args.barecodename)
                    productCodebarreInput.setText(args.searchtext)

                }

                "signinOut" -> {
                    TitleTxtVw.visibility = View.GONE
                    btnOui.text = getString(R.string.oui)
                    btnNon.text = getString(R.string.non)
                }
                "whatIsthisApp" -> {
                    btnOui.visibility = View.GONE
                    TitleTxtVw.visibility = View.GONE
                    btnNon.visibility = View.GONE
                   // yesNoViewDialogue.visibility = View.GONE
                }

                "contactus" -> {
                    TitleTxtVw.visibility = View.GONE
                    btnOui.text = getString(R.string.nous_contacter_par_email)
                    btnNon.visibility = View.GONE
                  //  yesNoViewDialogue.visibility = View.GONE
                }

            }

            btnOui.setOnClickListener {
                if (Functions.isNumber(args.action)) {

                    //  productInfoViewModel.deleteProductFromDialogue(args.action,args.searchtext)
                    dismiss()
                    //     findNavController(this@DialogFragment).navigate(R.id.action_dialogFragment_to_AddProductFragment)


                }
                else when (args.action) {
                    "add" -> {

                        NetDetect.check { isConnected: Boolean ->
                            if (isConnected) {
                                Functions.bottomsheetStateInfo = "putonlyproductID"
                                val product = Product(
                                    args.searchtext, "","", "", "","", "", "","", "", "","", "", "", "", "", "", "", "","", "", "","", "", "","", "", "","", "", "","", "", "","", "", "","", "", "","", "", "",""
                                 )

                                productInfoViewModel.putprodInfoTomodify(product)

                              //  productInfoViewModel.putProductInfoFromAddDialogue(args.searchtext)
                                // findNavController(parentFragment!!).navigate(R.id.action_MainFragment_to_AddProductFragment)
                                this@DialogueFragment.findNavController().navigate(R.id.action_dialogFragment_to_AddProductFragment) //  NavHostFragment.findNavController(this@DialogueFragment).navigate(R.id.action_dialogFragment_to_AddProductFragment)
                            }
                            else errorToast(requireContext(), getString(R.string.connecter_pour_modifier_produit))


                        }

                    }

                    "addFROMADDFRAG" -> {
                        NetDetect.check { isConnected: Boolean ->
                            if (isConnected) {
                                //  productInfoViewModel.putProductInfoFromAddDialogue(args.searchtext)
                                Functions.bottomsheetStateInfo = "putonlyproductID"
                                val product = Product(
                                    args.searchtext, "","", "", "","", "", "","", "", "","", "", "", "", "", "", "", "","", "", "","", "", "","", "", "","", "", "","", "", "","", "", "","", "", "","", "", "",""
                                )

                                productInfoViewModel.putprodInfoTomodify(product)
                                dismiss()
                            }

                            else errorToast(requireContext(),getString(R.string.erreurconexion))


                            }


                    }


                    "deleteAll" -> {
                        shopListRoomViewModel.deleteAll(getString(R.string.toute_liste_courses_suprimer))
                        dismiss()
                    }


                    "deleteOneItem" -> {
                        shopListRoomViewModel.deleteItem(
                            args.searchtext.toLong(),
                            getString(R.string.product_deleted_in_shoping_list,args.barecodetype)
                        )
                        dismiss()
                    }

                    "deleteFidCard" -> {
                        fidCardRoomViewModel.deletebyValue(
                            args.searchtext,
                            getString(R.string.fidcard_deleted)
                        )
                        dismiss()
                    }

                    "deleteprodFirestore" -> {
                        val deleteProductniFirestore =
                            productsViewModel.deleteProduct(args.searchtext)
                        deleteProductniFirestore.observe(viewLifecycleOwner) {
                            if (it.e == null){
                                deleteProductNamesArrayInFirestore(args.barecodetype)
                                productsViewModel.firestoreOperationState(
                                    getString(R.string.succe_produit),
                                    "",
                                    ""
                                )



                            }

                            else productsViewModel.firestoreOperationState(
                                getString(R.string.erreur_supression),
                                args.searchtext,
                                args.barecodetype
                            )



                            dismiss()

                        }
                    }

                    "addFidCard" -> {
                        if (productFidcardnameInput.text.toString() == "") {
                            productFidcardnameInputLayout.error = "Il faut ajouter un nom !"

                        } else if (productCodebarreInput.text.toString() == "") {
                            productCodebarreInputLayout.error = "Il faut ajouter le code barre de la carte fid !"
                        } else if (productFidcardnameInput.text.toString() != "" && productCodebarreInput.text.toString() != "") {


                            val fidcard = Barecode(
                                productFidcardnameInput.text.toString(),
                                args.searchtext,
                                args.barecodetype
                            )
                            fidCardRoomViewModel.insertItem(fidcard)
                            dismiss()
                        }

                    }

                    "editFidCard" -> {
                        if (productFidcardnameInput.text.toString() == "") {
                            productFidcardnameInputLayout.error = "Il faut ajouter un nom !"

                        } else if (productCodebarreInput.text.toString() == "") {
                            productCodebarreInputLayout.error =
                                "Il faut ajouter le code barre de la carte fid !"
                        } else if (productFidcardnameInput.text.toString() != "" && productCodebarreInput.text.toString() != "") {


                            val fidcard = Barecode(
                                args.id.toLong(),
                                productFidcardnameInput.text.toString(),
                                productCodebarreInput.text.toString(),
                                args.barecodetype
                            )
                            fidCardRoomViewModel.insertItem(fidcard)
                            dismiss()
                        }

                    }

                    "signinOut" -> {
                        productInfoViewModel.putProductInfoFromAddDialogue("signinOut")
                        dismiss()
                    }

                    "whatIsthisApp" -> dismiss()

                    "contactus" -> {
                        productInfoViewModel.putProductInfoFromAddDialogue("contactus")
                        dismiss()
                    }


                }

                //         getDialog()?.dismiss()
            }

            btnNon.setOnClickListener {
                dismiss()
            }
        }


    }

    private fun deleteProductNamesArrayInFirestore(productName:String){
        val deleteProductNamesArrayInFirestore= productsViewModel.deleteProductNamesArrayInFirestore(productName)
        deleteProductNamesArrayInFirestore.observe(viewLifecycleOwner) {
            //if (it != null)  if (it.data!!)  prod name array updated
            if (it.e != null) {
                //prod bame array not updated
                dynamicToast(requireContext(), it.e!!)

            }
        }
    }
}