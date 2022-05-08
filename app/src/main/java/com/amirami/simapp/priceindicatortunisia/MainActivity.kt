package com.amirami.simapp.priceindicatortunisia

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.amirami.simapp.priceindicatortunisia.viewmodel.PreferencesViewModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions.loadInterstitialAd
import com.amirami.simapp.priceindicatortunisia.utils.Functions.nbrInterstitialAdShowed
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import com.narify.netdetect.NetDetect
import java.util.*
import com.amirami.simapp.priceindicatortunisia.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    // val db = FirebaseFirestore.getInstance()
    private val preferencesViewModel : PreferencesViewModel by viewModels()
    private lateinit var adViewAdaptiveBanner: AdView
    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.

    private val adSize: AdSize
        get() {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { this.display }
            else {
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay
            }
            val outMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // val display = activity.display
                display?.getRealMetrics(outMetrics)
            } else {

                //  val display = activity.windowManager.defaultDisplay
                @Suppress("DEPRECATION")
                display?.getMetrics(outMetrics)
            }

            // display!!.getMetrics(outMetrics)

            val density = outMetrics.density
            val adAdaptivebannerMain=findViewById<FrameLayout>(R.id.ad_adaptivebanner_main)
            var adWidthPixels = adAdaptivebannerMain.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        loadInterstitialAd(this@MainActivity)

            lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                nbrInterstitialAdShowed = preferencesViewModel.preferencesFlow.first().nbr_Interstitial_Ad_Showed
            }
        }


       // setContentView(R.layout.activity_main)
        Locale.setDefault(Locale("en", "US"))

          try {
          // supportActionBar?.setDisplayHomeAsUpEnabled(false)
           this@MainActivity.supportActionBar?.hide()
       } catch (e: NullPointerException) { }

        NetDetect.init(this@MainActivity)

        // Initialize the Mobile Ads SDK.

        MobileAds.initialize(this) { }
        adViewAdaptiveBanner = AdView(this)
        binding.adAdaptivebannerMain.addView(adViewAdaptiveBanner)
        loadBanner()






        setupNavController()

    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if( destination.id == R.id.pricesHistoryGraphFragment
                || destination.id == R.id.AddProductFragment
                || destination.id == R.id.barrecodeScannerFragment
                || destination.id == R.id.imageFidCarDialogFragment) {
             //   toolbar.visibility = View.GONE
                binding.bottomNavigation.visibility = View.GONE
            }
            else {
             //   toolbar.visibility = View.VISIBLE
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        adViewAdaptiveBanner.resume()
    }


    public override fun onPause() {
        adViewAdaptiveBanner.pause()
        super.onPause()
    }


    public override fun onDestroy() {
        adViewAdaptiveBanner.destroy()
        super.onDestroy()
    }


    private fun loadBanner() {
        adViewAdaptiveBanner.adUnitId =  getString(R.string.banner_adUnitId)

        adViewAdaptiveBanner.adSize = adSize

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
        val adRequest = AdRequest
            .Builder()
            // .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .build()

        // Start loading the ad in the background.
        adViewAdaptiveBanner.loadAd(adRequest)
    }




}