package com.amirami.simapp.priceindicatortunisia.ui

import android.os.Bundle
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductInfoViewModel
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentPricesHistoryGraphBinding
import com.amirami.simapp.priceindicatortunisia.utils.Functions.checkedradio
import com.amirami.simapp.priceindicatortunisia.utils.Functions.formatDate
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeWord
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.eazegraph.lib.models.ValueLinePoint
import org.eazegraph.lib.models.ValueLineSeries
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.amirami.simapp.priceindicatortunisia.utils.Functions


@AndroidEntryPoint
class PricesHistoryGraphFragment : Fragment(R.layout.fragment_prices_history_graph) {
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()
    var monoprixHistoryPriceArray: ArrayList<String> = ArrayList()
    var mgHistoryPriceArray: ArrayList<String> = ArrayList()
    var azizaHistoryPriceArray: ArrayList<String> = ArrayList()
    var carrefourHistoryPriceArray: ArrayList<String> = ArrayList()
    var geantHistoryPriceArray: ArrayList<String> = ArrayList()


    private lateinit var binding: FragmentPricesHistoryGraphBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPricesHistoryGraphBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productInfoViewModel.putProductInfo.collectLatest {
                    when (it) {
                        is ProductInfoViewModel.LatestprodInfoTomodify.Success ->{
                            val product = it.product

                            monoprixHistoryPriceArray = product.monoprixPriceHistory
                            mgHistoryPriceArray = product.mgpriceHistory
                            azizaHistoryPriceArray = product.azizaPriceHistory
                            carrefourHistoryPriceArray = product.carrefourPriceHistory
                            geantHistoryPriceArray = product.geantPriceHistory


                            if (monoprixHistoryPriceArray.size > 1 && checkedradio == -55)
                                checkedradio = R.id.monoprixRadioBtn
                            else if (monoprixHistoryPriceArray.size <= 1)
                                binding.monoprixRadioBtn.visibility = View.GONE
                            if (mgHistoryPriceArray.size > 1 && checkedradio == -55)
                                checkedradio = R.id.mgRadioBtn
                            else if (mgHistoryPriceArray.size <= 1) binding.mgRadioBtn.visibility =
                                View.GONE
                            if (azizaHistoryPriceArray.size > 1 && checkedradio == -55)
                                checkedradio = R.id.azizaRadioBtn
                            else if (azizaHistoryPriceArray.size <= 1) binding.azizaRadioBtn.visibility =
                                View.GONE
                            if (carrefourHistoryPriceArray.size > 1 && checkedradio == -55)
                                checkedradio = R.id.carrefourRadioBtn
                            else if (carrefourHistoryPriceArray.size <= 1) binding.carrefourRadioBtn.visibility =
                                View.GONE
                            if (geantHistoryPriceArray.size > 1 && checkedradio == -55)
                                checkedradio = R.id.geantRadioBtn
                            else if (geantHistoryPriceArray.size <= 1) binding.geantRadioBtn.visibility =
                                View.GONE

                            when (checkedradio) {

                                R.id.monoprixRadioBtn -> {
                                    binding.monoprixRadioBtn.isChecked = true
                                    showGreaph(monoprixHistoryPriceArray)
                                    binding.historypriceTitleTxtVw.text =
                                        getString(R.string.Historique_des_prix) + getString(R.string.monoprix)
                                }
                                R.id.azizaRadioBtn -> {
                                    binding.azizaRadioBtn.isChecked = true
                                    showGreaph(azizaHistoryPriceArray)
                                    binding.historypriceTitleTxtVw.text =
                                        getString(R.string.Historique_des_prix) + getString(R.string.azziza)
                                }
                                R.id.mgRadioBtn -> {
                                    binding.mgRadioBtn.isChecked = true
                                    showGreaph(mgHistoryPriceArray)
                                    binding.historypriceTitleTxtVw.text =
                                        getString(R.string.Historique_des_prix) + getString(R.string.mg)
                                }
                                R.id.geantRadioBtn -> {
                                    binding.geantRadioBtn.isChecked = true
                                    showGreaph(geantHistoryPriceArray)
                                    binding.historypriceTitleTxtVw.text =
                                        getString(R.string.Historique_des_prix) + getString(R.string.Géant)
                                }
                                R.id.carrefourRadioBtn -> {
                                    binding.carrefourRadioBtn.isChecked = true
                                    showGreaph(carrefourHistoryPriceArray)
                                    binding.historypriceTitleTxtVw.text =
                                        getString(R.string.Historique_des_prix) + getString(R.string.carrefour)
                                }
                                else -> {

                                }
                            }

                            binding.historyPriceRadioGroupe.setOnCheckedChangeListener { group, checkedId ->
                                when (checkedId) {

                                    R.id.monoprixRadioBtn -> {
                                        checkedradio = R.id.monoprixRadioBtn
                                        productInfoViewModel.putprodInfoTomodify(product)
                                        recreateHistoryPriceFragment()
                                        //     showGreaph(monoprixHistoryPriceArray)

                                    }

                                    R.id.azizaRadioBtn -> {
                                        checkedradio = R.id.azizaRadioBtn
                                        productInfoViewModel.putprodInfoTomodify(product)
                                        recreateHistoryPriceFragment()
                                        //   showGreaph(azizaHistoryPriceArray)

                                    }
                                    R.id.mgRadioBtn -> {
                                        checkedradio = R.id.mgRadioBtn
                                        productInfoViewModel.putprodInfoTomodify(product)
                                        recreateHistoryPriceFragment()
                                        //  showGreaph(mgHistoryPriceArray)
                                    }
                                    R.id.geantRadioBtn -> {
                                        checkedradio = R.id.geantRadioBtn
                                        productInfoViewModel.putprodInfoTomodify(product)
                                        recreateHistoryPriceFragment()
                                        //  showGreaph(geantHistoryPriceArray)
                                    }
                                    R.id.carrefourRadioBtn -> {
                                        checkedradio = R.id.carrefourRadioBtn
                                        productInfoViewModel.putprodInfoTomodify(product)
                                        recreateHistoryPriceFragment()
                                        //  showGreaph(carrefourHistoryPriceArray)
                                    }

                                }
                            }
                        }
                        is ProductInfoViewModel.LatestprodInfoTomodify.Error -> Functions.errorToast(
                            requireContext(),
                            it.exception.toString()
                        )
                    }

                }

            }
        }
    }

    fun parseColor(colorString: String): Int {

        return colorString.toColorInt()
    }

    fun showGreaph(array: ArrayList<String>) {

        val series = ValueLineSeries()


        series.color = parseColor("#FF000000")// -0xa9480f  FF3700B3


        binding.cubiclinechart.addSeries(series)
        binding.cubiclinechart.startAnimation()

        for (i in 0 until array.size) {
            series.addPoint(
                ValueLinePoint(
                    formatDate(getDates(array[i])),
                    getPrices(array[i])
                )
            )
        }
    }


    private fun getDates(string: String): String {
        return removeWord(string.substringBefore(":", "0"), ":")
    }

    private fun getPrices(string: String): Float {
        return removeWord(string.substringAfter(":", "0"), ":").toFloat()

    }


    private fun recreateHistoryPriceFragment() {

        val action = PricesHistoryGraphFragmentDirections.actionPricesHistoryGraphFragmentSelf()
        this@PricesHistoryGraphFragment.findNavController()
            .navigate(action) //     NavHostFragment.findNavController(this).navigate(action)
    }


}
