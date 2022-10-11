package com.amirami.simapp.priceindicatortunisia.core

import android.util.Log

class Utils {
    companion object {
        fun printMessage(message: String) {
            Log.d("TAG", message)
        }


        val <T> T.exhaustive: T
            get() = this
    }
}