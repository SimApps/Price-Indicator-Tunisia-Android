package com.amirami.simapp.priceindicatortunisia.products.room.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel

@Database(entities = [ProductModel::class], version = 1, exportSchema = false)
abstract class ProdShopListDb : RoomDatabase() {
    abstract fun prodShopListDAO(): ProdShopListDAO
}