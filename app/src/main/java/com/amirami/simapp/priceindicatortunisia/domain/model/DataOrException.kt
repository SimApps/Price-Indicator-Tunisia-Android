package com.amirami.simapp.priceindicatortunisia.domain.model

data class DataOrException<T, E : String?>(
    var data: T? = null,
    var e:String?=null //E? = null
)