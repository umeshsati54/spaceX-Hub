package com.usati.spacex.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.usati.spacex.models.rocket.PayloadWeight
import java.lang.reflect.Type


class Converter {

//    @TypeConverter
//    fun fromList(list: List<Any>): String = list.joinToString("/n")
//
//
//    @TypeConverter
//    fun toStringList(source: String): List<String> = source.split("/n")
//
//    @TypeConverter
//    private fun <T> String.splitAndTrim(converter: (String) -> T):
//            List<T> = split("\n").filter { it.isNotEmpty() }.map(converter)
//
//    @TypeConverter
//    fun toPayloadWeightList(source: String) : List<PayloadWeight> =
//        source.splitAndTrim { PayloadWeight.fr }

    @TypeConverter
    fun fromStringToPl(value: String?): List<PayloadWeight?>? {
        val listType: Type = object : TypeToken<List<PayloadWeight?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListToPl(list: List<PayloadWeight?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToFi(value: String?): List<String?>? {
        val listType: Type = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListToFi(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}