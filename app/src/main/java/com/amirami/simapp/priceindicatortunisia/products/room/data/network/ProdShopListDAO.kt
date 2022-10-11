package com.amirami.simapp.priceindicatortunisia.products.room.data.network

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.products.room.core.Constants.Companion.SHOPING_LIST_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdShopListDAO {
    @Query("SELECT * FROM $SHOPING_LIST_TABLE ORDER BY name ASC")
    fun getShopList(): Flow<List<ProductModel>>

    @Query("SELECT * FROM $SHOPING_LIST_TABLE WHERE id = :id")
    fun getItemShopList(id: Int): Flow<ProductModel>

    @Insert(onConflict = REPLACE)
    fun addProduct(productName: ProductModel)

    @Update
    fun updateShopList(productName: ProductModel)

    @Query("UPDATE $SHOPING_LIST_TABLE SET quantity=:quantity WHERE id = :id")
    fun updateQuantity(quantity: Double?, id: Long?)

    @Query("DELETE FROM $SHOPING_LIST_TABLE WHERE id = :productid")
    fun deleteShopingProduct(productid: String)

    @Query("DELETE FROM $SHOPING_LIST_TABLE")
    fun deleteAll()
}
