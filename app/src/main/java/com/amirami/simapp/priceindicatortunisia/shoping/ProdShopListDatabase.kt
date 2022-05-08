package com.amirami.simapp.priceindicatortunisia.shoping

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProdShopListEntity::class],
    version = 3
)
abstract class ProdShopListDatabase : RoomDatabase() {

    abstract fun customDao() : ProdShopListDAO

}