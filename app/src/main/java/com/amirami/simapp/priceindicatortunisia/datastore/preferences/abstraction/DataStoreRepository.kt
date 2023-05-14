package com.amirami.simapp.priceindicatortunisia.datastore.preferences.abstraction

interface DataStoreRepository {

    suspend fun putString(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getString(key: String): String
    suspend fun getInt(key: String): Int
    suspend fun getBoolean(key: String): Boolean
}