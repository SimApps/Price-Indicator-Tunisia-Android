package com.amirami.simapp.priceindicatortunisia.core

import androidx.core.text.isDigitsOnly
import java.text.NumberFormat
import java.util.Locale

object StringFormatting {

    fun convertStringToPriceFormat(input : String): String {
        val format = NumberFormat.getCurrencyInstance(Locale("fr", "TN"))

        format.maximumFractionDigits = 3

        return if(input=="" /*|| input== "0.0"*/) "" else format.format(stringToDouble(input))
    }

    fun returnNAifEmpty(string: String?): String = if (!string.isNullOrBlank()) string else "N/A"


    fun checkIfNull(string: String): String = string.ifBlank { "" }

    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

    fun convertStringToDoubleFormat(input: String) :String {
        val format = NumberFormat.getNumberInstance(Locale("fr", "TN"))
        format.maximumFractionDigits = 3
        return if(input=="" || input== "0.0") "0" else format.format(input.toDouble())
    }

    fun usingKotlinStringFormat(input: Double, scale: Int) = "%.${scale}f".format(input)



    fun removeTrailingZeroInDouble(value : String):String =
       if(value.orEmpty().trimEnd { it == '0' }.trimEnd { it == '.' }.trimEnd { it == ',' }!="")
          value.orEmpty().trimEnd { it == '0' }.trimEnd { it == '.' }.trimEnd { it == ',' }
     else "0"
    fun stringToDouble(value : String?):Double =
        if (
            value == "null"||
           value.isNullOrBlank() //||
       // value.isEmpty() ||
       // value.isNullOrEmpty()// ||
       // !value.isDigitsOnly() ||
       // value.toDoubleOrNull() == null
    )
      0.0
     else value.replace(",",".")  .toDouble()



}