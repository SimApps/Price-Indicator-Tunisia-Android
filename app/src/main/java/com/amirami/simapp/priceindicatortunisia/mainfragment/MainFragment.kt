package com.amirami.simapp.priceindicatortunisia.mainfragment

import android.Manifest
import android.app.SearchManager
import android.content.ContentValues
import android.content.Context
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentMainBinding
import com.amirami.simapp.priceindicatortunisia.databinding.ProductTiketBinding
import com.amirami.simapp.priceindicatortunisia.model.ProdRoomNamesModel
import com.amirami.simapp.priceindicatortunisia.model.Product
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesRoomViewModel
import com.amirami.simapp.priceindicatortunisia.shoping.ProductShopingRoom
import com.amirami.simapp.priceindicatortunisia.rvadapters.ProductTypesAdapter
import com.amirami.simapp.priceindicatortunisia.shoping.ShopListRoomViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Constants
import com.amirami.simapp.priceindicatortunisia.utils.Converters.fromArrayList
import com.amirami.simapp.priceindicatortunisia.utils.Converters.fromString
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.PriceFormating
import com.amirami.simapp.priceindicatortunisia.utils.Functions.capitalizeWords
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getdateDiffrence
import com.amirami.simapp.priceindicatortunisia.utils.Functions.hideKeyboard
import com.amirami.simapp.priceindicatortunisia.utils.Functions.isNumber
import com.amirami.simapp.priceindicatortunisia.utils.Functions.listnerInterstitialAd
import com.amirami.simapp.priceindicatortunisia.utils.Functions.loadimageurl
import com.amirami.simapp.priceindicatortunisia.utils.Functions.logopalcer
import com.amirami.simapp.priceindicatortunisia.utils.Functions.nbrInterstitialAdShowed
import com.amirami.simapp.priceindicatortunisia.utils.Functions.pricenotdefined
import com.amirami.simapp.priceindicatortunisia.utils.Functions.prod_name_array
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeLeadingZeroes
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeWordWithReplace
import com.amirami.simapp.priceindicatortunisia.utils.Functions.reopendialogue
import com.amirami.simapp.priceindicatortunisia.utils.Functions.searchType
import com.amirami.simapp.priceindicatortunisia.utils.Functions.searchtext
import com.amirami.simapp.priceindicatortunisia.utils.Functions.searchtype
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.amirami.simapp.priceindicatortunisia.utils.Functions.shortformateDate
import com.amirami.simapp.priceindicatortunisia.utils.Functions.showInterstitialAd
import com.amirami.simapp.priceindicatortunisia.utils.Functions.showRestOfString
import com.amirami.simapp.priceindicatortunisia.utils.Functions.showStringifNotEmpty
import com.amirami.simapp.priceindicatortunisia.utils.Functions.warningToast
import com.amirami.simapp.priceindicatortunisia.utils.exhaustive
import com.amirami.simapp.priceindicatortunisia.viewmodel.*
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.material.textfield.TextInputLayout
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.firestore.FirebaseFirestore
import com.narify.netdetect.NetDetect
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main),
    ProductTypesAdapter.OnItemClickListener/*, ProductsAdapter.OnItemClickListener*/ {
    private val prodNamesRoomViewModel: ProdNamesRoomViewModel by viewModels()
    private val preferencesViewModel: PreferencesViewModel by viewModels()
    private val productsnamelist: MutableList<ArrayList<String>> = mutableListOf()
    private val productsViewModel: ProductsViewModel by viewModels()
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()
    private val shopListRoomViewModel: ShopListRoomViewModel by activityViewModels()


    lateinit var _binding: FragmentMainBinding
  //  lateinit var _bottomsheetbinding: ProductTiketBinding

    private lateinit var productFeauteredTypesAdapter: ProductTypesAdapter

    lateinit   var appUpdateManager: AppUpdateManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)


        checkUpdate()



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productInfoViewModel.productInfoEvents.collect { event ->
                when (event) {
                    is ProductInfoViewModel.ProductEvents.PutProductSubSubTypesInfo -> {
                        run {
                            searchtext = event.subsubtype
                            searchtype = "typesubsub"
                            recyclerview()
                            _binding.mainTxVwInfo.visibility = View.GONE
                        }
                    }


                    else -> {}
                }.exhaustive
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            shopListRoomViewModel.shopListEvents.collect { event ->
                when (event) {
                    is ShopListRoomViewModel.ShopListEvents.ProdAddToShopMsg -> DynamicToast.makeSuccess(
                        requireContext(),
                        event.msg,
                        9
                    ).show()
                    else -> {}
                }.exhaustive
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (getdateDiffrence(preferencesViewModel.preferencesFlow.first().date_list_name_refresh).toInt() >= 1) {
                    NetDetect.check { isConnected: Boolean ->
                        if (isConnected) {
                            _binding.mainTxVwInfo.text =
                                getString(R.string.Mise_a_jour_liste_des_nom)
                            getProdListNamesFromFirestore()
                        } else errorToast( requireContext(),
                            getString(R.string.erreur_conexion, ""))
                    }
                } else {
                    val mainTxvwArray = resources.getStringArray(R.array.main_txvw_array)
                    val random = (mainTxvwArray.indices).random()
                    _binding.mainTxVwInfo.text = removeWordWithReplace(
                        mainTxvwArray[random],
                        "%s",
                        prod_name_array.size.toString()
                    )
                }
            }
        }

        subscribeObserversRoomProductNames()

        setUpProductFeauteredTypesRv()


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productInfoViewModel.barecodeScanEvents.collect { event ->
                when (event) {
                    is ProductInfoViewModel.BareCodeScanerEvents.PutBareCodeScanerInfo -> {
                        run {
                            //val searchProductTxv = requireView().findViewById<androidx.appcompat.widget.SearchView>(R.id.search_product_txv)
                            if (isNumber(event.barecode.value!!.toString())) {
                                _binding.searchProductTxv.setQuery(
                                    removeLeadingZeroes(event.barecode.value!!.toString()),
                                    true
                                )
                            } else  _binding.searchProductTxv.setQuery(
                                event.barecode.value!!.toString(),
                                true
                            )


                            searchquerry()
                            _binding.searchProductTxv.setQuery(
                                _binding.searchProductTxv.query,
                                true
                            )
                        }

                    }
                }.exhaustive
            }
        }



        _binding.scanFloatingActionBtn.setSafeOnClickListener {
            if (Functions.checkPermission(requireContext()))
                goToBarrecodeScaner()
            else requestMultiplePermissions.launch(arrayOf(Manifest.permission.CAMERA))
        }
    }


    private val listener: InstallStateUpdatedListener = InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            Log.d(ContentValues.TAG, "An update has been downloaded")
            appUpdateManager.completeUpdate()

            warningToast(requireContext(),"An update has been downloaded")

        }
    }


    private fun checkUpdate(){
        appUpdateManager = AppUpdateManagerFactory.create(requireContext())

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            //   DynamicToast.makeWarning(requireContext(),  appUpdateInfo.updateAvailability().toString(), 9).show()
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                //   && (appUpdateInfo.clientVersionStalenessDays() ?: -1) >=0// DAYS_FOR_FLEXIBLE_UPDATE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.

                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    // AppUpdateType.IMMEDIATE,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    requireActivity() /* Functions.unwrap(context)*/,
                    // Include a request code to later monitor this update request.
                    1)

                appUpdateManager.registerListener(listener)
            }
            else {
                appUpdateManager.unregisterListener(listener)
                Log.d(ContentValues.TAG, "No Update available")
            }
        }
    }


