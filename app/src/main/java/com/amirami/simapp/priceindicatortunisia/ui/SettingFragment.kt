package com.amirami.simapp.priceindicatortunisia.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.FragmentSettingBinding
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.amirami.simapp.priceindicatortunisia.utils.Functions.userRecord
import com.amirami.simapp.priceindicatortunisia.utils.exhaustive
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductInfoViewModel
import com.firebase.ui.auth.AuthUI
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            // Successfully signed in

            _binding.signinOutItxVw.text = resources.getString(R.string.Déconnecter)
            // _binding?.signinOutImVw?.setImageResource(R.drawable.ic_signout)
            _binding.signinOutItxVw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signout, 0, 0, 0)
            DynamicToast.makeSuccess(requireContext(), resources.getString(R.string.Connexion_réussie), 9)
            // ...
        } else {
            DynamicToast.makeError(requireContext(), resources.getString(R.string.Échec_connexion), 9).show()
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    lateinit var _binding: FragmentSettingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingBinding.bind(view)

        if (userRecord.currentUser != null) {
            _binding.signinOutItxVw.text = resources.getString(R.string.Déconnecter)
            _binding.signinOutItxVw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signout, 0, 0, 0)
            // _binding?.signinOutImVw?.setImageResource(R.drawable.ic_signout)
        } else {
            // no one loged in
            _binding.signinOutItxVw.text = resources.getString(R.string.Connecter)
            _binding.signinOutItxVw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signin, 0, 0, 0)
            //    _binding?.signinOutImVw?.setImageResource(R.drawable.ic_signin)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productInfoViewModel.dialogueEvents.collect { event ->
                when (event) {
                    is ProductInfoViewModel.DialogueEvents.PutAddDialogueInfo -> {
                        run {
                            if (event.id == "signinOut") signOut()
                            else if (event.id == "contactus") reportProblem()
                        }
                    }

                    else -> {}
                }.exhaustive
            }
        }

        _binding.signinOutItxVw.setSafeOnClickListener {
            if (userRecord.currentUser != null) {
                goToAddDialog("signinOut")
            } else {
                createSignInIntent()
            }
        }

        _binding.rateTvVw.setSafeOnClickListener {
            rate()
        }

        _binding.whatIsThisappTvVw.setSafeOnClickListener {
            goToAddDialog("whatIsthisApp")
        }

        _binding.problemReportTxVw.setSafeOnClickListener {
            goToAddDialog("contactus")
        }

        _binding.moreappTvVw.setSafeOnClickListener {
            more_apps()
        }

        _binding.licencesTxVw.setSafeOnClickListener {
            goToLiceneceDialog()
        }
    }

    private fun createSignInIntent() {
        // when change theme !!! to check
        // val providers = emptyList<AuthUI.IdpConfig>()
        // [START auth_fui_create_intent]
        // Choose authentication providers

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
            // AuthUI.IdpConfig.PhoneBuilder().build(),
            // AuthUI.IdpConfig.GoogleBuilder().build(),
            // AuthUI.IdpConfig.FacebookBuilder().build(),
            // AuthUI.IdpConfig.TwitterBuilder().build()
        )
        // Create and launch sign-in intent

        startForResult.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                //   .setLogo(R.drawable.my_great_logo) // Set logo drawable
                //  .setTheme(R.style.MySuperAppTheme) // Set theme
                .build()
        )
        // [END auth_fui_create_intent]
    }

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                // ...
                _binding.signinOutItxVw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signin, 0, 0, 0)
                // _binding?.signinOutImVw?.setImageResource(R.drawable.ic_signin)
                _binding.signinOutItxVw.text = resources.getString(R.string.Connecter)
                errorToast(requireContext(), resources.getString(R.string.Déconnection_réussite))
            }
        // [END auth_fui_signout]
    }

   /* private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(requireContext())
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_delete]
    }
    private fun privacyAndTerms() {
        val providers = emptyList<AuthUI.IdpConfig>()
        // [START auth_fui_pp_tos]
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html")
                .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_pp_tos]
    }
*/

    private fun rate() {
        val uri = Uri.parse("market://details?id=" + requireContext().packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            (
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + requireContext().packageName)
                )
            )
        }
    }

    private fun reportProblem() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_email)))
            intent.type = "message/rfc822" // "text/plain"

            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Nous_Contacter))

            requireContext().startActivity(Intent.createChooser(intent, getString(R.string.Nous_contacter_Email)))
        } catch (e: Exception) {
            DynamicToast.makeError(requireContext(), e.toString(), 9).show()
        }
    }

    private fun more_apps() {
        val uri = Uri.parse("https://play.google.com/store/apps/developer?id=AmiRami")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            (
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
        )
        try { requireContext().startActivity(goToMarket) } catch (e: ActivityNotFoundException) {
            requireContext().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=AmiRami")
                )
            )
        }
    }

    private fun goToLiceneceDialog() {
        val action = SettingFragmentDirections.actionSettingFragmentToLicencesDialogFragment()
        this@SettingFragment.findNavController().navigate(action) //  NavHostFragment.findNavController(requireParentFragment()).navigate(action)
    }

    private fun goToAddDialog(actions: String) {
        val action = SettingFragmentDirections.actionSettingFragmentToDialogFragment(actions)
        this@SettingFragment.findNavController().navigate(action) //     NavHostFragment.findNavController(requireParentFragment()).navigate(action)
    }
}
