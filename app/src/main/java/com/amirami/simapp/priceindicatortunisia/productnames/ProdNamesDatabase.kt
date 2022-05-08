package com.amirami.simapp.priceindicatortunisia.productnames

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProdNamesEntity::class],
    version = 1
)
abstract class ProdNamesDatabase : RoomDatabase() {

    abstract fun customProductNameDao() : ProdNamesDAO

}