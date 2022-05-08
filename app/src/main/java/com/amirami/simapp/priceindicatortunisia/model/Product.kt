package com.amirami.simapp.priceindicatortunisia.model

data class Product(var id:String?="",
                   val date:String? = "",
                   var name:String? = "",
                   var nameArabe:String? = "",
                   var marques:String? = "",
                   var marquesArabe:String? = "",
                   var description:String? = "",
                   var descriptionArabe:String? = "",
                   var imageurl:String? = "",
                   var type:String? ="",
                   var typesub:String? ="",
                   var typesubsub:String? ="",
                   var sieze:String? ="",
                   var monoprixprice:String? = "",
                   var monoprixremarq:String? = "",
                   var mgprice:String? = "",
                   var mgremarq:String? = "",
                   var carrefourprice:String? = "",
                   var carrefourremarq:String? = "",
                   var azzizaprice:String? = "",
                   var azzizaremarq:String? = "",
                   var geantprice:String? = "",
                   var geantremarq:String? = "",

                   var monoprixremarqmodifdate:String? = "",
                   var mgremarqmodifdate:String? = "",
                   var carrefourremarqmodifdate:String? = "",
                   var azzizaremarqmodifdate:String? = "",
                   var geantremarqmodifdate:String? = "",


                   var userid:String? = "",

                   var monoprixmodifdate:String? = "",
                   var mgmodifdate:String? = "",
                   var carrefourmodifdate:String? = "",
                   var azzizamodifdate:String? = "",
                   var geantmodifdate:String? = "",

                   var monoprixbonusfid:String? = "",
                   var mgbonusfid:String? = "",
                   var carrefourbonusfid:String? = "",
                   var azzizabonusfid:String? = "",
                   var geantbonusfid:String? = "",

                   var monoprixbonusfidmodifdate:String? = "",
                   var mgbonusfidmodifdate:String? = "",
                   var carrefourbonusfidmodifdate:String? = "",
                   var azzizabonusfidmodifdate:String? = "",
                   var geantbonusfidmodifdate:String? = "",

                   var monoprixPriceHistory: ArrayList<String> = ArrayList(),
                   var mgpriceHistory: ArrayList<String> = ArrayList(),
                   var azizaPriceHistory: ArrayList<String> = ArrayList(),
                   var carrefourPriceHistory: ArrayList<String> = ArrayList(),
                   var geantPriceHistory: ArrayList<String> = ArrayList()

    /*  var monoprixpricedate:String="Monoprix Price date",
      var mgpricedate:String="Magasin général Price date",
      var carrefourpricedate:String="Carrefour Price date",
      var azzizapricedate:String="Azziza Price date",
      var geantpricedate:String="Geant Price date"*/
)
