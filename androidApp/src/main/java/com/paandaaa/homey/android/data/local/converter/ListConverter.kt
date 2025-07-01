package com.paandaaa.homey.android.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ListConverter {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        if (list == null) return null
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(string: String?): List<String>? {
        if (string == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(string, type)
    }
}