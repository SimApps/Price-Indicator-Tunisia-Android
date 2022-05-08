package com.amirami.simapp.priceindicatortunisia.shoping

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.*

@Dao
interface ProdShopListDAO {

    //@Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   // @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(prodShopListEntity: ProdShopListEntity)

    @Update
    fun update(prodShopListEntity: ProdShopListEntity)

    //@Delete
    //fun delete(customEntity: CustomEntity)

    //  @Query("DELETE FROM custom_table WHERE Id = :id")
    // fun delete(id : Int?)

    @Query("DELETE FROM shoplist_table WHERE productid = :productid")
    fun delete(productid : Long?)

    @Query("UPDATE shoplist_table SET quantity=:quantity WHERE productid = :id")
    fun updateQuantity(quantity: Double?, id: Long?)

    @Query("DELETE FROM shoplist_table")
    fun deleteAll()

    //@Query("SELECT * FROM custom_table")
    @Query("SELECT * FROM shoplist_table ORDER BY productid ASC")
    fun getAll() : LiveData<List<ProdShopListEntity>>
}