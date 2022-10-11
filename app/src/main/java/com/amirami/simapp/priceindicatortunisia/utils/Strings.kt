package com.amirami.simapp.priceindicatortunisia.utils

import android.content.res.Resources

fun readString(resource: Int): String {
    return Resources.getSystem().getString(resource)
}