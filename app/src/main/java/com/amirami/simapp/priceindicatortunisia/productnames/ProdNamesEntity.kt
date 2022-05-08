package com.amirami.simapp.priceindicatortunisia.productnames

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prodnames_table")
data class ProdNamesEntity (
    @ColumnInfo(name = "Name")
    var name : String? = null,
    ) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id : Int? = null

    constructor(id : Int, name : String) : this() {
        this.id = id
        this.name = name
    }

    override fun toString(): String {
        return "MessageThreadListEntity(Id=$id, Name=$name)"
    }
}