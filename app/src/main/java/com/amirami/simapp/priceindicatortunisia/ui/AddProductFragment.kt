package com.amirami.simapp.priceindicatortunisia.ui

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.capitalizeWords
import com.amirami.simapp.priceindicatortunisia.utils.Functions.changemodificationpricedate
import com.amirami.simapp.priceindicatortunisia.utils.Functions.checkPermission
import com.amirami.simapp.priceindicatortunisia.utils.Functions.copyToclipord
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getCurrentDate
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getuserid
import com.amirami.simapp.priceindicatortunisia.utils.Functions.hideKeyboard
import com.amirami.simapp.priceindicatortunisia.utils.Functions.isDouble
import com.amirami.simapp.priceindicatortunisia.utils.Functions.isNumber
import com.amirami.simapp.priceindicatortunisia.utils.Functions.loadimageurl
import com.amirami.simapp.priceindicatortunisia.utils.Functions.prod_name_array
import com.amirami.simapp.priceindicatortunisia.utils.Functions.listnerInterstitialAd
import com.amirami.simapp.priceindicatortunisia.utils.Functions.nbrInterstitialAdShowed
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeAllDigitExeptX
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeWord
import com.amirami.simapp.priceindicatortunisia.utils.Functions.replacesiez
import com.amirami.simapp.priceindicatortunisia.utils.Functions.searchType
import com.amirami.simapp.priceindicatortunisia.utils.Functions.searchtext
import com.amirami.simapp.priceindicatortunisia.utils.Functions.searchtype
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentAddProductBinding
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import java.util.*
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeLeadingZeroes
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.viewmodel.*
import com.amirami.simapp.priceindicatortunisia.model.Product
import com.amirami.simapp.priceindicatortunisia.model.ProdRoomNamesModel
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesRoomViewModel
import com.amirami.simapp.priceindicatortunisia.shopingfragment.ProductShopingRoom
import com.amirami.simapp.priceindicatortunisia.shopingfragment.ShopListRoomViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Converters.fromArrayList
import com.amirami.simapp.priceindicatortunisia.utils.Functions.PriceReFormating
import com.amirami.simapp.priceindicatortunisia.utils.Functions.dynamicToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.amirami.simapp.priceindicatortunisia.utils.Functions.showInterstitialAd
import com.amirami.simapp.priceindicatortunisia.utils.Functions.succesToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.warningToast
import com.amirami.simapp.priceindicatortunisia.utils.exhaustive
import com.narify.netdetect.NetDetect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal


@AndroidEntryPoint
class AddProductFragment : Fragment(R.layout.fragment_add_product) {
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()
    private val preferencesViewModel: PreferencesViewModel by activityViewModels()
    private val productsViewModel: ProductsViewModel by activityViewModels()
    private val prodNamesRoomViewModel: ProdNamesRoomViewModel by activityViewModels()
    private val shopListRoomViewModel: ShopListRoomViewModel by activityViewModels()

    private val products: MutableList<Product> = mutableListOf()


    var preveiewsProdame = ""
    var preveiewsMonoprixPrice = ""
    var preveiewsMgPrice = ""
    var preveiewsGeantPrice = ""
    var preveiewsAzizaPrice = ""
    var preveiewsCarrefourPrice = ""

    var preveiewsMonoprixremarque = ""
    var preveiewsMgremarque = ""
    var preveiewsGeantremarque = ""
    var preveiewsAzizaremarque = ""
    var preveiewsCarrefourremarque = ""


    var geantmoddate: String = ""
    var mgmoddate: String = ""
    var monoprixmoddate: String = ""
    var azzizamoddate: String = ""
    var carrefourmoddate: String = ""

    var geantremarqmoddate: String = ""
    var mgremarqmoddate: String = ""
    var monoprixremarqmoddate: String = ""
    var azzizaremarqmoddate: String = ""
    var carrefourremarqmoddate: String = ""

    var geantbonusfid = ""
    var monoprixbonusfid = ""
    var carrefourbonusfid = ""
    var azzizabonusfid = ""
    var mgbonusfid = ""


    var monoprixHistoryPriceArray: ArrayList<String> = ArrayList()
    var mgHistoryPriceArray: ArrayList<String> = ArrayList()
    var azizaHistoryPriceArray: ArrayList<String> = ArrayList()
    var carrefourHistoryPriceArray: ArrayList<String> = ArrayList()
    var geantHistoryPriceArray: ArrayList<String> = ArrayList()


    lateinit var _binding: FragmentAddProductBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddProductBinding.bind(view)

        Locale.setDefault(Locale("en", "US"))

        remarquesCliks()
        setUserAccess()
        clickSaveBtn()

