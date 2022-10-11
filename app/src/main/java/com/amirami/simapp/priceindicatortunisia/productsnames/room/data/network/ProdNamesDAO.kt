package com.amirami.simapp.priceindicatortunisia.productsnames.room.data.network

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.amirami.simapp.priceindicatortunisia.productsnames.room.core.Constants.Companion.PRODUCTS_NAMES_TABLE
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.model.ProductName
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdNamesDAO {
    @Query("SELECT * FROM $PRODUCTS_NAMES_TABLE ORDER BY id ASC")
    fun getProdNames(): Flow<List<ProductName>>

    @Query("SELECT * FROM $PRODUCTS_NAMES_TABLE WHERE id = :id")
    fun getBook(id: Int): Flow<ProductName>

    @Insert(onConflict = REPLACE)
    fun addBook(productName: ProductName)

    @Update
    fun updateBook(productName: ProductName)

    @Query("DELETE FROM $PRODUCTS_NAMES_TABLE WHERE name = :productname")
    fun deleteProdName(productname: String)


    @Query("DELETE FROM $PRODUCTS_NAMES_TABLE")
    fun deleteAll()
}