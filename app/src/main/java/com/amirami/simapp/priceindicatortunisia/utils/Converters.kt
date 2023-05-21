package com.amirami.simapp.priceindicatortunisia.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }




    @TypeConverter
    fun fromString(value: String): ArrayList<String> {

        val placeJson = if(value=="")"{}" else value

        val listType = object :
            TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(placeJson, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }






}