        clickScanBtn()
        clickTypes()
        clickSize()


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.getResponseFromFirestoreUsingCoroutines.collectLatest { dataOrException ->
                    val productList = dataOrException.data
                    if (productList != null) {
                        products.addAll(productList)
                        if (productList.isNotEmpty()) {
                            _binding.listViewAddprod.visibility = View.GONE
                            _binding.nestedScroll.visibility = View.VISIBLE

                            // product.setDocumentId(documentSnapshot.id)
                            val id: String = productList[0].id!!.toString()
                            val name: String = productList[0].name!!
                            val nameArabe: String = productList[0].nameArabe!!
                            val marques: String = productList[0].marques!!
                            val marquesArabe: String = productList[0].marquesArabe!!
                            val description: String = productList[0].description!!
                            val descriptionArabe: String = productList[0].descriptionArabe!!
                            val imageurl: String = productList[0].imageurl!!
                            val type: String = productList[0].type!!
                            val typeSub: String = productList[0].typesub!!
                            val typeSubsub: String = productList[0].typesubsub!!
                            val sieze: String = productList[0].sieze!!
                            val monoprixprice: String = productList[0].monoprixprice!!
                            val monoprixremarq: String = productList[0].monoprixremarq!!
                            val mgprice: String = productList[0].mgprice!!
                            val mgremarq: String = productList[0].mgremarq!!
                            val carrefourprice: String = productList[0].carrefourprice!!
                            val carrefourremarq: String = productList[0].carrefourremarq!!
                            val azzizaprice: String = productList[0].azzizaprice!!
                            val azzizaremarq: String = productList[0].azzizaremarq!!
                            val geantprice: String = productList[0].geantprice!!
                            val geantremarq: String = productList[0].geantremarq!!

                            preveiewsMonoprixPrice = monoprixprice
                            preveiewsCarrefourPrice = carrefourprice
                            preveiewsMgPrice = mgprice
                            preveiewsAzizaPrice = azzizaprice
                            preveiewsGeantPrice = geantprice

                            preveiewsMonoprixremarque = productList[0].monoprixremarq!!
                            preveiewsMgremarque = productList[0].mgremarq!!
                            preveiewsGeantremarque = productList[0].geantremarq!!
                            preveiewsAzizaremarque = productList[0].azzizaremarq!!
                            preveiewsCarrefourremarque = productList[0].carrefourremarq!!

                            geantbonusfid = productList[0].geantbonusfid!!
                            monoprixbonusfid = productList[0].monoprixbonusfid!!
                            carrefourbonusfid = productList[0].carrefourbonusfid!!
                            azzizabonusfid = productList[0].azzizabonusfid!!
                            mgbonusfid = productList[0].mgbonusfid!!

                            geantmoddate = productList[0].geantmodifdate!!
                            mgmoddate = productList[0].mgmodifdate!!
                            monoprixmoddate = productList[0].monoprixmodifdate!!
                            azzizamoddate = productList[0].azzizamodifdate!!
                            carrefourmoddate = productList[0].carrefourmodifdate!!

                            geantremarqmoddate = productList[0].geantremarqmodifdate!!
                            mgremarqmoddate = productList[0].mgremarqmodifdate!!
                            monoprixremarqmoddate = productList[0].monoprixremarqmodifdate!!
                            azzizaremarqmoddate = productList[0].azzizaremarqmodifdate!!
                            carrefourremarqmoddate = productList[0].carrefourremarqmodifdate!!

                            preveiewsProdame = name


                            _binding.apply {
                                ProductIDInput.setText(getString(R.string.codebarre_ID, id))
                                productNameInput.setText(name)
                                productNameArabeInput.setText(nameArabe)
                                productMarquesInput.setText(marques)
                                productMarquesArabeInput.setText(marquesArabe)
                                //  product_type_input.text = Functions.globaltypeofproduct
                                productSiezeInput.setText(replacesiez(sieze))
                                productDescriptionInput.setText(description)
                                productDescriptionArabeInput.setText(descriptionArabe)
                                productImageurlInput.setText(imageurl)
                                productTypesInput.setText(type)
                                productTypesSubInput.setText(typeSub)
                                productTypesSubsubInput.setText(typeSubsub)
                                spinnerUnitofmesurAutocomplete.setText(removeAllDigitExeptX(sieze))

                                loadimageurl(requireContext(), imageurl, imageViewadd)
                            }


                            //  setMagasineInputText(name,pricenotdefined(preveiewsCarrefourPrice) , carrefourremarq, pricenotdefined(preveiewsMonoprixPrice) , monoprixremarq,pricenotdefined(preveiewsGeantPrice) , geantremarq,pricenotdefined(preveiewsMgPrice) , mgremarq, pricenotdefined(preveiewsAzizaPrice), azzizaremarq)

                            setMagasineInputText()

                            monoprixHistoryPriceArray = productList[0].monoprixPriceHistory
                            mgHistoryPriceArray = productList[0].mgpriceHistory
                            azizaHistoryPriceArray = productList[0].azizaPriceHistory
                            carrefourHistoryPriceArray = productList[0].carrefourPriceHistory
                            geantHistoryPriceArray = productList[0].geantPriceHistory


                            val product = ProductShopingRoom(
                                id.toLong(),
                                productList[0].date.toString(),
                                name,
                                nameArabe,
                                marques,
                                marquesArabe,
                                1.0,
                                description,
                                descriptionArabe,
                                imageurl,
                                type,
                                typeSub,
                                typeSubsub,
                                sieze,
                                monoprixprice,
                                monoprixremarq,
                                mgprice,
                                mgremarq,
                                carrefourprice,
                                carrefourremarq,
                                azzizaprice,
                                azzizaremarq,
                                geantprice,
                                geantremarq,

                                productList[0].monoprixremarqmodifdate,
                                productList[0].mgremarqmodifdate,
                                productList[0].carrefourremarqmodifdate,
                                productList[0].azzizaremarqmodifdate,
                                productList[0].geantremarqmodifdate,

                                productList[0].monoprixmodifdate!!,
                                productList[0].mgmodifdate!!,
                                productList[0].carrefourmodifdate!!,
                                productList[0].azzizamodifdate!!,
                                productList[0].geantmodifdate!!,

                                productList[0].monoprixbonusfid!!,
                                productList[0].mgbonusfid!!,
                                productList[0].carrefourbonusfid!!,
                                productList[0].azzizabonusfid!!,
                                productList[0].geantbonusfid!!,

                                productList[0].monoprixbonusfidmodifdate,
                                productList[0].mgbonusfidmodifdate,
                                productList[0].carrefourbonusfidmodifdate,
                                productList[0].azzizabonusfidmodifdate,
                                productList[0].geantbonusfidmodifdate,

                                fromArrayList(productList[0].monoprixPriceHistory),
                                fromArrayList(productList[0].mgpriceHistory),
                                fromArrayList(productList[0].azizaPriceHistory),
                                fromArrayList(productList[0].carrefourPriceHistory),
                                fromArrayList(productList[0].geantPriceHistory)
                            )
if(removeWord(_binding.ProductIDInput.text.toString(), getString(R.string.codebarre_ID, ""))==id){
    clickBtnAddtoShopList(product)
    clickBtnDeleteProd(id, name)
}


                        }
                     /*   else {

                    //         if (searchtype == "id") {
                                NetDetect.check { isConnected: Boolean ->
                                if (isConnected) {
                                 /*   val action = AddProductFragmentDirections.actionAddProductFragmentToDialogFragment(
                                            "addFROMADDFRAG",
                                            removeLeadingZeroes(searchtext)
                                        )
                                    this@AddProductFragment.findNavController().navigate(action)*/
                                }
                                else errorToast(requireContext(),getString(R.string.erreurconexion))
                            }
                         //   }
                          //  else warningToast(requireContext(),"$searchtext  n'est pas disponible dans notre base de donnée!")

                        }
