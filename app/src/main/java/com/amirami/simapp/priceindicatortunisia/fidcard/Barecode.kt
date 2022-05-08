package com.amirami.simapp.priceindicatortunisia.fidcard




class Barecode {

    var id: Long? = null
    var name: String? = null
    var value: String? = null
    var barecodetype: String? = null


    constructor(name : String,value : String,barecodetype: String) {

        this.name = name
        this.value = value
        this.barecodetype= barecodetype

    }

    constructor(id: Long, name : String,value : String,barecodetype: String) {
        this.id = id
        this.name = name
        this.value = value
        this.barecodetype= barecodetype

    }
    constructor(name : String) {

        this.name = name
    }

    constructor(value : String, barecodetype : String) {
        this.value = value
        this.barecodetype = barecodetype
    }
}