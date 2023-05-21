package com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.data.network

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.core.Constants.Companion.FIDCARD_TABLE
import com.amirami.simapp.priceindicatortunisia.screens.cartefidelite.room.domain.model.FidCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FidCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun upsert(fidcardEntity: FidCardEntity)//: Long

    @Query("SELECT * FROM $FIDCARD_TABLE")
    fun get(): Flow<List<FidCardEntity>>


    @Query("SELECT * FROM $FIDCARD_TABLE  WHERE value = :value")
    fun getByValue(value: String): Flow<FidCardEntity?>

    @Query("DELETE FROM $FIDCARD_TABLE")
    fun deleteAll()




    @Query("DELETE FROM $FIDCARD_TABLE WHERE value = :value") //you can use this too, for delete note by id.
     fun deleteFidCardById(value: String)
}