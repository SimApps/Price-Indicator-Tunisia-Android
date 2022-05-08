package com.amirami.simapp.priceindicatortunisia.fidcard

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.amirami.simapp.priceindicatortunisia.utils.Functions.checkPermission
import com.amirami.simapp.priceindicatortunisia.utils.Functions.unwrap
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductInfoViewModel
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentFidCardBinding
import com.amirami.simapp.priceindicatortunisia.utils.Functions.dynamicToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.amirami.simapp.priceindicatortunisia.utils.Functions.warningToast
import com.amirami.simapp.priceindicatortunisia.utils.exhaustive
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FidCardFragment : Fragment(R.layout.fragment_fid_card), FidCardAdapter.FidCardItemListener {
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()
    private val fidCardRoomViewModel: FidCardRoomViewModel by activityViewModels()
    private val barecode: MutableList<Barecode> = mutableListOf()

    private lateinit var adapter: FidCardAdapter


    private lateinit var binding: FragmentFidCardBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFidCardBinding.bind(view)

        nativeRate()
        addFidCardBtn()


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            fidCardRoomViewModel.fidCardEvents.collect { event ->
                when (event) {
                    is FidCardRoomViewModel.FidCardEvents.FidCardDeleteMsg -> {
                        dynamicToast(requireContext(), event.msg)
                        if (barecode.size == 1) {
                            binding.rvFidCard.visibility = View.GONE
                            binding.emptyFidcardList.visibility = View.VISIBLE
                        } else {
                            binding.emptyFidcardList.visibility = View.GONE
                            populateRecyclerView(barecode)
                        }

                    }
                }.exhaustive
            }
        }


        setupRecyclerView()

        fidCardRoomViewModel.getItems().observe(viewLifecycleOwner) { list ->
            //    Log.d("MainFragment","ID ${list.map { it.id }}, Name ${list.map { it.name }}")
            if (list.isNotEmpty()) {

                binding.emptyFidcardList.visibility = View.GONE
                barecode.clear()
                barecode.addAll(list)
                populateRecyclerView(list)
            } else {
                binding.emptyFidcardList.visibility = View.VISIBLE
                //empty
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productInfoViewModel.barecodeScanEvents.collect { event ->
                when (event) {
                    is ProductInfoViewModel.BareCodeScanerEvents.PutBareCodeScanerInfo -> {
                        run {
                            var name=""
                            var id = ""
                             fun isInfidCardDb(barcode: String): Boolean {
                                var isInDB = false
                                for (i in 0 until  barecode.size){
                                    if(!barecode.isNullOrEmpty()){
                                        if (barecode[i].value.toString() == barcode) {
                                            isInDB = true
                                            id = barecode[i].id.toString()
                                            name=barecode[i].name.toString()
                                        }
                                    }


                                }
                                return isInDB
                            }
                            if (isInfidCardDb(event.barecode.value!!.toString())) {

                                fidcardToDialogFragment(
                                    "editFidCard",
                                    event.barecode.value.toString(),
                                    event.barecode.barecodetype!!,
                                    name,
                                    id
                                )
                            }
                            else
                                fidcardToDialogFragment(
                                    "addFidCard",
                                    event.barecode.value!!.toString(),
                                    event.barecode.barecodetype!!,
                                    "",
                                    ""
                                )
                        }

                    }


                }.exhaustive
            }
        }


    }



    private fun goToBarrecodeScaner() {
        val action = FidCardFragmentDirections.actionFidCardFragmentToBarrecodeScannerFragment()
        //NavHostFragment.findNavController(this@FidCardFragment).navigate(action)

        this@FidCardFragment.findNavController().navigate(action)
    }

    private fun populateRecyclerView(fidcardRoom: MutableList<Barecode>) {
        if (fidcardRoom.isNotEmpty()) {
            adapter.setItems(fidcardRoom)
            //  adapter.submitList(ArrayList(productShopingRoom))
            //  adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        adapter = FidCardAdapter(this)
        binding.apply {
            rvFidCard.layoutManager = LinearLayoutManager(requireContext())
            rvFidCard.setHasFixedSize(true)
            rvFidCard.adapter = adapter
        }
    }


    private fun nativeRate() {
        val manager = ReviewManagerFactory.create(requireContext())

        val request = manager.requestReviewFlow()

        request.addOnCompleteListener { task ->

            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result

                val flow = manager.launchReviewFlow(unwrap(requireContext()), reviewInfo)
                flow.addOnCompleteListener {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }
            }/* else {
                //   DynamicToast.makeSuccess(requireContext(),"eere",9)
                // There was some problem, log or handle the error code.
                //   @ReviewErrorCode val reviewErrorCode = (task.exception as TaskException).errorCode
            }*/
        }

    }

    override fun onClickedFidCardDelete(barecode: Barecode) {
        deleteOneFidCard(barecode.id!!.toString(), barecode.name!!)
    }

    override fun onClickedFidCardEdit(barecode: Barecode) {
        fidcardToDialogFragment(
            "editFidCard",
            barecode.value!!.toString(),
            barecode.barecodetype!!,
            barecode.name!!,
            barecode.id!!.toString()
        )
    }


    private fun deleteOneFidCard(fidcardid: String, name: String) {
        if (barecode.isNotEmpty()) fidcardToDialogFragment("deleteFidCard", fidcardid, "", name, "")
        else warningToast(requireContext(), getString(R.string.emptyshopinglist))
    }

    override fun onClickedFidCardItem(barecode: Barecode) {
        val action = FidCardFragmentDirections.actionFidCardFragmentToImageFidCarDialogFragment(
            barecode.name!!,
            barecode.value!!.toString(),
            barecode.barecodetype!!
        )
        this@FidCardFragment.findNavController().navigate(action)
        //    NavHostFragment.findNavController(this).navigate(action)
    }

    private fun fidcardToDialogFragment(
        actions: String,
        fidcardid: String?,
        barecodetype: String,
        fidCardname: String,
        id: String
    ) {
        val action = FidCardFragmentDirections.actionFidCardFragmentToDialogFragment(
            actions,
            fidcardid!!,
            barecodetype,
            fidCardname,
            id
        )
        //  NavHostFragment.findNavController(this).navigate(action)
        this@FidCardFragment.findNavController().navigate(action)
    }

    private fun addFidCardBtn() {
        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    if (it.value == true)
                        goToBarrecodeScaner()
                }
            }

        binding.addFidCardTxVw.setSafeOnClickListener {
            if (checkPermission(requireContext()))
                goToBarrecodeScaner()
            else
                requestMultiplePermissions.launch(arrayOf(Manifest.permission.CAMERA))
        }


    }

}

