package com.amirami.simapp.priceindicatortunisia.productsnames.room.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName

@Database(entities = [ProductName::class], version = 1, exportSchema = false)
abstract class BookDb : RoomDatabase() {
    abstract fun bookDao(): ProdNamesDAO
}