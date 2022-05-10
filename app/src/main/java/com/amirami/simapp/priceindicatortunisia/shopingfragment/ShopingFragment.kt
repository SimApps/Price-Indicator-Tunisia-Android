package com.amirami.simapp.priceindicatortunisia.shopingfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getdateDiffrence
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentShopingBinding
import com.narify.netdetect.NetDetect
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import java.util.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.amirami.simapp.priceindicatortunisia.utils.Functions.hideKeyboard
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.viewmodel.PreferencesViewModel
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Constants
import com.amirami.simapp.priceindicatortunisia.utils.Converters.fromArrayList
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getCurrentDate
import com.amirami.simapp.priceindicatortunisia.utils.Functions.prod_name_array
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.amirami.simapp.priceindicatortunisia.utils.Functions.warningToast
import com.amirami.simapp.priceindicatortunisia.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopingFragment : Fragment(R.layout.fragment_shoping), ProductShopingRV.OnItemClickListener {
    private val shopListRoomViewModel: ShopListRoomViewModel by activityViewModels()
    private val products: MutableList<ProductShopingRoom> = mutableListOf()
    private val preferencesViewModel: PreferencesViewModel by viewModels()
    private val productsViewModel: ProductsViewModel by viewModels()


    private lateinit var adapter: ProductShopingRV


    lateinit var _binding: FragmentShopingBinding

    // lateinit var _bindingProductTiketBinding: ProductTiketBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShopingBinding.bind(view)





        Locale.setDefault(Locale("en", "US"))

        _binding.shoplistInfoTxtVw.text = getString(R.string.Listedecourse)

        setupRecyclerView()
        getShopListProd()


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            shopListRoomViewModel.shopListEvents.collect { event ->
                when (event) {
                    is ShopListRoomViewModel.ShopListEvents.ProdAddToShopMsg -> {
                        if (event.msg == "")  Unit
                        else Functions.dynamicToast(requireContext(), event.msg)
                    }
                    is ShopListRoomViewModel.ShopListEvents.ProdDeleteShopMsg -> {
                        Functions.dynamicToast(requireContext(), event.msg)                    }

                    else -> {}
                }.exhaustive
            }
        }






        _binding.deleteallTxt.setSafeOnClickListener {
            deleteAllShopList()

            //     getAllProductName()
        }

        _binding.btnshowshoppingbill.setSafeOnClickListener {
            countPrices()
        }
    }


    private fun getShopListProd() {
        shopListRoomViewModel.getItems().observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                products.clear()
                products.addAll(list)
                _binding.productTotalTx.text = getString(R.string.NbrProduits, list.size.toString())
                populateRecyclerView(list)

                _binding.btnshowshoppingbill.visibility = View.VISIBLE
                _binding.deleteallTxt.visibility = View.VISIBLE

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                     //   DynamicToast.makeSuccess(requireContext(), list.isNotEmpty().toString() + getdateDiffrence(preferencesViewModel.preferencesFlow.first().last_shopinglist_refresh), 9).show()
                        if (getdateDiffrence(preferencesViewModel.preferencesFlow.first().last_shopinglist_refresh).toInt() >= 1) {
                            NetDetect.check { isConnected: Boolean ->
                                if (isConnected) loadOneProductFromFirestore(list)
                            }
                        }
                    }


                }
            } else {

                _binding.apply {
                    productTotalTx.text = getString(R.string.NbrProduits, "0")
                    rv.visibility = View.GONE
                    emptyShoppingListTxtVw.visibility = View.VISIBLE
                    productTotalTx.visibility = View.INVISIBLE
                    deleteallTxt.visibility = View.INVISIBLE
                    btnshowshoppingbill.visibility = View.INVISIBLE
                }

            }
        }

    }




    private fun loadOneProductFromFirestore(ShopList: List<ProductShopingRoom>) {

        displayProgressBar()
        for (element in ShopList) {
            val isGetProductByIdLiveData = productsViewModel.getProduct(element.productid.toString())
            isGetProductByIdLiveData.observe(viewLifecycleOwner) { dataOrException ->
                val productList = dataOrException.data
                if (productList != null) {
                    if (productList.isNotEmpty()) {
                        Functions.listnerInterstitialAd(requireContext())
                        Functions.showInterstitialAd(requireActivity(),5)

                        lateinit var product: List<ProductShopingRoom>

                        for (i in productList.indices) {
                            productList[i].apply {
                                product = listOf(
                                    ProductShopingRoom(
                                        id!!.toLong(),
                                        date!!,
                                        name!!,
                                        nameArabe!!,
                                        marques!!,
                                        marquesArabe!!,
                                        1.0,
                                        description!!,
                                        descriptionArabe!!,
                                        imageurl!!,
                                        type!!,
                                        typesub!!,
                                        typesubsub!!,
                                        sieze!!,
                                        monoprixprice!!,
                                        monoprixremarq!!,
                                        mgprice!!,
                                        mgremarq!!,
                                        carrefourprice!!,
                                        carrefourremarq!!,
                                        azzizaprice!!,
                                        azzizaremarq!!,
                                        geantprice!!,
                                        geantremarq!!,
                                        monoprixmodifdate!!,
                                        mgmodifdate!!,
                                        carrefourmodifdate!!,
                                        azzizamodifdate!!,
                                        geantmodifdate!!,

                                        monoprixremarqmodifdate,
                                        mgremarqmodifdate,
                                        carrefourremarqmodifdate,
                                        azzizaremarqmodifdate,
                                        geantremarqmodifdate,

                                        monoprixbonusfid!!,
                                        mgbonusfid!!,
                                        carrefourbonusfid!!,
                                        azzizabonusfid!!,
                                        geantbonusfid!!,

                                        monoprixbonusfidmodifdate,
                                        mgbonusfidmodifdate,
                                        carrefourbonusfidmodifdate,
                                        azzizabonusfidmodifdate,
                                        geantbonusfidmodifdate,

                                        fromArrayList(monoprixPriceHistory),
                                        fromArrayList(mgpriceHistory),
                                        fromArrayList(azizaPriceHistory),
                                        fromArrayList(carrefourPriceHistory),
                                        fromArrayList(geantPriceHistory)
                                    )
                                )
                            }
                            shopListRoomViewModel.insertItem(
                                product[i],
                             ""  // getString(R.string.misejourshopinglist)
                            )

                            if (i == productList.size - 1) {
                                hideProgressBar()

                            }
                        }

                        preferencesViewModel.onLastShopingRefreshChanged(getCurrentDate())
                    }
                    else {
                       // hideProgressBar()
                        shopListRoomViewModel.deleteItem(
                            element.productid!!,
                            getString(R.string.product_deleted_in_shoping_list,element.name)
                        )
                       // errorToast(requireContext(), "productList is Empty() " + dataOrException.e.toString())

                    }

                }

                if (dataOrException.e != null) {
                    hideProgressBar()
                    errorToast(requireContext(), dataOrException.e.toString())
                }
            }
        }
    }

    private fun displayProgressBar() {
        _binding.progressBarShoplist.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        _binding.progressBarShoplist.visibility = View.GONE
    }

    override fun onTextEditConfirmClick(task: ProductShopingRoom, quantityEdited: Double) {
        if (quantity(quantityEdited) > 0.001) {
            shopListRoomViewModel.updateQuantity(quantityEdited, task.productid!!)
        } else/* if( quantity(quantityEdited)<=0.001) */ {
            shopListRoomViewModel.deleteItem(
                task.productid!!.toLong(),
                getString(R.string.product_deleted_in_shoping_list,task.name)
            )
        }

        hideKeyboard()

    }


    /* override fun onTextEditChanged(task: ProductShopingRoom,quantityEdited:Double) {
        if (quantity(quantityEdited)>0.001) shopListRoomViewModel.updateQuantity(quantityEdited, task.productid!!)
        else if( quantity(quantityEdited)<=0.001) shopListRoomViewModel.deleteItem(task.productid!!,getString(R.string.product_deleted_in_shoping_list))

     }*/


    override fun onAddQuantityBtnClick(task: ProductShopingRoom) {
        /*  if( quantity(task.quantity!!)<=0.001) {
              shopListRoomViewModel.deleteItem(task.productid!!.toLong(),getString(R.string.product_deleted_in_shoping_list))
          }
          else{*/
        if (task.typesub == Constants.PRODUITS_FRAIS)
            shopListRoomViewModel.updateQuantity(task.quantity!! + 0.100, task.productid!!)
        else shopListRoomViewModel.updateQuantity(task.quantity!! + 1.0, task.productid!!)
        //  }


    }

    override fun onMinusQuantityBtnClick(task: ProductShopingRoom) {
        if (task.typesub == Constants.PRODUITS_FRAIS) {
            if (task.quantity!! - 0.100 <= 0.001)
                deleteOneProdShopList(task.productid!!.toLong(), task.name)
            else shopListRoomViewModel.updateQuantity(task.quantity!! - 0.100, task.productid!!)

        } else if (task.quantity!! - 1 <= 0.001)
            deleteOneProdShopList(task.productid!!.toLong(), task.name)
        else shopListRoomViewModel.updateQuantity(task.quantity!! - 1, task.productid!!)

    }

    override fun onItemClick(task: ProductShopingRoom) {
        shopListRoomViewModel.putProductRoomInfoToBottomSheetNoBill(task, "nobill")
        /*this@ShopingFragment.*/findNavController().navigate(R.id.action_ShopingFragment_to_myBottomSheetDialogFragment)
        //       NavHostFragment.findNavController(this).navigate(R.id.action_ShopingFragment_to_myBottomSheetDialogFragment)
    }


    private fun populateRecyclerView(productShopingRoom: MutableList<ProductShopingRoom>) {
        if (productShopingRoom.isNotEmpty()) {
            adapter.setItems(productShopingRoom)
            //  adapter.submitList(ArrayList(productShopingRoom))
            //  adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        adapter = ProductShopingRV(this)
        _binding.apply {
            rv.layoutManager = LinearLayoutManager(requireContext())
            rv.setHasFixedSize(true)
            rv.adapter = adapter
        }
    }


    fun quantity(quantity: Double): Double {
        return if (quantity > 0.0)
            quantity
        else 0.0
    }

    private fun countPrices() {
        if (products.isNotEmpty()) {

            shopListRoomViewModel.putProductRoomInfoToBottomSheetBill(products)

            this@ShopingFragment.findNavController()
                .navigate(R.id.action_ShopingFragment_to_myBottomSheetDialogFragment) //        NavHostFragment.findNavController(this).navigate(R.id.action_ShopingFragment_to_myBottomSheetDialogFragment)
            // MyBottomSheetDialogFragment().show(parentFragmentManager, "tag")
        } else warningToast(requireContext(), getString(R.string.emptyshopinglist))
    }


    private fun deleteAllShopList() {

        if (products.isNotEmpty()) {
            val action =
                ShopingFragmentDirections.actionShopingFragmentToDialogFragment("deleteAll")
            this@ShopingFragment.findNavController()
                .navigate(action) //    NavHostFragment.findNavController(this@ShopingFragment).navigate(action)
        } else warningToast(requireContext(), getString(R.string.emptyshopinglist))
    }

    private fun deleteOneProdShopList(productid: Long?, name: String) {
        if (products.isNotEmpty()) {
            val action = ShopingFragmentDirections.actionShopingFragmentToDialogFragment(
                "deleteOneItem",
                productid.toString(),
                name
            )
            this@ShopingFragment.findNavController()
                .navigate(action) //   NavHostFragment.findNavController(this@ShopingFragment).navigate(action)
        } else warningToast(requireContext(), getString(R.string.emptyshopinglist))
    }


    fun getAllProductName() {
        displayProgressBar()
        val responseLiveData = productsViewModel.getAllProduct()
        responseLiveData.observe(viewLifecycleOwner) { dataOrException ->
            val productList = dataOrException.data
            if (productList != null) {
                prod_name_array.clear()
                prod_name_array.removeAll(prod_name_array.toSet())

                for (element in productList) {
                    prod_name_array.add(element.name!!)
                    hideProgressBar()
                }
            }

            if (dataOrException.e != null) errorToast(requireContext(), dataOrException.e.toString())
        }

    }

    fun addAllProdNameArrayInFirestore(productName: ArrayList<String>) {
        val addProductNamesArrayInFirestore =
            productsViewModel.addAllProductNamesArrayInFirestore(productName)
        addProductNamesArrayInFirestore.observe(viewLifecycleOwner) {
            //if (it != null)  if (it.data!!)  prod name array updated
            if (it.e != null) {
                //prod bame array not updated
                    warningToast(requireContext(), it.e!!)
            }

        }
    }
}