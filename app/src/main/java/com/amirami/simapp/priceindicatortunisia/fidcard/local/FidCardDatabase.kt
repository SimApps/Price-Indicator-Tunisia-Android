package com.amirami.simapp.priceindicatortunisia.fidcard.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardEntity

@Database(
    entities = [FidCardEntity::class],
    version = 2
)
abstract class FidCardDatabase : RoomDatabase() {

    abstract fun fidCardNameDao() : FidCardDao

}