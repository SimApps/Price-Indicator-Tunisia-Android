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

    @Ignore
    @ColumnInfo(name = "userid")
    var userid: String = "",

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

    @Ignore
    var monoprixPriceHistory: ArrayList<String> = ArrayList(),
    @Ignore
    var mgpriceHistory: ArrayList<String> = ArrayList(),
    @Ignore
    var azizaPriceHistory: ArrayList<String> = ArrayList(),
    @Ignore
    var carrefourPriceHistory: ArrayList<String> = ArrayList(),
    @Ignore
    var geantPriceHistory: ArrayList<String> = ArrayList()
) {

    constructor(
        productid: String,
        date: String,
        name: String,
        namearabe: String,
        marques: String,
        marquesarabe: String,
        quantity: Double? = 0.0,
        description: String,
        descriptionarabe: String,
        imageurl: String,
        type: String,
        typesub: String,
        typesubsub: String,
        sieze: String,
        monoprixprice: String,
        monoprixremarq: String,
        mgprice: String,
        mgremarq: String,
        carrefourprice: String,
        carrefourremarq: String,
        azzizaprice: String,
        azzizaremarq: String,
        geantprice: String,
        geantremarq: String,

        monoprixremarqmodifdate: String,
        mgremarqmodifdate: String,
        carrefourremarqmodifdate: String,
        azzizaremarqmodifdate: String,
        geantremarqmodifdate: String,

        monoprixmodifdate: String,
        mgmodifdate: String,
        carrefourmodifdate: String,
        azzizamodifdate: String,
        geantmodifdate: String,

        monoprixbonusfid: String,
        mgbonusfid: String,
        carrefourbonusfid: String,
        azzizabonusfid: String,
        geantbonusfid: String,

        monoprixbonusfidmodifdate: String,
        mgbonusfidmodifdate: String,
        carrefourbonusfidmodifdate: String,
        azzizabonusfidmodifdate: String,
        geantbonusfidmodifdate: String,

        monoprixPriceHistory: String,
        mgpriceHistory: String,
        azizaPriceHistory: String,
        carrefourPriceHistory: String,
        geantPriceHistory: String
    ) : this() {

        this.id = productid
        this.date = date
        this.name = name
        this.namearabe = namearabe
        this.marques = marques
        this.marquesarabe = marquesarabe
        this.quantity = quantity!!
        this.description = description
        this.descriptionarabe = descriptionarabe
        this.imageurl = imageurl
        this.type = type
        this.typesub = typesub
        this.typesubsub = typesubsub
        this.sieze = sieze
        this.monoprixprice = monoprixprice
        this.monoprixremarq = monoprixremarq
        this.mgprice = mgprice
        this.mgremarq = mgremarq
        this.carrefourprice = carrefourprice
        this.carrefourremarq = carrefourremarq
        this.azzizaprice = azzizaprice
        this.azzizaremarq = azzizaremarq
        this.geantprice = geantprice
        this.geantremarq = geantremarq

        this.monoprixremarqmodifdate = monoprixremarqmodifdate
        this.mgremarqmodifdate = mgremarqmodifdate
        this.carrefourremarqmodifdate = carrefourremarqmodifdate
        this.azzizaremarqmodifdate = azzizaremarqmodifdate
        this.geantremarqmodifdate = geantremarqmodifdate

        this.monoprixmodifdate = monoprixmodifdate
        this.mgmodifdate = mgmodifdate
        this.carrefourmodifdate = carrefourmodifdate
        this.azzizamodifdate = azzizamodifdate
        this.geantmodifdate = geantmodifdate

        this.monoprixbonusfid = monoprixbonusfid
        this.mgbonusfid = mgbonusfid
        this.carrefourbonusfid = carrefourbonusfid
        this.azzizabonusfid = azzizabonusfid
        this.geantbonusfid = geantbonusfid

        this.monoprixbonusfidmodifdate = monoprixbonusfidmodifdate
        this.mgbonusfidmodifdate = mgbonusfidmodifdate
        this.carrefourbonusfidmodifdate = carrefourbonusfidmodifdate
        this.azzizabonusfidmodifdate = azzizabonusfidmodifdate
        this.geantbonusfidmodifdate = geantbonusfidmodifdate

        this.monoprixPricHistory = monoprixPriceHistory
        this.mgpricHistory = mgpriceHistory
        this.azizaPricHistory = azizaPriceHistory
        this.carrefourPricHistory = carrefourPriceHistory
        this.geantPricHistory = geantPriceHistory
    }

    constructor(
        type: String,
        typesub: String,
        typesubsub: String
    ) : this() {

        this.type = type
        this.typesub = typesub
        this.typesubsub = typesubsub
    }

    override fun toString(): String {
        return "MessageThreadListEntity(Id=$id, Name=$name)"
    }
}
