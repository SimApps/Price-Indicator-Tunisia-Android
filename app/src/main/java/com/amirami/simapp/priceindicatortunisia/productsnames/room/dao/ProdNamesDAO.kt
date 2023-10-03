package com.amirami.simapp.priceindicatortunisia.productsnames.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.amirami.simapp.priceindicatortunisia.productsnames.room.core.Constants.Companion.PRODUCTS_NAMES_TABLE
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.ProductName
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdNamesDAO {
    @Query("SELECT * FROM $PRODUCTS_NAMES_TABLE ORDER BY id ASC")
    fun getProdNames(): Flow<List<ProductName>>

    @Query("SELECT * FROM $PRODUCTS_NAMES_TABLE WHERE id = :id")
    fun getName(id: Int): Flow<ProductName>

    @Upsert
    fun addName(productName: ProductName)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllNames(productName: List<ProductName>)

    //@Update
    @Query("UPDATE $PRODUCTS_NAMES_TABLE SET name = :updatedName where Id = :id")

    fun updateName(id: String, updatedName: String)

    @Query("DELETE FROM $PRODUCTS_NAMES_TABLE WHERE id = :codeBare")
    fun deleteProdName(codeBare: String)


    @Query("DELETE FROM $PRODUCTS_NAMES_TABLE")
    fun deleteAll()
}