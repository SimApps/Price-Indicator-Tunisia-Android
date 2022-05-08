package com.amirami.simapp.priceindicatortunisia.fidcard

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



