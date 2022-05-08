package com.amirami.simapp.priceindicatortunisia.model

class ProdRoomNamesModel {

    var id: Int? = null
    var name: String? = null

    constructor(name : String) {
        this.name = name
    }

    constructor(id : Int, name : String) {
        this.id = id
        this.name = name
    }
}