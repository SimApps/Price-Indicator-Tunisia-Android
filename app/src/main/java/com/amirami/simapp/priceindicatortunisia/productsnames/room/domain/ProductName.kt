package com.amirami.simapp.priceindicatortunisia.productsnames.room.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amirami.simapp.priceindicatortunisia.productsnames.room.core.Constants.Companion.PRODUCTS_NAMES_TABLE


@Entity(tableName = PRODUCTS_NAMES_TABLE)
data class ProductName (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Id")
    var id : String = "",
    @ColumnInfo(name = "Name")
    var name : String? = null,
)