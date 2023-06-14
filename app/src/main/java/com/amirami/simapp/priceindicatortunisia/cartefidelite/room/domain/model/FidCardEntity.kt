package com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model
/*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FidCard_table")
data class FidCardEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,



 /*   @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "value")
    var value: Long? = null,
*/

    @ColumnInfo(name = "value")
    var value: String? = null,

    @ColumnInfo(name = "barecodetype")
    var barecodetype: String? = null,


){

    constructor(id : Long,name : String, value : String, barecodetype: String) : this() {
        this.id = id
        this.name = name
        this.value = value
        this.barecodetype = barecodetype
    }
    constructor(name : String, value : String, barecodetype: String) : this() {
        this.name = name
        this.value = value
        this.barecodetype = barecodetype
    }

    override fun toString(): String {
        return "MessageThreadListEntity(Id=$value, Name=$name)"
    }
}
*/


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.core.Constants.Companion.FIDCARD_TABLE

@Entity(tableName = FIDCARD_TABLE)
data class FidCardEntity(
/*    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long? = null,*/

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "value")
    var value: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",


    @ColumnInfo(name = "barecodeformat")
    var barecodeformat: Int = -1,


    @ColumnInfo(name = "barecodetype")
    var barecodetype: Int = -1,


){

  /*  constructor(id : Long,name : String, value : String, barecodeformat: Int, barecodetype: Int) : this() {
        this.id = id
        this.name = name
        this.value = value
        this.barecodeformat = barecodeformat
        this.barecodetype = barecodetype
    }*/

     constructor( value : String, barecodeformat: Int, barecodetype: Int) : this() {
        this.value = value
        this.barecodeformat = barecodeformat
        this.barecodetype = barecodetype
    }

    override fun toString(): String {
        return "MessageThreadListEntity(Id=$value, Name=$name)"
    }
}






