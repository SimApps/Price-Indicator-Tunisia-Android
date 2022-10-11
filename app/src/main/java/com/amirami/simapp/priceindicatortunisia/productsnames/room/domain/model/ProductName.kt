package com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amirami.simapp.priceindicatortunisia.productsnames.room.core.Constants.Companion.PRODUCTS_NAMES_TABLE


@Entity(tableName = PRODUCTS_NAMES_TABLE)
data class ProductName (
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