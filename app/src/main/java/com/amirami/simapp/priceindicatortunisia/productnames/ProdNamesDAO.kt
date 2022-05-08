package com.amirami.simapp.priceindicatortunisia.productnames

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProdNamesDAO {

    //@Insert
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(prodNamesEntity: ProdNamesEntity)

    @Update
    fun update(prodNamesEntity: ProdNamesEntity)

    //@Delete
    //fun delete(customEntity: CustomEntity)

  //  @Query("DELETE FROM custom_table WHERE Id = :id")
   // fun delete(id : Int?)

    @Query("DELETE FROM prodnames_table WHERE name = :productname")
    fun delete(productname : String?)



    @Query("DELETE FROM prodnames_table")
    fun deleteAll()

    //@Query("SELECT * FROM custom_table")
    @Query("SELECT * FROM prodnames_table ORDER BY Id ASC")
    fun getAll() : LiveData<List<ProdNamesEntity>>
}