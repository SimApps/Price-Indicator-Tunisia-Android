package com.amirami.simapp.priceindicatortunisia.products.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import java.util.ArrayList

@Entity(tableName = "shoplist_table")
data class ProductModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "namearabe")
    var namearabe: String = "",
    @ColumnInfo(name = "marques")
    var marques: String = "",
    @ColumnInfo(name = "marquesarabe")
    var marquesarabe: String = "",
    @get:Exclude
    @ColumnInfo(name = "quantity")
    var quantity: Double = 0.0,
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "descriptionarabe")
    var descriptionarabe: String = "",
    @ColumnInfo(name = "imageurl")
    var imageurl: String = "",
    @ColumnInfo(name = "type")
    var type: String = "",
    @ColumnInfo(name = "typesub")
    var typesub: String = "",
    @ColumnInfo(name = "typesubsub")
    var typesubsub: String = "",
    @ColumnInfo(name = "sieze")
    var sieze: String = "",
    @ColumnInfo(name = "monoprixprice")
    var monoprixprice: String = "",
    @ColumnInfo(name = "monoprixremarq")
    var monoprixremarq: String = "",
    @ColumnInfo(name = "mgprice")
    var mgprice: String = "",
    @ColumnInfo(name = "mgremarq")
    var mgremarq: String = "",
    @ColumnInfo(name = "carrefourprice")
    var carrefourprice: String = "",
    @ColumnInfo(name = "carrefourremarq")
    var carrefourremarq: String = "",
    @ColumnInfo(name = "azzizaprice")
    var azzizaprice: String = "",
    @ColumnInfo(name = "azzizaremarq")
    var azzizaremarq: String = "",
    @ColumnInfo(name = "geantprice")
    var geantprice: String = "",
    @ColumnInfo(name = "geantremarq")
    var geantremarq: String = "",

    @ColumnInfo(name = "monoprixremarqmodifdate")
    var monoprixremarqmodifdate: String = "",
    @ColumnInfo(name = "mgremarqmodifdate")
    var mgremarqmodifdate: String = "",
    @ColumnInfo(name = "carrefourremarqmodifdate")
    var carrefourremarqmodifdate: String = "",
    @ColumnInfo(name = "azzizaremarqmodifdate")
    var azzizaremarqmodifdate: String = "",
    @ColumnInfo(name = "geantremarqmodifdate")
    var geantremarqmodifdate: String = "",

    @ColumnInfo(name = "monoprixmodifdate")
    var monoprixmodifdate: String = "",
    @ColumnInfo(name = "mgmodifdate")
    var mgmodifdate: String = "",
    @ColumnInfo(name = "carrefourmodifdate")
    var carrefourmodifdate: String = "",
    @ColumnInfo(name = "azzizamodifdate")
    var azzizamodifdate: String = "",
    @ColumnInfo(name = "geantmodifdate")
    var geantmodifdate: String = "",
    @ColumnInfo(name = "monoprixbonusfid")
    var monoprixbonusfid: String = "",
    @ColumnInfo(name = "mgbonusfid")
    var mgbonusfid: String = "",
    @ColumnInfo(name = "carrefourbonusfid")
    var carrefourbonusfid: String = "",
    @ColumnInfo(name = "azzizabonusfid")
    var azzizabonusfid: String = "",
    @ColumnInfo(name = "geantbonusfid")
    var geantbonusfid: String = "",

    @ColumnInfo(name = "monoprixbonusfidmodifdate")
    var monoprixbonusfidmodifdate: String = "",
    @ColumnInfo(name = "mgbonusfidmodifdate")
    var mgbonusfidmodifdate: String = "",
    @ColumnInfo(name = "carrefourbonusfidmodifdate")
    var carrefourbonusfidmodifdate: String = "",
    @ColumnInfo(name = "azzizabonusfidmodifdate")
    var azzizabonusfidmodifdate: String = "",
    @ColumnInfo(name = "geantbonusfidmodifdate")
    var geantbonusfidmodifdate: String = "",



    @get:Exclude
    @ColumnInfo(name = "monoprixPriceHistory")
    var monoprixPricHistory: String = "",
    @get:Exclude
    @ColumnInfo(name = "mgpriceHistory")
    var mgpricHistory: String = "",
    @get:Exclude
    @ColumnInfo(name = "azizaPriceHistory")
    var azizaPricHistory: String = "",
    @get:Exclude
    @ColumnInfo(name = "carrefourPriceHistory")
    var carrefourPricHistory: String = "",
    @get:Exclude
    @ColumnInfo(name = "geantPriceHistory")
    var geantPricHistory: String = "",


){
    @Ignore
    @ColumnInfo(name = "userid")
    var userid: String = ""

    @Ignore
    var monoprixPriceHistory: ArrayList<String> = ArrayList()

    @Ignore
    var mgpriceHistory: ArrayList<String> = ArrayList()

    @Ignore
    var azizaPriceHistory: ArrayList<String> = ArrayList()

    @Ignore
    var carrefourPriceHistory: ArrayList<String> = ArrayList()

    @Ignore
    var geantPriceHistory: ArrayList<String> = ArrayList()
}
