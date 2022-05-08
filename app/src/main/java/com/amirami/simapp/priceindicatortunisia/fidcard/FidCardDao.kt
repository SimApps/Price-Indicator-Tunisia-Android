package com.amirami.simapp.priceindicatortunisia.fidcard

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FidCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(fidcardEntity: FidCardEntity): Long

    @Query("SELECT * FROM FidCard_table")
    fun get(): LiveData<List<FidCardEntity>>


    @Query("DELETE FROM FidCard_table")
    fun deleteAll()




    @Query("DELETE FROM FidCard_table WHERE Id = :Id") //you can use this too, for delete note by id.
    suspend fun deleteFidCardById(Id: String)
}