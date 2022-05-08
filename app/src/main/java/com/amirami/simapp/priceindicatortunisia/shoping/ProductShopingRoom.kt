package com.amirami.simapp.priceindicatortunisia.shoping


class ProductShopingRoom(
    var productid: Long?,
    date: String?,
    name: String?,
    namearabe: String?,
    marques: String?,
    marquesarabe: String?,
    var quantity: Double? = 0.0,
    description: String?,
    descriptionarabe: String?,
    imageurl: String?,
    type: String?,
    typesub: String?,
    typesubsub: String?,
    sieze: String?,
    monoprixprice: String?,
    monoprixremarq: String?,
    mgprice: String?,
    mgremarq: String?,
    carrefourprice: String?,
    carrefourremarq: String?,
    azzizaprice: String?,
    azzizaremarq: String?,
    geantprice: String?,
    geantremarq: String?,

    monoprixremarqmodifdate:String?,
    mgremarqmodifdate:String?,
    carrefourremarqmodifdate:String?,
    azzizaremarqmodifdate:String?,
    geantremarqmodifdate:String?,

    monoprixmodifdate: String?,
    mgmodifdate: String?,
    carrefourmodifdate: String?,
    azzizamodifdate: String?,
    geantmodifdate: String?,
    monoprixbonusfid: String?,
    mgbonusfid: String?,
    carrefourbonusfid: String?,
    azzizabonusfid: String?,
    geantbonusfid: String?,

    monoprixbonusfidmodifdate:String?,
    mgbonusfidmodifdate:String?,
    carrefourbonusfidmodifdate:String?,
    azzizabonusfidmodifdate:String?,
    geantbonusfidmodifdate:String?,

    monoprixPriceHistory: String?,
    mgpriceHistory: String?,
    azizaPriceHistory: String?,
    carrefourPriceHistory: String?,
    geantPriceHistory: String?
) {

    var date:String = date!!
    var name:String = name!!
    var namearabe:String = namearabe!!
    var marques:String = marques!!
    var marquesarabe:String = marquesarabe!!
    var description:String = description!!
    var descriptionarabe:String = descriptionarabe!!
    var imageurl:String = imageurl!!
    var type:String = type!!
    var typesub:String = typesub!!
    var typesubsub:String = typesubsub!!
    var sieze:String = sieze!!
    var monoprixprice:String = monoprixprice!!
    var monoprixremarq:String = monoprixremarq!!
    var mgprice:String = mgprice!!
    var mgremarq:String = mgremarq!!
    var carrefourprice:String = carrefourprice!!
    var carrefourremarq:String = carrefourremarq!!
    var azzizaprice:String = azzizaprice!!
    var azzizaremarq:String = azzizaremarq!!
    var geantprice:String = geantprice!!
    var geantremarq:String = geantremarq!!

    var monoprixremarqmodifdate:String =monoprixremarqmodifdate!!
    var mgremarqmodifdate:String = mgremarqmodifdate!!
    var carrefourremarqmodifdate:String = carrefourremarqmodifdate!!
    var azzizaremarqmodifdate:String = azzizaremarqmodifdate!!
    var geantremarqmodifdate:String = geantremarqmodifdate!!


    var monoprixmodifdate:String = monoprixmodifdate!!
    var mgmodifdate:String = mgmodifdate!!
    var carrefourmodifdate:String = carrefourmodifdate!!
    var azzizamodifdate:String = azzizamodifdate!!
    var geantmodifdate:String = geantmodifdate!!

    var monoprixbonusfid:String = monoprixbonusfid!!
    var mgbonusfid:String = mgbonusfid!!
    var carrefourbonusfid:String = carrefourbonusfid!!
    var azzizabonusfid:String = azzizabonusfid!!
    var geantbonusfid:String = geantbonusfid!!

    var monoprixbonusfidmodifdate:String = monoprixbonusfidmodifdate!!
    var mgbonusfidmodifdate:String = mgbonusfidmodifdate!!
    var carrefourbonusfidmodifdate:String = carrefourbonusfidmodifdate!!
    var azzizabonusfidmodifdate:String = azzizabonusfidmodifdate!!
    var geantbonusfidmodifdate:String = geantbonusfidmodifdate!!

    var monoprixPriceHistory:String = monoprixPriceHistory!!//: ArrayList<String> = ArrayList(),
    var mgpriceHistory:String = mgpriceHistory!!//: ArrayList<String> = ArrayList(),
    var azizaPriceHistory:String = azizaPriceHistory!!//: ArrayList<String> = ArrayList(),
    var carrefourPriceHistory:String = carrefourPriceHistory!!//: ArrayList<String> = ArrayList(),
    var geantPriceHistory:String = geantPriceHistory!!//: ArrayList<String> = ArrayList()


}

