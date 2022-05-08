package com.amirami.simapp.priceindicatortunisia.fidcard

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FidCardEntity::class],
    version = 2
)
abstract class FidCardDatabase : RoomDatabase() {

    abstract fun fidCardNameDao() : FidCardDao

}