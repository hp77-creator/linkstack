package com.hp77.linkstash.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hp77.linkstash.domain.model.LinkType

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromLinkType(value: LinkType): String = value.name

    @TypeConverter
    fun toLinkType(value: String): LinkType = try {
        LinkType.valueOf(value)
    } catch (e: IllegalArgumentException) {
        LinkType.OTHER
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}