*/
                    }

                    if (dataOrException.e != null) {
                        if (dataOrException.e == "add") {
                            if (searchtype == "id") {
                                NetDetect.check { isConnected: Boolean ->
                                    if (isConnected) {
                                        val action =
                                            AddProductFragmentDirections.actionAddProductFragmentToDialogFragment(
                                                "addFROMADDFRAG",
                                                removeLeadingZeroes(searchtext)
                                            )
                                        this@AddProductFragment.findNavController()
                                            .navigate(action) //   NavHostFragment.findNavController(requireParentFragment()).navigate(action)

                                    }
                                    else errorToast(requireContext(),getString(R.string.erreurconexion))
                                    // else errorToast()
                                }
                            }

                        }
                        else errorToast(requireContext(),dataOrException.e.toString())
                    }
                    hideProgressBar()

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productInfoViewModel.putProductInfo.collectLatest {

                    when (it) {
                        is ProductInfoViewModel.LatestprodInfoTomodify.Success ->{
                            searchTextAndSuggestList()
                            run {
                                //      if (Functions.bottomsheetStateInfo != "FirestoreloadFrom_AddFragment") {
                                when (Functions.bottomsheetStateInfo) {
                                    "productInfo" -> {
                                        _binding.apply {
                                            ProductIDInput.setText(getString(R.string.codebarre_ID, it.product.id))
                                            productNameInput.setText(it.product.name!!)
                                            productNameArabeInput.setText(it.product.nameArabe)
                                            productMarquesInput.setText(it.product.marques)
                                            productMarquesArabeInput.setText(it.product.marquesArabe)
                                            //  product_type_input.text = Functions.globaltypeofproduct
                                            productSiezeInput.setText(replacesiez(it.product.sieze!!))
                                            productDescriptionInput.setText(it.product.description)
                                            productDescriptionArabeInput.setText(it.product.descriptionArabe)
                                            productImageurlInput.setText(it.product.imageurl)
                                            loadimageurl(requireContext(), it.product.imageurl!!, imageViewadd)
                                            productTypesInput.setText(it.product.type)
                                            productTypesSubInput.setText(it.product.typesub)
                                            productTypesSubsubInput.setText(it.product.typesubsub)
                                            spinnerUnitofmesurAutocomplete.setText(removeAllDigitExeptX(it.product.sieze!!))

                                        }
                                        preveiewsProdame = it.product.name!!

                                        preveiewsMonoprixPrice = PriceReFormating(it.product.monoprixprice!!)
                                        preveiewsCarrefourPrice = PriceReFormating(it.product.carrefourprice!!)
                                        preveiewsMgPrice = PriceReFormating(it.product.mgprice!!)
                                        preveiewsAzizaPrice = PriceReFormating(it.product.azzizaprice!!)
                                        preveiewsGeantPrice = PriceReFormating(it.product.geantprice!!)

                                        preveiewsMonoprixremarque = it.product.monoprixremarq!!
                                        preveiewsMgremarque = it.product.mgremarq!!
                                        preveiewsGeantremarque = it.product.geantremarq!!
                                        preveiewsAzizaremarque = it.product.azzizaremarq!!
                                        preveiewsCarrefourremarque = it.product.carrefourremarq!!
                                        setMagasineInputText()



                                     val productShopingRoom=   ProductShopingRoom(
                                            it.product.id!!.toLong(),
                                            it.product.date!!,
                                            it.product.name!!,
                                            it.product.nameArabe!!,
                                            it.product.marques!!,
                                            it.product.marquesArabe!!,
                                            1.0,
                                            it.product.description!!,
                                            it.product.descriptionArabe!!,
                                            it.product.imageurl!!,
                                            it.product.type!!,
                                            it.product.typesub!!,
                                            it.product.typesubsub!!,
                                            it.product.sieze!!,
                                            it.product.monoprixprice!!,
                                            it.product.monoprixremarq!!,
                                            it.product.mgprice!!,
                                            it.product.mgremarq!!,
                                            it.product.carrefourprice!!,
                                            it.product.carrefourremarq!!,
                                            it.product.azzizaprice!!,
                                            it.product.azzizaremarq!!,
                                            it.product.geantprice!!,
                                            it.product.geantremarq!!,
                                            it.product.monoprixmodifdate!!,
                                            it.product.mgmodifdate!!,
                                            it.product.carrefourmodifdate!!,
                                            it.product.azzizamodifdate!!,
                                            it.product.geantmodifdate!!,

                                            it.product.monoprixremarqmodifdate,
                                            it.product.mgremarqmodifdate,
                                            it.product.carrefourremarqmodifdate,
                                            it.product.azzizaremarqmodifdate,
                                            it.product.geantremarqmodifdate,

                                            it.product.monoprixbonusfid!!,
                                            it.product.mgbonusfid!!,
                                            it.product.carrefourbonusfid!!,
                                            it.product.azzizabonusfid!!,
                                            it.product.geantbonusfid!!,

                                            it.product.monoprixbonusfidmodifdate,
                                            it.product.mgbonusfidmodifdate,
                                            it.product.carrefourbonusfidmodifdate,
                                            it.product.azzizabonusfidmodifdate,
                                            it.product.geantbonusfidmodifdate,

                                            fromArrayList(it.product.monoprixPriceHistory),
                                            fromArrayList(it.product.mgpriceHistory),
                                            fromArrayList(it.product.azizaPriceHistory),
                                            fromArrayList(it.product.carrefourPriceHistory),
                                            fromArrayList(it.product.geantPriceHistory)
                                        )
                                        if(removeWord(_binding.ProductIDInput.text.toString(), getString(R.string.codebarre_ID, ""))==it.product.id!!){
                                            clickBtnAddtoShopList(productShopingRoom)
                                            clickBtnDeleteProd(it.product.id!!, it.product.name!!)
                                            //  search_input=findViewById<SearchView>(R.id.search_input)
                                        }

                                    }
                                    "nobill" -> {
                                        searchtext = it.product.id.toString()
                                        searchType(searchtext)
                                        loadProducttomodify()
                                    }
                                    "putonlyproductID" -> {
                                        resetOnluInputtext()
                                        restProductVar()

                                        _binding.ProductIDInput.setText(getString(R.string.codebarre_ID, it.product.id))
                                    }
                                }
                            }
                            //  }
                        }
                        is ProductInfoViewModel.LatestprodInfoTomodify.Error -> errorToast(requireContext(),it.exception.toString())
                    }


                }

            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productInfoViewModel.barecodeScanEvents.collectLatest { event ->
                when (event) {
                    is ProductInfoViewModel.BareCodeScanerEvents.PutBareCodeScanerInfo -> {
                        run {
                            getResultFromScanFragment(event.barecode.value!!.toString())
                        }
                    }
                }.exhaustive
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productInfoViewModel.productInfoEvents.collectLatest { event ->
                when (event) {
                    is ProductInfoViewModel.ProductEvents.PutRemarqueInfo -> {
                        putRemarqueFragmentInfo(
                            event.magasin,
                            event.discountInfo,
                            event.bonusfidnfo
                        )

                        _binding.listViewAddprod.visibility = View.GONE
                        _binding.nestedScroll.visibility = View.VISIBLE
                    }
                    is ProductInfoViewModel.ProductEvents.PutProductTypesInfo -> {
                        run {
                            _binding.productTypesInput.setText(event.type)
                            _binding.productTypesSubInput.setText("")
                            _binding.productTypesSubsubInput.setText("")
                        }
                    }
                    is ProductInfoViewModel.ProductEvents.PutProductSubTypesInfo -> {
                        run {
                            _binding.productTypesSubInput.setText(event.subtype)

                            _binding.productTypesSubsubInput.setText("")
                        }
                    }
                    is ProductInfoViewModel.ProductEvents.PutProductSubSubTypesInfo -> {
                        run {
                            _binding.productTypesSubsubInput.setText(event.subsubtype)
                        }
                    }
                    is ProductInfoViewModel.ProductEvents.PutProductSizeInfo -> {
                        run {
                            _binding.spinnerUnitofmesurAutocomplete.setText(event.size)
                        }
                    }

                }.exhaustive
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            shopListRoomViewModel.shopListEvents.collectLatest { event ->
                when (event) {
                    is ShopListRoomViewModel.ShopListEvents.ProdAddToShopMsg -> {
                        succesToast(requireContext(),event.msg)
                    }
                    else -> {}
                }.exhaustive
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productsViewModel.firestoreEvents.collectLatest { event ->
                when (event) {
                    is ProductsViewModel.FirestoreEvents.ProdDeleteMsg -> {
                        if (event.msg == getString(R.string.succe_produit)) {
                            DynamicToast.makeSuccess(requireContext(), event.msg, 9).show()
                            resetOnluInputtext()
                            restProductVar()
                        } else emailDeleteProd(event.id, event.name)
                    }
                    else -> {}
                }.exhaustive
            }
        }


    }


    private fun saveproduct() {
        displayProgressBar()

        val productID = _binding.ProductIDInput.text.toString()
        val productName = _binding.productNameInput.text.toString().capitalizeWords()
        val productNameArabe = _binding.productNameArabeInput.text.toString().capitalizeWords()
        val productMarques = _binding.productMarquesInput.text.toString()
        val productMarquesArabe = _binding.productMarquesArabeInput.text.toString()
        val productDescription = _binding.productDescriptionInput.text.toString()
        val productDescriptionArabe = _binding.productDescriptionArabeInput.text.toString()
        val productImageurl = _binding.productImageurlInput.text.toString()
        val productType = _binding.productTypesInput.text.toString()//prodType.capitalizeWords()
        val productTypeSub = _binding.productTypesSubInput.text.toString()
        val productTypeSubsub = _binding.productTypesSubsubInput.text.toString()
        val monoprixPrice = _binding.monoprixPriceInput.text.toString()
        val monoprixRemarq = _binding.monoprixRemarqInput.text.toString()
        val mgPrice = _binding.magasingeneralPriceInput.text.toString()
        val mgRemarq = _binding.magasingeneralRemarqInput.text.toString()
        val carrefourPrice = _binding.carrefourPriceInput.text.toString()
        val carrefourRemarq = _binding.carrefourRemarqInput.text.toString()
        val azzizaPrice = _binding.azzizaPriceInput.text.toString()
        val azzizaRemarq = _binding.azzizaRemarqInput.text.toString()
        val geantPrice = _binding.geantPriceInput.text.toString()
        val geantRemarq = _binding.geantRemarq.text.toString()


        val currentDate = getCurrentDate()


        if (checkPriceChange(mgPrice, preveiewsMgPrice))
            mgHistoryPriceArray.add(addPricesHistoryArrays(mgPrice, currentDate))
        if (checkPriceChange(monoprixPrice, preveiewsMonoprixPrice))
            monoprixHistoryPriceArray.add(addPricesHistoryArrays(monoprixPrice, currentDate))
        if (checkPriceChange(azzizaPrice, preveiewsAzizaPrice))
            azizaHistoryPriceArray.add(addPricesHistoryArrays(azzizaPrice, currentDate))
        if (checkPriceChange(carrefourPrice, preveiewsCarrefourPrice))
            carrefourHistoryPriceArray.add(addPricesHistoryArrays(carrefourPrice, currentDate))
        if (checkPriceChange(geantPrice, preveiewsGeantPrice))
            geantHistoryPriceArray.add(addPricesHistoryArrays(geantPrice, currentDate))


        val product = Product(
            removeWord(productID, getString(R.string.codebarre_ID, "")),
            getCurrentDate(),
            productName,
            productNameArabe,
            productMarques,
            productMarquesArabe,
            productDescription,
            productDescriptionArabe,
            productImageurl,
            productType,
            productTypeSub,
            productTypeSubsub,
            _binding.productSiezeInput.text.toString() + _binding.spinnerUnitofmesurAutocomplete.text.toString(),
            monoprixPrice,
            monoprixRemarq,
            mgPrice,
            mgRemarq,
            carrefourPrice,
            carrefourRemarq,
            azzizaPrice,
            azzizaRemarq,
            geantPrice,
            geantRemarq,


            /*   changemodificationpricedate(preveiewsMonoprixremarque, monoprixRemarq, monoprixremarqmoddate),
               changemodificationpricedate(preveiewsMgremarque, mgRemarq, mgremarqmoddate),
               changemodificationpricedate(preveiewsCarrefourremarque, carrefourRemarq,carrefourremarqmoddate),
               changemodificationpricedate(preveiewsAzizaremarque, azzizaRemarq, azzizaremarqmoddate),
               changemodificationpricedate(preveiewsGeantremarque, geantRemarq, geantremarqmoddate),
   */

            if (monoprixRemarq == getString(R.string.ajouter_promotions)) "" else currentDate,
            if (mgRemarq == getString(R.string.ajouter_promotions)) "" else currentDate,
            if (carrefourRemarq == getString(R.string.ajouter_promotions)) "" else currentDate,
            if (azzizaRemarq == getString(R.string.ajouter_promotions)) "" else currentDate,
            if (geantRemarq == getString(R.string.ajouter_promotions)) "" else currentDate,

            getuserid(),

            changemodificationpricedate(preveiewsMonoprixPrice, monoprixPrice, monoprixmoddate),
            changemodificationpricedate(preveiewsMgPrice, mgPrice, mgmoddate),
            changemodificationpricedate(preveiewsCarrefourPrice, carrefourPrice, carrefourmoddate),
            changemodificationpricedate(preveiewsAzizaPrice, azzizaPrice, azzizamoddate),
            changemodificationpricedate(preveiewsGeantPrice, geantPrice, geantmoddate),

            monoprixbonusfid,
            mgbonusfid,
            carrefourbonusfid,
            azzizabonusfid,
            geantbonusfid,


            currentDate,
            currentDate,
            currentDate,
            currentDate,
            currentDate,


            monoprixHistoryPriceArray,
            mgHistoryPriceArray,
            azizaHistoryPriceArray,
            carrefourHistoryPriceArray,
            geantHistoryPriceArray
        )


        val isProductAddLiveData = productsViewModel.getProduct(product, product.id!!.toString())

        isProductAddLiveData.observe(viewLifecycleOwner) { dataOrException ->
            val isProductAdded = dataOrException.data
            if (isProductAdded != null) {
                if (isProductAdded) {


                    if (productName != preveiewsProdame) {
                        prodNamesRoomViewModel.deleteAll()
                        deleteProductNamesArrayInFirestore(preveiewsProdame)

                        if (prod_name_array.indexOf(preveiewsProdame) != -1)
                            prod_name_array[prod_name_array.indexOf(preveiewsProdame)] = productName
                        else prod_name_array.add(productName)

                        addProdNameArrayInFirestore(productName)

                        prodNamesRoomViewModel.insertItem(
                            ProdRoomNamesModel(
                                1,
                                fromArrayList(prod_name_array)
                            )
                        )
                    }

                    hideProgressBar()


                    resetOnluInputtext()

                    succesToast(requireContext(),getString(R.string.product_saved))

                    _binding.searchInput.setQuery("", false)
                }
            }


            if (dataOrException.e != null) {
                hideProgressBar()
                emailProdInfo(product)
                resetOnluInputtext()
            }
        }

    }

    private fun emailProdInfo(product: Product) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_email)))
            intent.type = "message/rfc822"//"text/plain"
            intent.putExtra(
                // Intent.EXTRA_EMAIL,
                Intent.EXTRA_TEXT,
                getString(
                    R.string.proposition_ajouter_produit,
                    removeWord(product.id!!, getString(R.string.codebarre_ID, "")),
                    product.name,
                    product.nameArabe,
                    product.marques,
                    product.marquesArabe,
                    product.type,
                    product.typesub,
                    product.typesubsub,
                    /* if(UnitOfmesure !="Unité") replacesiez(product.sieze!!) +" $UnitOfmesure" else product.sieze!!*/
                    _binding.productSiezeInput.text.toString() + _binding.spinnerUnitofmesurAutocomplete.text.toString(),
                    product.description,
                    product.descriptionArabe,
                    product.monoprixprice,
                    product.monoprixremarq,
                    product.monoprixbonusfid,
                    product.mgprice,
                    product.mgremarq,
                    product.mgbonusfid,
                    product.carrefourprice,
                    product.carrefourremarq,
                    product.carrefourbonusfid,
                    product.azzizaprice,
                    product.azzizaremarq,
                    product.azzizabonusfid,
                    product.geantprice,
                    product.geantremarq,
                    product.geantbonusfid,
                    getuserid()
                )
            )

            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.Ajoutmodification_du_produit) + " : " + removeWord(
                    product.id!!,
                    getString(R.string.codebarre_ID, "")
                )
            )


            requireContext().startActivity(
                Intent.createChooser(
                    intent,
                    "Envoyer les informations du produits par "
                )
            )


        } catch (e: Exception) {
            //e.toString();
        }
    }

    private fun emailDeleteProd(prodId: String, prodName: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(requireContext().getString(R.string.app_email))
            )
            intent.type = "message/rfc822"//"text/plain"
            intent.putExtra(
                // Intent.EXTRA_EMAIL,
                Intent.EXTRA_TEXT,
                getString(
                    R.string.proposition_supression_produit,
                    prodId,
                    prodName,
                    getuserid()
                )
            )
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.suggestion_supression_produit) + " : $prodId"
            )

            requireContext().startActivity(
                Intent.createChooser(
                    intent,
                    getString(R.string.envoyer_info)
                )
            )

        } catch (e: Exception) {
            errorToast(requireContext(),e.toString())
        }
    }


    private fun deleteProductNamesArrayInFirestore(productName: String) {
        val deleteProductNamesArrayInFirestore =
            productsViewModel.deleteProductNamesArrayInFirestore(productName)
        deleteProductNamesArrayInFirestore.observe(viewLifecycleOwner) {
            //if (it != null)  if (it.data!!)  prod name array updated
            if (it.e != null) {
                //prod bame array not updated
                errorToast(requireContext(),it.e!!)
            }
        }
    }

    private fun addProdNameArrayInFirestore(productName: String) {
        val addProductNamesArrayInFirestore =
            productsViewModel.addProductNamesArrayInFirestore(productName)
        addProductNamesArrayInFirestore.observe(viewLifecycleOwner) {
            //if (it != null)  if (it.data!!)  prod name array updated
            if (it.e != null) {
                //prod bame array not updated
                errorToast(requireContext(),it.e!!)
            }

        }
    }


    private fun checkPriceChange(currentPrice: String, preveiewsPrice: String): Boolean =
        currentPrice != preveiewsPrice

    private fun addPricesHistoryArrays(currentPrice: String, date: String): String {
        var result = ""
        if (isDouble(currentPrice) && currentPrice.toDouble() > 0.000 /*&& currentPrice!=preveiewsPrice*/) {
            result = "$date:$currentPrice"
        }

        return result
    }


    private fun allowSaveProduct(): Boolean {
        var result = true

        if (_binding.productNameInput.text.toString() == "") {
            result = false
            _binding.productNameInputLayout.error =
                getString(R.string.Le_nom_du_produit_ne_doit_pas_etre_vide)
            // DynamicToast.makeError(requireContext(), getString(R.string.Le_nom_du_produit_ne_doit_pas_etre_vide), 9).show()
        }

        if (_binding.productTypesInput.text.toString() == "" && result) {
            result = false
            _binding.productTypesLayout.error =
                getString(R.string.Le_type_du_produit_ne_doit_pas_etre_vide)
            //  DynamicToast.makeError(requireContext(), getString(R.string.Le_type_du_produit_ne_doit_pas_etre_vide), 9).show()
        }


        if (!isNumber(
                removeWord(
                    _binding.ProductIDInput.text.toString(),
                    getString(R.string.codebarre_ID, "")
                )
            ) && result
        ) {
            result = false
            _binding.ProductIDInputLayout.error = getString(
                R.string.identifiant_produit_nest_pas_nombre,
                removeWord(
                    _binding.ProductIDInput.text.toString(),
                    getString(R.string.codebarre_ID, "")
                ),
            )
            errorToast( requireContext(),  getString(
                R.string.identifiant_produit_nest_pas_nombre,
                removeWord(
                    _binding.ProductIDInput.text.toString(),
                    getString(R.string.codebarre_ID, "")
                ),
            ))

        } else if (_binding.productSiezeInput.text.toString() != "" && _binding.spinnerUnitofmesurAutocomplete.text.toString() == "" && result) {
            result = false
            _binding.productSiezeInputLayout.error =
                getString(R.string.Sélectioné_une_unité_de_mésure)
            // DynamicToast.makeError(requireContext(), getString(R.string.Sélectioné_une_unité_de_mésure), 9).show()
        }

        //    }

        return result
    }

    private fun resetOnluInputtext() {


        _binding.apply {
            ProductIDInput.setText(getString(R.string.ScanproductIdoradditmanually))
            productNameInput.setText("")
            productNameArabeInput.setText("")
            productMarquesInput.setText("")
            productMarquesArabeInput.setText("")
            // product_type_input.text = ""
            productSiezeInput.setText("")
            spinnerUnitofmesurAutocomplete.setText("")
            productDescriptionInput.setText("")
            productDescriptionArabeInput.setText("")
            productImageurlInput.setText("")
            monoprixPriceInput.setText("")
            monoprixRemarqInput.setText("")
            magasingeneralPriceInput.setText("")
            magasingeneralRemarqInput.setText("")
            carrefourPriceInput.setText("")
            carrefourRemarqInput.setText("")
            azzizaPriceInput.setText("")
            azzizaRemarqInput.setText("")
            geantRemarq.setText("")
            geantPriceInput.setText("")
            productTypesInput.setText("")
            productTypesSubInput.setText("")
            productTypesSubsubInput.setText("")

            productNameInputLayout.isErrorEnabled = false
            productNameInputLayout.error = null
            productTypesLayout.isErrorEnabled = false
            productTypesLayout.error = null
            ProductIDInputLayout.isErrorEnabled = false
            ProductIDInputLayout.error = null
            productSiezeInputLayout.isErrorEnabled = false
            productSiezeInputLayout.error = null
        }


        loadimageurl(requireContext(), "", _binding.imageViewadd)

    }

    private fun restProductVar() {
        monoprixHistoryPriceArray.clear()
        mgHistoryPriceArray.clear()
        azizaHistoryPriceArray.clear()
        carrefourHistoryPriceArray.clear()
        geantHistoryPriceArray.clear()

        preveiewsProdame = ""
        preveiewsMonoprixPrice = ""
        preveiewsMgPrice = ""
        preveiewsGeantPrice = ""
        preveiewsAzizaPrice = ""
        preveiewsCarrefourPrice = ""

        geantremarqmoddate = ""
        mgremarqmoddate = ""
        monoprixremarqmoddate = ""
        azzizaremarqmoddate = ""
        carrefourremarqmoddate = ""

        preveiewsMonoprixremarque = ""
        preveiewsMgremarque = ""
        preveiewsGeantremarque = ""
        preveiewsAzizaremarque = ""
        preveiewsCarrefourremarque = ""

        geantmoddate = ""
        mgmoddate = ""
        monoprixmoddate = ""
        azzizamoddate = ""
        carrefourmoddate = ""

        geantbonusfid = ""
        monoprixbonusfid = ""
        carrefourbonusfid = ""
        azzizabonusfid = ""
        mgbonusfid = ""

    }

    private fun setMagasineInputText() {

        when {
            preveiewsProdame.contains(getString(R.string.carrefour)) -> {
                _binding.carrefourPriceInput.setText(preveiewsCarrefourPrice)
                _binding.carrefourRemarqInput.setText(preveiewsCarrefourremarque)

                _binding.monoprixinfoAddprodLL.visibility = View.GONE
                _binding.mginfoAddprodLL.visibility = View.GONE
                _binding.carrefourinfoAddprodLL.visibility = View.VISIBLE
                _binding.azizainfoAddprodLL.visibility = View.GONE
                _binding.geantinfoAddprodLL.visibility = View.GONE
            }
            preveiewsProdame.contains(getString(R.string.monoprix)) -> {
                _binding.monoprixPriceInput.setText(preveiewsMonoprixPrice)
                _binding.monoprixRemarqInput.setText(preveiewsMonoprixremarque)

                _binding.monoprixinfoAddprodLL.visibility = View.VISIBLE
                _binding.mginfoAddprodLL.visibility = View.GONE
                _binding.carrefourinfoAddprodLL.visibility = View.GONE
                _binding.azizainfoAddprodLL.visibility = View.GONE
                _binding.geantinfoAddprodLL.visibility = View.GONE
            }
            preveiewsProdame.contains(getString(R.string.Géant)) -> {

                _binding.geantPriceInput.setText(preveiewsGeantPrice)
                _binding.geantRemarq.setText(preveiewsGeantremarque)

                _binding.monoprixinfoAddprodLL.visibility = View.GONE
                _binding.mginfoAddprodLL.visibility = View.GONE
                _binding.carrefourinfoAddprodLL.visibility = View.GONE
                _binding.azizainfoAddprodLL.visibility = View.GONE
                _binding.geantinfoAddprodLL.visibility = View.VISIBLE
            }
            preveiewsProdame.contains(getString(R.string.mg)) -> {
                _binding.magasingeneralPriceInput.setText(preveiewsMgPrice)
                _binding.magasingeneralRemarqInput.setText(preveiewsMgremarque)

                _binding.monoprixinfoAddprodLL.visibility = View.GONE
                _binding.mginfoAddprodLL.visibility = View.VISIBLE
                _binding.carrefourinfoAddprodLL.visibility = View.GONE
                _binding.azizainfoAddprodLL.visibility = View.GONE
                _binding.geantinfoAddprodLL.visibility = View.GONE
            }
            preveiewsProdame.contains(getString(R.string.azziza)) -> {
                _binding.azzizaPriceInput.setText(preveiewsAzizaPrice)
                _binding.azzizaRemarqInput.setText(preveiewsAzizaremarque)

                _binding.monoprixinfoAddprodLL.visibility = View.GONE
                _binding.mginfoAddprodLL.visibility = View.GONE
                _binding.carrefourinfoAddprodLL.visibility = View.GONE
                _binding.azizainfoAddprodLL.visibility = View.VISIBLE
                _binding.geantinfoAddprodLL.visibility = View.GONE
            }
            else -> {
                _binding.monoprixinfoAddprodLL.visibility = View.VISIBLE
                _binding.mginfoAddprodLL.visibility = View.VISIBLE
                _binding.carrefourinfoAddprodLL.visibility = View.VISIBLE
                _binding.azizainfoAddprodLL.visibility = View.VISIBLE
                _binding.geantinfoAddprodLL.visibility = View.VISIBLE

                _binding.monoprixPriceInput.setText(preveiewsMonoprixPrice)
                _binding.monoprixRemarqInput.setText(preveiewsMonoprixremarque)
                _binding.magasingeneralPriceInput.setText(preveiewsMgPrice)
                _binding.magasingeneralRemarqInput.setText(preveiewsMgremarque)
                _binding.carrefourPriceInput.setText(preveiewsCarrefourPrice)
                _binding.carrefourRemarqInput.setText(preveiewsCarrefourremarque)
                _binding.azzizaPriceInput.setText(preveiewsAzizaPrice)
                _binding.azzizaRemarqInput.setText(preveiewsAzizaremarque)
                _binding.geantPriceInput.setText(preveiewsGeantPrice)
                _binding.geantRemarq.setText(preveiewsGeantremarque)
            }
        }
    }

    private fun loadProducttomodify() {
        restProductVar()
        displayProgressBar()

        listnerInterstitialAd(requireContext())
        nbrInterstitialAdShowed += 1

        if (nbrInterstitialAdShowed >= 100) nbrInterstitialAdShowed = 0

        preferencesViewModel.onNbrInterstitialAdShowedChanged(nbrInterstitialAdShowed)

        showInterstitialAd(requireActivity(), nbrInterstitialAdShowed)

        resetOnluInputtext()


        productsViewModel.getResponseFromFirestoreUsingCoroutines(searchtype, searchtext)

    }

    private fun clickBtnAddtoShopList(product: ProductShopingRoom) {

        _binding.addtoshopinglistAddFragment.setSafeOnClickListener {
            if (isNumber(
                    removeWord(
                        _binding.ProductIDInput.text.toString(),
                        getString(R.string.codebarre_ID, "")
                    )
                )
            ) {
                shopListRoomViewModel.insertItem(
                    product,
                    getString(R.string.product_saved_in_shoping_list)
                )

            }

        }
    }

    private fun clickBtnDeleteProd(id: String, name: String) {

        _binding.deletebtn.setSafeOnClickListener {

            NetDetect.check { isConnected: Boolean ->
                if ( isConnected) {
                    val idTextInpu = _binding.ProductIDInput.text.toString()

                    if (id != "" && isNumber(
                            removeWord(
                                id,
                                getString(R.string.codebarre_ID, "")
                            )
                        ) && isNumber(removeWord(idTextInpu, getString(R.string.codebarre_ID, "")))
                    ) {
                        val action =
                            AddProductFragmentDirections.actionAddProductFragmentToDialogueFragment(
                                "deleteprodFirestore",
                                id,
                                name
                            )
                        this@AddProductFragment.findNavController()
                            .navigate(action) //   NavHostFragment.findNavController(requireParentFragment()).navigate(action)

                    } else {
                        DynamicToast.makeError(
                            requireContext(),
                            getString(R.string.Pas_produit_pour_effacer),
                            9
                        ).show()
                    }
                } else DynamicToast.makeError(
                    requireContext(),
                    getString(R.string.erreur_conexion, ""),
                    9
                ).show()

            }
        }
    }

    private fun displayProgressBar() {
        _binding.progressBarAddprod.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        _binding.progressBarAddprod.visibility = View.GONE
    }




    private fun searchTextAndSuggestList() {
        _binding.listViewAddprod.visibility = View.GONE
        _binding.nestedScroll.visibility = View.VISIBLE

        //suggest List
        val searchTypeTocompare =
            requireContext().resources.getStringArray(R.array.productTypeArray)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, prod_name_array)

        _binding.listViewAddprod.adapter = adapter

        _binding.listViewAddprod.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as String
                _binding.searchInput.setQuery(selectedItem, false)
                searchtext = selectedItem.capitalizeWords()
                //searchtype="type"  TO PUT IT IF I WANT TO MAKE SEARCH BY TYPE
                searchType(searchtext)

                if (!searchTypeTocompare.contains(searchtext)){
                    loadProducttomodify()
                }
                else warningToast(requireContext(),getString(R.string.Selectionn_un_produit))

                _binding.listViewAddprod.visibility = View.GONE
                _binding.nestedScroll.visibility = View.VISIBLE
                _binding.searchInput.clearFocus()
                requireContext().hideKeyboard(view)
            }

        //search text
        _binding.searchInput.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val cursor =
                    MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                p0?.let {
                    prod_name_array.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(p0, true)) {
                            cursor.addRow(arrayOf(index, suggestion))
                        }
                    }
                }

                searchType(_binding.searchInput.query.toString())
                searchtext =
                    if (searchtype != "id") _binding.searchInput.query.toString().capitalizeWords()
                    else _binding.searchInput.query.toString()//.capitalizeWords()


                if (searchtext != "") {
                    if (!searchTypeTocompare.contains(searchtext)){
                        loadProducttomodify()
                    }
                    else {
                        DynamicToast.makeWarning(
                            requireContext(),
                            requireContext().getString(R.string.Selectionné_un_produit),
                            9
                        ).show()
                        hideProgressBar()
                    }
                    _binding.searchInput.clearFocus()
                } else _binding.searchInput.queryHint = requireContext().getString(R.string.search_hint)



                requireContext().hideKeyboard(requireView())

                _binding.listViewAddprod.visibility = View.GONE
                _binding.nestedScroll.visibility = View.VISIBLE
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //Start filtering the list as user start entering the characters
                if (p0 == "") {
                    _binding.listViewAddprod.visibility = View.GONE
                    _binding.nestedScroll.visibility = View.VISIBLE
                } else {
                    _binding.listViewAddprod.visibility = View.VISIBLE
                    _binding.nestedScroll.visibility = View.GONE
                    adapter.filter.filter(p0)

                }
                return false
            }
        })


    }


    private fun setUserAccess() {
        if (requireContext().getString(R.string.superUserOptions) == requireContext().getString(R.string.true_)) {
            _binding.productImageurlInputLayout.visibility = View.VISIBLE
            _binding.ProductIDInput.setOnClickListener {
                if (isNumber(
                        removeWord(
                            _binding.ProductIDInput.text.toString(), requireContext().getString(
                                R.string.codebarre_ID, ""
                            )
                        )
                    )
                ) {
                    copyToclipord(
                        requireContext(), (removeWord(
                            _binding.ProductIDInput.text.toString(), requireContext().getString(
                                R.string.codebarre_ID, ""
                            )
                        ))
                    )
                }
            }
        } else _binding.productImageurlInputLayout.visibility = View.GONE
    }


    private fun getResultFromScanFragment(value: String) {

        _binding.apply {
            if (isNumber(value)) ProductIDInput.setText(removeLeadingZeroes(value))
            else ProductIDInput.setText(value)
        }

        searchType(value)
        searchtext = value
        loadProducttomodify()
        _binding.searchInput.setQuery(if (isNumber(value)) removeLeadingZeroes(value) else value, false)

    }

    private fun clickScanBtn() {
        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    if (it.value == true) goToBarrecodeScaner()
                }
            }

        _binding.searchByscanAddproduct.setOnClickListener {
            if (checkPermission(requireContext())) goToBarrecodeScaner()
            else requestMultiplePermissions.launch(arrayOf(Manifest.permission.CAMERA))
        }
    }

    private fun clickSaveBtn() {
        _binding.BtnSaveInfo.setSafeOnClickListener {
            if (allowSaveProduct()) saveproduct()

        }
    }

    private fun goToBarrecodeScaner() {
        val action =
            AddProductFragmentDirections.actionAddProductFragmentToBarrecodeScannerFragment()

        NavHostFragment.findNavController(this).navigate(action)

    }

    private fun pricenotdefined(price: String): String {
        return if (price != "99999.0" && price != "" && price.isNotEmpty()) {
            val firstNummonoprixprice: BigDecimal? = try {
                BigDecimal(price)
            } catch (e: NumberFormatException) {
                null
            }
            if (!Functions.factor100(firstNummonoprixprice)) firstNummonoprixprice.toString()
            else String.format("%.3f", firstNummonoprixprice)

        } else ""

    }


    private fun putRemarqueFragmentInfo(magasin: String, discount: String, bonusfidcard: String) {
        fun discountOrNo(): String =
            if (discount == "NO_PROMO") getString(R.string.ajouter_promotions) else discount

        when (magasin) {
            getString(R.string.monoprix) -> {
                monoprixbonusfid = bonusfidcard
                preveiewsMonoprixremarque = discountOrNo()

                _binding.monoprixRemarqInput.setText(discountOrNo())
                _binding.geantRemarq.setText(preveiewsGeantremarque)
                _binding.magasingeneralRemarqInput.setText(preveiewsMgremarque)
                _binding.azzizaRemarqInput.setText(preveiewsAzizaremarque)
                _binding.carrefourRemarqInput.setText(preveiewsCarrefourremarque)
            }
            getString(R.string.mg) -> {
                mgbonusfid = bonusfidcard
                preveiewsMgremarque = discountOrNo()

                _binding.magasingeneralRemarqInput.setText(discountOrNo())
                _binding.monoprixRemarqInput.setText(preveiewsMonoprixremarque)
                _binding.geantRemarq.setText(preveiewsGeantremarque)
                _binding.azzizaRemarqInput.setText(preveiewsAzizaremarque)
                _binding.carrefourRemarqInput.setText(preveiewsCarrefourremarque)
            }
            getString(R.string.azziza) -> {
                azzizabonusfid = bonusfidcard
                preveiewsAzizaremarque = discountOrNo()

                _binding.azzizaRemarqInput.setText(discountOrNo())
                _binding.magasingeneralRemarqInput.setText(preveiewsMgremarque)
                _binding.monoprixRemarqInput.setText(preveiewsMonoprixremarque)
                _binding.geantRemarq.setText(preveiewsGeantremarque)
                _binding.carrefourRemarqInput.setText(preveiewsCarrefourremarque)
            }
            getString(R.string.carrefour) -> {
                carrefourbonusfid = bonusfidcard
                preveiewsCarrefourremarque = discountOrNo()

                _binding.carrefourRemarqInput.setText(discountOrNo())
                _binding.magasingeneralRemarqInput.setText(preveiewsMgremarque)
                _binding.monoprixRemarqInput.setText(preveiewsMonoprixremarque)
                _binding.geantRemarq.setText(preveiewsGeantremarque)
                _binding.azzizaRemarqInput.setText(preveiewsAzizaremarque)
            }
            getString(R.string.Géant) -> {
                geantbonusfid = bonusfidcard
                preveiewsGeantremarque = discountOrNo()

                _binding.geantRemarq.setText(discountOrNo())
                _binding.magasingeneralRemarqInput.setText(preveiewsMgremarque)
                _binding.monoprixRemarqInput.setText(preveiewsMonoprixremarque)
                _binding.azzizaRemarqInput.setText(preveiewsAzizaremarque)
                _binding.carrefourRemarqInput.setText(preveiewsCarrefourremarque)
            }

            "closedresult" -> {
                _binding.geantRemarq.setText(preveiewsGeantremarque)
                _binding.magasingeneralRemarqInput.setText(preveiewsMgremarque)
                _binding.monoprixRemarqInput.setText(preveiewsMonoprixremarque)
                _binding.azzizaRemarqInput.setText(preveiewsAzizaremarque)
                _binding.carrefourRemarqInput.setText(preveiewsCarrefourremarque)
            }
            else -> Toast.makeText(
                context,
                requireContext().getString(R.string.error_message),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun clickSize() {
        _binding.spinnerUnitofmesurAutocomplete.setSafeOnClickListener {
            goToProductTypesDialog("", "size")
        }
    }

    private fun clickTypes() {
        _binding.productTypesInput.setSafeOnClickListener {
            goToProductTypesDialog("", "type")
        }


        _binding.productTypesSubInput.setSafeOnClickListener {
            goToProductTypesDialog(_binding.productTypesInput.text.toString(), "subtype")
        }

        _binding.productTypesSubsubInput.setSafeOnClickListener {
            goToProductTypesDialog(_binding.productTypesSubInput.text.toString(), "subsubtype")
        }
    }

    fun remarquesCliks() {
        _binding.apply {
            monoprixRemarqInput.setSafeOnClickListener {

                goToPriceRemarqFragmenet(
                    getString(R.string.monoprix),
                    _binding.monoprixRemarqInput.text.toString(),
                    monoprixbonusfid
                )

            }

            magasingeneralRemarqInput.setSafeOnClickListener {
                goToPriceRemarqFragmenet(
                    getString(R.string.mg),
                    _binding.magasingeneralRemarqInput.text.toString(),
                    mgbonusfid
                )
            }

            carrefourRemarqInput.setSafeOnClickListener {
                goToPriceRemarqFragmenet(
                    getString(R.string.carrefour),
                    _binding.carrefourRemarqInput.text.toString(),
                    carrefourbonusfid
                )
            }

            azzizaRemarqInput.setSafeOnClickListener {
                goToPriceRemarqFragmenet(
                    getString(R.string.azziza),
                    _binding.azzizaRemarqInput.text.toString(),
                    azzizabonusfid
                )
            }

            geantRemarq.setSafeOnClickListener {
                goToPriceRemarqFragmenet(
                    getString(R.string.Géant),
                    _binding.geantRemarq.text.toString(),
                    geantbonusfid
                )
            }
        }
    }


    private fun goToPriceRemarqFragmenet(magasin: String, discount: String, bonusfidcard: String) {
        val action = AddProductFragmentDirections.actionAddProductFragmentToPriceRemarqueFragment(
            magasin,
            discount,
            bonusfidcard
        )
        this@AddProductFragment.findNavController()
            .navigate(action) //   NavHostFragment.findNavController(this).navigate(action)
    }

    private fun goToProductTypesDialog(topcategorie: String, fromType: String) {
        val action = AddProductFragmentDirections.actionAddProductFragmentToProductTypesDialog(
            topcategorie,
            fromType
        )
        this@AddProductFragment.findNavController()
            .navigate(action) //   NavHostFragment.findNavController(this).navigate(action)
    }
}


