package com.amirami.simapp.priceindicatortunisia.shopingfragment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shoplist_table")
data class ProdShopListEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "productid")
    var productid: Long? = null,
    @ColumnInfo(name = "date")
    var date: String? = null,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "namearabe")
    var namearabe: String? = null,
    @ColumnInfo(name = "marques")
    var marques: String? = null,
    @ColumnInfo(name = "marquesarabe")
    var marquesarabe: String? = null,
    @ColumnInfo(name = "quantity")
    var quantity: Double? = 0.0,
    @ColumnInfo(name = "description")
    var description: String? = null,
    @ColumnInfo(name = "descriptionarabe")
    var descriptionarabe: String? = null,
    @ColumnInfo(name = "imageurl")
    var imageurl: String? = null,
    @ColumnInfo(name = "type")
    var type: String? = null,
    @ColumnInfo(name = "typesub")
    var typesub: String? = null,
    @ColumnInfo(name = "typesubsub")
    var typesubsub: String? = null,
    @ColumnInfo(name = "sieze")
    var sieze: String? = null,
    @ColumnInfo(name = "monoprixprice")
    var monoprixprice: String? = null,
    @ColumnInfo(name = "monoprixremarq")
    var monoprixremarq: String? = null,
    @ColumnInfo(name = "mgprice")
    var mgprice: String? = null,
    @ColumnInfo(name = "mgremarq")
    var mgremarq: String? = null,
    @ColumnInfo(name = "carrefourprice")
    var carrefourprice: String? = null,
    @ColumnInfo(name = "carrefourremarq")
    var carrefourremarq: String? = null,
    @ColumnInfo(name = "azzizaprice")
    var azzizaprice: String? = null,
    @ColumnInfo(name = "azzizaremarq")
    var azzizaremarq: String? = null,
    @ColumnInfo(name = "geantprice")
    var geantprice: String? = null,
    @ColumnInfo(name = "geantremarq")
    var geantremarq: String? = null,

    @ColumnInfo(name = "monoprixremarqmodifdate")
    var monoprixremarqmodifdate: String? = null,
    @ColumnInfo(name = "mgremarqmodifdate")
    var mgremarqmodifdate: String? = null,
    @ColumnInfo(name = "carrefourremarqmodifdate")
    var carrefourremarqmodifdate: String? = null,
    @ColumnInfo(name = "azzizaremarqmodifdate")
    var azzizaremarqmodifdate: String? = null,
    @ColumnInfo(name = "geantremarqmodifdate")
    var geantremarqmodifdate: String? = null,


    @ColumnInfo(name = "monoprixmodifdate")
    var monoprixmodifdate: String? = null,
    @ColumnInfo(name = "mgmodifdate")
    var mgmodifdate: String? = null,
    @ColumnInfo(name = "carrefourmodifdate")
    var carrefourmodifdate: String? = null,
    @ColumnInfo(name = "azzizamodifdate")
    var azzizamodifdate: String? = null,
    @ColumnInfo(name = "geantmodifdate")
    var geantmodifdate: String? = null,
    @ColumnInfo(name = "monoprixbonusfid")
    var monoprixbonusfid: String? = null,
    @ColumnInfo(name = "mgbonusfid")
    var mgbonusfid: String? = null,
    @ColumnInfo(name = "carrefourbonusfid")
    var carrefourbonusfid: String? = null,
    @ColumnInfo(name = "azzizabonusfid")
    var azzizabonusfid: String? = null,
    @ColumnInfo(name = "geantbonusfid")
    var geantbonusfid: String? = null,

    @ColumnInfo(name = "monoprixbonusfidmodifdate")
    var monoprixbonusfidmodifdate: String? = null,
    @ColumnInfo(name = "mgbonusfidmodifdate")
    var mgbonusfidmodifdate: String? = null,
    @ColumnInfo(name = "carrefourbonusfidmodifdate")
    var carrefourbonusfidmodifdate: String? = null,
    @ColumnInfo(name = "azzizabonusfidmodifdate")
    var azzizabonusfidmodifdate: String? = null,
    @ColumnInfo(name = "geantbonusfidmodifdate")
    var geantbonusfidmodifdate: String? = null,


    @ColumnInfo(name = "monoprixPriceHistory")
    var monoprixPriceHistory: String? = null,
    @ColumnInfo(name = "mgpriceHistory")
    var mgpriceHistory: String? = null,
    @ColumnInfo(name = "azizaPriceHistory")
    var azizaPriceHistory: String? = null,
    @ColumnInfo(name = "carrefourPriceHistory")
    var carrefourPriceHistory: String? = null,
    @ColumnInfo(name = "geantPriceHistory")
    var geantPriceHistory: String? = null
) {


    constructor(
        productid: Long,
        date: String?,
        name: String?,
        namearabe: String?,
        marques: String?,
        marquesarabe: String?,
        quantity: Double? = 0.0,
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

        monoprixremarqmodifdate: String?,
        mgremarqmodifdate: String?,
        carrefourremarqmodifdate: String?,
        azzizaremarqmodifdate: String?,
        geantremarqmodifdate: String?,


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

        monoprixbonusfidmodifdate: String?,
        mgbonusfidmodifdate: String?,
        carrefourbonusfidmodifdate: String?,
        azzizabonusfidmodifdate: String?,
        geantbonusfidmodifdate: String?,

        monoprixPriceHistory: String?,
        mgpriceHistory: String?,
        azizaPriceHistory: String?,
        carrefourPriceHistory: String?,
        geantPriceHistory: String?
    ) : this() {

        this.productid = productid
        this.date = date
        this.name = name
        this.namearabe = namearabe
        this.marques = marques
        this.marquesarabe = marquesarabe
        this.quantity = quantity
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

            this.monoprixPriceHistory = monoprixPriceHistory
        this.mgpriceHistory = mgpriceHistory
        this.azizaPriceHistory = azizaPriceHistory
        this.carrefourPriceHistory = carrefourPriceHistory
        this.geantPriceHistory = geantPriceHistory
    }

    override fun toString(): String {
        return "MessageThreadListEntity(Id=$productid, Name=$name)"
    }
}
