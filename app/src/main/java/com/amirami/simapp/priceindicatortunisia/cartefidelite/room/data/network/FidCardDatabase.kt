package com.amirami.simapp.priceindicatortunisia.cartefidelite.room.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity

@Database(
    entities = [FidCardEntity::class],
    version = 2,
    exportSchema = false
)
abstract class FidCardDatabase : RoomDatabase() {

    abstract fun fidCardNameDao() : FidCardDao

}