/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode != Activity.RESULT_CANCELED) {
                //  Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
                DynamicToast.makeError(requireContext(), resultCode.toString(), 9).show()
            }
            else if(resultCode != RESULT_IN_APP_UPDATE_FAILED){
                DynamicToast.makeError(requireContext(), resultCode.toString(), 9).show()
            }
        }
    }
*/





    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                //Log.e("DEBUG", "${it.key} = ${it.value}")
                if (it.value == true) {
                    goToBarrecodeScaner()
                }
            }
        }

    private fun searchquerry() {
        _binding.listView.visibility = View.GONE
        _binding.productsrv.visibility = View.VISIBLE
        _binding.favCategorieRv.visibility = View.VISIBLE

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, /*search_type*/
            prod_name_array
        )
        _binding.listView.adapter = adapter

        _binding.listView.onItemClickListener =

            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as String
                _binding.searchProductTxv.setQuery(selectedItem, false)
                searchtext = selectedItem.capitalizeWords()

                searchType(searchtext)

                reopendialogue = true

                recyclerview()

                _binding.listView.visibility = View.GONE
                _binding.favCategorieRv.visibility = View.VISIBLE
                _binding.productsrv.visibility = View.VISIBLE
                _binding.mainTxVwInfo.visibility = View.GONE
                // _binding.scanFloatingActionBtn.visibility = View.GONE
                _binding.searchProductTxv.clearFocus()
                requireContext().hideKeyboard(view)

            }

        _binding.searchProductTxv.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                val cursor =
                    MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                p0?.let {
                     prod_name_array.forEachIndexed { index, suggestion ->
                    if (suggestion.contains(p0, true)) cursor.addRow(arrayOf(index, suggestion))
                }
                }


                searchType(_binding.searchProductTxv.query.toString())
                searchtext = if (searchtype != "id") _binding.searchProductTxv.query.toString()
                    .capitalizeWords()
                else removeLeadingZeroes(_binding.searchProductTxv.query.toString())

                if (searchtext != "") {
                    reopendialogue = true
                    recyclerview()
                    _binding.searchProductTxv.clearFocus()
                } else _binding.searchProductTxv.queryHint = getString(R.string.search_hint)


                requireContext().hideKeyboard(requireView())

                _binding.listView.visibility = View.GONE
                _binding.favCategorieRv.visibility = View.VISIBLE
                _binding.productsrv.visibility = View.VISIBLE
                _binding.mainTxVwInfo.visibility = View.GONE
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //Start filtering the list as user start entering the characters
                if (p0 == "") {
                    //  _binding.scanFloatingActionBtn.visibility = View.VISIBLE
                    _binding.mainTxVwInfo.visibility = View.GONE
                    _binding.listView.visibility = View.GONE
                    _binding.favCategorieRv.visibility = View.VISIBLE
                    _binding.productsrv.visibility = View.VISIBLE
                } else {
                    //  _binding.scanFloatingActionBtn.visibility = View.GONE
                    _binding.mainTxVwInfo.visibility = View.GONE
                    _binding.listView.visibility = View.VISIBLE
                    _binding.favCategorieRv.visibility = View.INVISIBLE
                    _binding.productsrv.visibility = View.GONE
                    adapter.filter.filter(p0)
                }
                return false
            }
        })

    }

    fun recyclerview() {
        listnerInterstitialAd(requireContext())
        nbrInterstitialAdShowed += 1
        showInterstitialAd(requireActivity(), nbrInterstitialAdShowed)
        if (nbrInterstitialAdShowed >= 100) nbrInterstitialAdShowed = 0

        preferencesViewModel.onNbrInterstitialAdShowedChanged(nbrInterstitialAdShowed)
//        _binding.scanFloatingActionBtn.visibility = View.GONE

        val db = FirebaseFirestore.getInstance()
        //  val query =db.collection("Tunisia").whereGreaterThanOrEqualTo(searchtype, searchtext).whereLessThanOrEqualTo(searchtype, "$searchtext\uF8FF")

        // val baseQuery: Query = db.collection.orderBy("value", Query.Direction.ASCENDING)
        val query = db.collection(Constants.PRODUCTS_COLLECTION)
            .whereEqualTo(searchtype, searchtext)
            //.orderBy("name")
           // .orderBy("name", Query.Direction.ASCENDING)
        //  .limit(5)

           val config = PagingConfig(5, 1, false)

        val options = FirestorePagingOptions.Builder<Product>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(query, config, Product::class.java)
            .build()

        /*  val options = FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java)
              .setLifecycleOwner(viewLifecycleOwner/*requireContext() as LifecycleOwner*/)
              .build()*/
        val adapter = object : FirestorePagingAdapter<Product, Productviewholder>(options) {


            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): Productviewholder {

                /*  val view = LayoutInflater.from(requireContext())
                      .inflate(R.layout.product_tiket, parent, false)
                  _bottomsheetbinding = ProductTiketBinding.bind(view)
                  return Productviewholder(ProductTiketBinding.bind(view))*/
                 return Productviewholder(
                    ProductTiketBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

                override fun onBindViewHolder(
                    holder: Productviewholder,
                    position: Int,
                    model: Product
                ) {

                    val sortedValues = mutableListOf(
                        BigDecimal(PriceFormating(model.monoprixprice!!)) to getString(R.string.monoprix),
                        BigDecimal(PriceFormating(model.mgprice!!)) to requireContext().getString(R.string.mg),
                        BigDecimal(PriceFormating(model.carrefourprice!!)) to requireContext().getString(
                            R.string.carrefour
                        ),
                        BigDecimal(PriceFormating(model.azzizaprice!!)) to requireContext().getString(
                            R.string.azziza
                        ),
                        BigDecimal(PriceFormating(model.geantprice!!)) to requireContext().getString(
                            R.string.Géant
                        )
                    )
                    sortedValues.sortBy { it.first }

                  // _bottomsheetbinding.apply {
                    val nameTxVw =  holder.itemView.findViewById(R.id.name_txVw) as TextView
                    val addtoshoplistAndPlusTxVw =  holder.itemView.findViewById(R.id.addtoshoplistAnd_plus_txVw) as ImageView
                    val bestpriceAndQuantityTxVwLayout =  holder.itemView.findViewById(R.id.bestpriceAnd_quantity_txVw_layout) as TextInputLayout
                    val logoAndMinusImVw =  holder.itemView.findViewById(R.id.logoAnd_minus_imVw) as ImageView
                    val imageview =  holder.itemView.findViewById(R.id.imageview) as ImageView
                    val linearLayoutHolder =  holder.itemView.findViewById(R.id.linearLayoutHolder) as ConstraintLayout


                    nameTxVw.text=model.name
                        addtoshoplistAndPlusTxVw.setImageResource(R.drawable.ic_add_to_cart)
                        bestpriceAndQuantityTxVwLayout.visibility = View.GONE
                        logoAndMinusImVw.visibility = View.GONE


                        loadimageurl(requireContext(), model.imageurl!!, imageview)

                        showProductWithoutBill(model, sortedValues,holder.itemView)

                        val productRoom = ProductShopingRoom(

                            model.id!!.toLong(),
                            model.date!!,
                            model.name!!,
                            model.nameArabe!!,
                            model.marques!!,
                            model.marquesArabe!!,
                            1.0,
                            model.description!!,
                            model.descriptionArabe!!,
                            model.imageurl!!,
                            model.type!!,
                            model.typesub!!,
                            model.typesubsub!!,
                            model.sieze!!,
                            model.monoprixprice!!,
                            model.monoprixremarq!!,
                            model.mgprice!!,
                            model.mgremarq!!,
                            model.carrefourprice!!,
                            model.carrefourremarq!!,
                            model.azzizaprice!!,
                            model.azzizaremarq!!,
                            model.geantprice!!,
                            model.geantremarq!!,

                            model.monoprixremarqmodifdate,
                            model.mgremarqmodifdate,
                            model.carrefourremarqmodifdate,
                            model.azzizaremarqmodifdate,
                            model.geantremarqmodifdate,

                            model.monoprixmodifdate!!,
                            model.mgmodifdate!!,
                            model.carrefourmodifdate!!,
                            model.azzizamodifdate!!,
                            model.geantmodifdate!!,
                            model.monoprixbonusfid!!,
                            model.mgbonusfid!!,
                            model.carrefourbonusfid!!,
                            model.azzizabonusfid!!,
                            model.geantbonusfid!!,

                            model.monoprixbonusfidmodifdate,
                            model.mgbonusfidmodifdate,
                            model.carrefourbonusfidmodifdate,
                            model.azzizabonusfidmodifdate,
                            model.geantbonusfidmodifdate,


                            fromArrayList(model.monoprixPriceHistory),
                            fromArrayList(model.mgpriceHistory),
                            fromArrayList(model.azizaPriceHistory),
                            fromArrayList(model.carrefourPriceHistory),
                            fromArrayList(model.geantPriceHistory)
                        )
                        linearLayoutHolder.setSafeOnClickListener {
                            shopListRoomViewModel.putProductRoomInfoToBottomSheetNoBill(
                                productRoom,
                                "productInfo"
                            )
                            /*  this@MainFragment.*/findNavController().navigate(R.id.action_MainFragment_to_myBottomSheetDialogFragment)

                            /*   val action = MainFragmentDirections.actionMainFragmentToMyBottomSheetDialogFragment()
                               requireParentFragment().findNavController().navigate(action)*/
                        }
                        addtoshoplistAndPlusTxVw.setSafeOnClickListener {
                            shopListRoomViewModel.insertItem(
                                productRoom,
                                getString(R.string.product_saved_in_shoping_list)
                            )

                        }

                   // }


                }


/*
                override fun onError(e: FirebaseFirestoreException) {
                    DynamicToast.makeWarning(requireContext(), "rv error : $e", 9).show()
                    super.onError(e)
                }


                override fun onDataChanged() {
                    if (reopendialogue) {

                        if (isAdded) {
                            reopendialogue = false

                            if (itemCount == 0 && isNumber(_binding.searchProductTxv.query.toString()) && searchtext != "") {
                                val action = MainFragmentDirections.actionMainFragmentToDialogFragment("add", removeLeadingZeroes(_binding.searchProductTxv.query.toString()))
                                this@MainFragment.findNavController().navigate(action) //          NavHostFragment.findNavController(requireParentFragment()).navigate(action)
                            }
                            else if (itemCount == 0 && !isNumber(_binding.searchProductTxv.query.toString()) && _binding.searchProductTxv.query.toString() !in prod_name_array) {
                                DynamicToast.makeWarning(requireContext(), "${_binding.searchProductTxv.query} n'est pas disponible dans notre base de donnée!", 9).show()
                            }
                        }

                    }
                    hideProgressBar()
                    super.onDataChanged()
                }
*/
            }
        _binding.productsrv.adapter = adapter
        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.VERTICAL, false)
        // rvUsers.layoutManager = LinearLayoutManager(requireContext())  cause a strange bugs !!!

        _binding.productsrv.layoutManager = mLayoutManager


        // Activities can use lifecycleScope directly, but Fragments should instead use
