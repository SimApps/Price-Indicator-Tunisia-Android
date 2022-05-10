package com.amirami.simapp.priceindicatortunisia.dialoguesfragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.LicencesDialogueBinding
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class LicencesDialogFragment : BottomSheetDialogFragment() {
    lateinit var _binding: LicencesDialogueBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LicencesDialogueBinding.inflate(inflater, container, false)
        return _binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fun homepageChrome(homepageJson: String) {
            if (homepageJson != "") {
                val url: String = homepageJson
                val intent = Intent(Intent.ACTION_VIEW)

                if (!url.startsWith("http://") && !url.startsWith("https://")){
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
                else{
                    intent.data = Uri.parse(url)
                   startActivity(intent)
                }

            }
            else errorToast(requireContext(), getString(R.string.No_Homepage))
        }



            _binding.apply {
                androidxTxtV.setSafeOnClickListener {
                    homepageChrome("https://developer.android.com/jetpack/androidx")
                }
                lottiefilesTxV.setSafeOnClickListener {
                    homepageChrome("https://lottiefiles.com/")
                }
                EazeGraphTxtV.setSafeOnClickListener {
                    homepageChrome("https://github.com/blackfizz/EazeGraph#eazegraph")
                }

                imageFreeTxtV.setSafeOnClickListener {
                    homepageChrome("https://www.vecteezy.com/free-vector/icons")
                }

                netConnectionTxtV.setSafeOnClickListener {
                    homepageChrome("https://github.com/amrsalah3/NetDetect")
                }

                glideTxtV.setSafeOnClickListener {
                    homepageChrome("https://github.com/bumptech/glide")
                }


                GooglePlayServicesTxtV.setSafeOnClickListener {
                    homepageChrome("https://developers.google.com/android/guides/setup")
                }


                FirebaseAndroidTxtV.setSafeOnClickListener {
                    homepageChrome("https://developers.google.com/android/guides/setup")
                }

                flaticonLayout.setSafeOnClickListener {
                    homepageChrome("https://www.flaticon.com/authors/freepik")
                }


                barecodeScanerTxtV.setSafeOnClickListener {
                    homepageChrome("https://github.com/journeyapps/zxing-android-embedded")
                }


            }
    }



}
