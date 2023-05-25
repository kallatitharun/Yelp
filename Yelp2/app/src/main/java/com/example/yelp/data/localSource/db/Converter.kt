package com.example.yelp.data.localSource.db

import androidx.room.TypeConverter
import com.google.gson.Gson

/**
 * This class is responsible for converting unusual SQL types to primitive data type
 */
class Converters {

    private val gson = Gson()

    /**
     * Serves to convert string value (json array) to list of strings
     * as DB does not support storing list at it is
     */
    @TypeConverter
    fun listFromString(value: String): List<String> {
        return gson.fromJson(value, Array<String>::class.java).toList()
    }

    /**
     * Serves to convert list of strings to string (json array)
     * as DB does not support storing list at it is
     */
    @TypeConverter
    fun listToString(list: List<String>): String {
        return gson.toJson(list)
    }

    /**
     * Serves to convert string value (json array) to list of ints
     * as DB does not support storing list at it is
     */
    @TypeConverter
    fun intListFromString(value: String): List<Int> {
        return gson.fromJson(value, Array<Int>::class.java).toList()
    }

    /**
     * Serves to convert list of ints to string (json array)
     * as DB does not support storing list at it is
     */
    @TypeConverter
    fun intListToString(list: List<Int>): String {
        return gson.toJson(list)
    }

}