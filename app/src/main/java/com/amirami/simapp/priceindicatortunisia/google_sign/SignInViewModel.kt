package com.amirami.simapp.priceindicatortunisia.google_sign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignInViewModel: ViewModel() {

   /* private val _state = MutableStateFlow(SignInState())
   val state = _state.asStateFlow()
   fun onSignInResult(result: SignInResult) {
       _state.update { it.copy(
           isSignInSuccessful = result.data != null,
           signInError = result.errorMessage
       ) }
   }
   fun resetState() {
        _state.update { SignInState() }
    }
    */
    var state by mutableStateOf(SignInState())
        private set
    fun onSignInResult(result: SignInResult) {
        state = state.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        )
    }

    fun onSetIsSigneIn() {
        state = state.copy(
            isSignInSuccessful = true,
            signInError = null
        )
    }
    fun resetState() {
        state = SignInState()
    }
}