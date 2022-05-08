package com.amirami.simapp.priceindicatortunisia.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.amirami.simapp.priceindicatortunisia.utils.Functions.hideKeyboard
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductInfoViewModel
import com.amirami.simapp.priceindicatortunisia.databinding.BarecodescannerViewBinding
import com.amirami.simapp.priceindicatortunisia.fidcard.Barecode
import com.amirami.simapp.priceindicatortunisia.utils.Functions.warningToast
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener
import dagger.hilt.android.AndroidEntryPoint


// This property is only valid between onCreateView and
// onDestroyView.

@AndroidEntryPoint
class BarrecodeScannerFragment : Fragment(R.layout.barecodescanner_view), View.OnClickListener,
    TorchListener {
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()
    private lateinit var _binding: BarecodescannerViewBinding

    private var capture: CaptureManager? = null
    //private var barcodeScannerView: DecoratedBarcodeView? = null
    // private var switchFlashlightButton: Button? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BarecodescannerViewBinding.bind(view)
        _binding.zxingBarcodeScanner.setTorchListener(this)

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            _binding.switchFlashlight.visibility = View.GONE
        }


        capture = CaptureManager(requireActivity(), _binding.zxingBarcodeScanner)
        //   capture!!.initializeFromIntent(intent, savedInstanceState)
        capture!!.decode()


        _binding.zxingBarcodeScanner.decodeSingle(callback)

//        viewfinderView = requireView().findViewById(R.id.zxing_viewfinder_view)


        requireContext().hideKeyboard(requireView())


        _binding.flashOnOffBtn.setOnClickListener {
            switchFlashlight()
        }
        /*  _binding?.switchFlashlight?.setOnClickListener {
              switchFlashlight()
          }*/

        //    DynamicToast.makeWarning(requireContext(), tag, 9).show()
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {

        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null) warningToast(requireContext(), requireActivity().getString(R.string.Annulé))


            else {
                _binding.zxingBarcodeScanner.setStatusText(result.text)
                val fidcard = Barecode(result.text!!, result.barcodeFormat.toString())
                productInfoViewModel.putBareCodeInfo(fidcard)
                NavHostFragment.findNavController(this@BarrecodeScannerFragment).navigateUp()


            }

        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


    override fun onResume() {
        _binding.zxingBarcodeScanner.resume()
        super.onResume()
    }

    override fun onPause() {
        _binding.zxingBarcodeScanner.pause()
        super.onPause()
    }

/*    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeScannerView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }*/

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private fun hasFlash(): Boolean {
        return requireContext().packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    private fun switchFlashlight() {
        if (requireContext().getString(R.string.turn_on_flashlight) == _binding.switchFlashlight.text) {
            _binding.zxingBarcodeScanner.setTorchOn()
            _binding.flashOnOffBtn.setImageResource(R.drawable.ic_flash_off_indicator)
        } else {
            _binding.zxingBarcodeScanner.setTorchOff()
            _binding.flashOnOffBtn.setImageResource(R.drawable.ic_flash_on_indicator)

        }
    }

    override fun onTorchOn() {
        _binding.switchFlashlight.text = requireContext().getString(R.string.turn_off_flashlight)
        _binding.flashOnOffBtn.setImageResource(R.drawable.ic_flash_off_indicator)
    }

    override fun onTorchOff() {
        _binding.switchFlashlight.text = requireContext().getString(R.string.turn_on_flashlight)
        _binding.flashOnOffBtn.setImageResource(R.drawable.ic_flash_on_indicator)
    }


    /* fun changeMaskColor(view: View?) {
         val rnd = Random()
         val color: Int = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

         viewfinderView!!.setMaskColor(color)
     }

     fun changeLaserVisibility(visible: Boolean) {
         viewfinderView!!.setLaserVisibility(visible)
     }*/

}