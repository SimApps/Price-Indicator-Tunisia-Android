package com.amirami.simapp.priceindicatortunisia.core.local_db.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.products.room.data.dao.ProdShopListDAO
import com.amirami.simapp.priceindicatortunisia.productsnames.room.data.dao.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName

@Database(entities = [ProductModel::class, ProductName::class], version = 1, exportSchema = false)
abstract class LocalDb : RoomDatabase() {
    abstract fun prodShopListDAO(): ProdShopListDAO

    abstract fun bookDao(): ProdNamesDAO
}