// viewLifecycleOwner.lifecycleScope.
        viewLifecycleOwner.lifecycleScope.launch {

            adapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Error -> {
                        // The initial load failed. Call the retry() method
                        // in order to retry the load operation.
                        // ...
                        hideProgressBar()
                        //  DynamicToast.makeWarning(requireContext(), "refresh LoadState.Error", 9).show()

                    }
                    is LoadState.Loading -> {
                        // The initial Load has begun
                        // ...
                        displayProgressBar()
                        //DynamicToast.makeWarning(requireContext(), "refresh LoadState.Loading", 9).show()
                    }
                    is LoadState.NotLoading -> {
                        hideProgressBar()
                        if (adapter.itemCount == 10) {
                            showInterstitialAd(requireActivity(), 5)
                        }

                        if (adapter.itemCount == 0) {
                            NetDetect.check { isConnected: Boolean ->
                                if (isConnected) {
                                    if (reopendialogue && isAdded) {
                                        reopendialogue = false

                                        if (isNumber(_binding.searchProductTxv.query.toString()) && searchtext != "") {
                                            val action = MainFragmentDirections.actionMainFragmentToDialogFragment(
                                                    "add",
                                                    removeLeadingZeroes(_binding.searchProductTxv.query.toString())
                                                )
                                            this@MainFragment.findNavController().navigate(action)
                                        //          NavHostFragment.findNavController(requireParentFragment()).navigate(action)
                                        } else if (!isNumber(_binding.searchProductTxv.query.toString())
                                            && _binding.searchProductTxv.query.toString() !in prod_name_array)
                                                warningToast(requireContext(), "$searchtext n'est pas disponible dans notre base de donnée!")
                                    }
                                } else errorToast(requireContext(), getString(R.string.erreurconexion))
                            }

                        }
                    }
                }

                when (loadStates.append) {
                    is LoadState.Error -> {
                        // The additional load failed. Call the retry() method
                        // in order to retry the load operation.
                        // ...
                        hideProgressBar()
                    }
                    is LoadState.Loading -> {
                        // The adapter has started to load an additional page
                        // ...
                        displayProgressBar()
                        //  DynamicToast.makeWarning(requireContext(), "append LoadState.Loading", 9).show()
                    }
                    is LoadState.NotLoading -> {
                        if (loadStates.append.endOfPaginationReached) {
                            hideProgressBar()
                            // The adapter has finished loading all of the data set
                            // ...
                            //     DynamicToast.makeWarning(requireContext(), "append LoadState.endOfPaginationReached", 9).show()
                        }
                        if (loadStates.refresh is LoadState.NotLoading) {
                            // The previous load (either initial or additional) completed
                            // ...
                            // DynamicToast.makeWarning(requireContext(), "append LoadState.NotLoading", 9).show()
                            hideProgressBar()

                        }
                    }
                }
            }
        }

    }

    class Productviewholder(itemView: ProductTiketBinding) : RecyclerView.ViewHolder(itemView.root)


    class LinearLayoutManagerWrapper//constructor(context: Context?) : super(context) {}
        (context: Context?, orientation: Int, reverseLayout: Boolean) : LinearLayoutManager(
        context,
        orientation,
        reverseLayout
    ) {

        /* constructor(
             context: Context?,
             attrs: AttributeSet?,
             defStyleAttr: Int,
             defStyleRes: Int
         ) : super(context, attrs, defStyleAttr, defStyleRes) {
         }*/

        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }


    private fun goToBarrecodeScaner() {
        val action = MainFragmentDirections.actionMainFragmentToBarrecodeScannerFragment()
        this@MainFragment.findNavController()
            .navigate(action) //      NavHostFragment.findNavController(this).navigate(action)
    }


    private fun getProdListNamesFromFirestore() {
        displayProgressBar()
        productsViewModel.productNameListLiveData.observe(viewLifecycleOwner) { DataOrExceptionProdNames ->
            val productList = DataOrExceptionProdNames.data
            if (productList != null && productList.isNotEmpty() && productList[0].size > 0) {
                preferencesViewModel.onDateListNameRefreshChanged(Functions.getCurrentDate())

                prod_name_array.clear()
                prod_name_array.removeAll(prod_name_array.toSet())

                prodNamesRoomViewModel.deleteAll()

                if (productsnamelist.isNotEmpty()) productList.clear()

                productsnamelist.addAll(productList)

                prod_name_array = productsnamelist[0]

                prodNamesRoomViewModel.insertItem(
                    ProdRoomNamesModel(
                        id = 1,
                        fromArrayList(productList[0])
                    )
                )


                _binding.mainTxVwInfo.text = resources.getStringArray(R.array.main_txvw_array)[0]

                //searchquerry()

                hideProgressBar()
            }

            if (DataOrExceptionProdNames.e != null) {
                hideProgressBar()
                errorToast( requireContext(),
                    DataOrExceptionProdNames.e.toString() + requireContext().getString(R.string.Pas_nom_produit))

            }
        }
    }


    private fun displayProgressBar() {
        _binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        _binding.progressBar.visibility = View.GONE
    }


    private fun subscribeObserversRoomProductNames() {
        prodNamesRoomViewModel.getItems().observe(viewLifecycleOwner) { list ->
            if (prod_name_array.isEmpty() && list.isNotEmpty()) {
                prod_name_array = fromString(list.map { it.name }[0]!!)
            }
            searchquerry()
        }

    }


    fun showProductWithoutBill(
        product: Product,
        sortedValues: MutableList<Pair<BigDecimal, String>>,
        holder: View
    ) {
        val priceTxVw =  holder.findViewById(R.id.priceTxVw) as TextView
        val magasinImage =  holder.findViewById(R.id.magasin_image) as ImageView

        when {
            product.name!!.contains(getString(R.string.carrefour)) -> {
               priceTxVw.text = pricenotdefined(
                    requireContext(),
                    product.carrefourprice!!,
                    true
                )
                magasinImage.setImageResource(R.drawable.logo_carrefour)
            }
            product.name!!.contains(getString(R.string.monoprix)) -> {
                priceTxVw.text = pricenotdefined(
                    requireContext(),
                    product.monoprixprice!!,
                    true
                )
                magasinImage.setImageResource(R.drawable.ic_monoprix_logo)
            }
            product.name!!.contains(getString(R.string.Géant)) -> {
                priceTxVw.text = pricenotdefined(
                    requireContext(),
                    product.geantprice!!,
                    true
                )
                magasinImage.setImageResource(R.drawable.geantlogo)
            }
            product.name!!.contains(getString(R.string.mg)) -> {
                 priceTxVw.text = pricenotdefined(
                    requireContext(),
                    product.mgprice!!,
                    true
                )
                magasinImage.setImageResource(R.drawable.mglogo)
            }
            product.name!!.contains(getString(R.string.azziza)) -> {
                priceTxVw.text = pricenotdefined(
                    requireContext(),
                    product.azzizaprice!!,
                    true
                )
                magasinImage.setImageResource(R.drawable.azizalogo)
            }
            else -> {
                /*
                     _bottomsheetbinding.apply {
                         secondpriceLL.visibility = View.VISIBLE
                         thirdpriceLL.visibility = View.VISIBLE
                         fourthpriceLL.visibility = View.VISIBLE
                         fifthpriceLL.visibility = View.VISIBLE
                     }
     */
                priceTxVw.text = pricenotdefined(
                    requireContext(),
                    sortedValues[0].first.toString(),
                    true
                ) + showRestOfString(priceDate(sortedValues[0].second, product))

                /*     _bottomsheetbinding.prodSecondpriceSpanel.text = pricenotdefined(
                         requireContext(),
                         sortedValues[1].first.toString(),
                         true
                     ) + remarqTowhom(sortedValues[1].second, product)

                     _bottomsheetbinding.prodThirdpriceSpanel.text = pricenotdefined(
                         requireContext(),
                         sortedValues[2].first.toString(),
                         true
                     ) + remarqTowhom(sortedValues[2].second, product)

                     _bottomsheetbinding.prodForthpriceSpanel.text = pricenotdefined(
                         requireContext(),
                         sortedValues[3].first.toString(),
                         true
                     ) + remarqTowhom(sortedValues[3].second, product)

                     _bottomsheetbinding.prodFifthpriceSpanel.text = pricenotdefined(
                         requireContext(),
                         sortedValues[4].first.toString(),
                         true
                     ) + remarqTowhom(sortedValues[4].second, product)
     */

               magasinImage.setImageResource(
                    logopalcer(
                        requireContext(),
                        sortedValues[0].second
                    )
                )
                // loadimageInt(logopalcer(requireContext(), sortedValues[0].second), _bottomsheetbinding.magasinImage)
                /*    loadimageInt(logopalcer(requireContext(), sortedValues[1].second), _bottomsheetbinding.secondpriceImagevwPanel)
                    loadimageInt(logopalcer(requireContext(), sortedValues[2].second), _bottomsheetbinding.thirdpriceImagevwPanel)
                    loadimageInt(logopalcer(requireContext(), sortedValues[3].second), _bottomsheetbinding.fourthpriceImagevwPanel)
                    loadimageInt(logopalcer(requireContext(), sortedValues[4].second), _bottomsheetbinding.fifthpriceImagevwPanel)*/

                //   }
                // }
            }


        }


    }

    fun priceDate(magasin: String, product: Product): String {
        var remarque = ""


        when (magasin) {
            getString(R.string.monoprix) -> {
                remarque =
                        /* showStringifNotEmpty(product.monoprixremarq!!, "") + showStringifNotEmpty(
                             pricenotdefined(requireContext(), product.monoprixbonusfid!!, false), "fidbonustring") + */
                    showStringifNotEmpty(shortformateDate(product.monoprixmodifdate!!), "")
            }
            getString(R.string.mg) -> {
                remarque =/* showStringifNotEmpty(product.mgremarq!!, "") + showStringifNotEmpty(
                    pricenotdefined(requireContext(), product.mgbonusfid!!, false),
                    "fidbonustring"
                ) + */showStringifNotEmpty(shortformateDate(product.mgmodifdate!!), "")
            }
            getString(R.string.carrefour) -> {
                remarque =
                        /* showStringifNotEmpty(product.carrefourremarq!!, "") + showStringifNotEmpty(
                             pricenotdefined(requireContext(), product.carrefourbonusfid!!, false),
                             "fidbonustring"
                         ) +*/
                    showStringifNotEmpty(shortformateDate(product.carrefourmodifdate!!), "")
            }
            getString(R.string.azziza) -> {
                remarque = /*showStringifNotEmpty(product.azzizaremarq!!, "") + showStringifNotEmpty(
                    pricenotdefined(requireContext(), product.azzizabonusfid!!, false),
                    "fidbonustring"
                ) + */showStringifNotEmpty(shortformateDate(product.azzizamodifdate!!), "")
            }
            getString(R.string.Géant) -> {
                remarque = /*showStringifNotEmpty(product.geantremarq!!, "") + showStringifNotEmpty(
                    pricenotdefined(requireContext(), product.geantbonusfid!!, false),
                    "fidbonustring"
                ) + */showStringifNotEmpty(shortformateDate(product.geantmodifdate!!), "")
            }
        }

        return remarque
    }


    private fun setUpProductFeauteredTypesRv() {
        /* val uri = "@array/" + "subTypeArray"+ topcategorie.replace("-".toRegex(), "")
         val arrayResource = resources.getIdentifier(uri, null, requireContext().packageName)*/
        productFeauteredTypesAdapter =
            ProductTypesAdapter(resources.getStringArray(R.array.productFeauteredTypeArray), this)

        _binding.favCategorieRv.apply {
            adapter = productFeauteredTypesAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)

            setHasFixedSize(true)
        }
    }

    override fun onItemTagsClick(item: String) {

        if (item == "Tous les Catégories") goToProductTypesDialog()
        else {
            searchtext = item
            searchtype = "typesubsub"
            recyclerview()

            _binding.mainTxVwInfo.visibility = View.GONE
        }

    }

    private fun goToProductTypesDialog() {
        val action =
            MainFragmentDirections.actionMainFragmentToProductTypesDialog("", "typeFromMain")
        this@MainFragment.findNavController()
            .navigate(action) //   NavHostFragment.findNavController(this).navigate(action)
    }


}


