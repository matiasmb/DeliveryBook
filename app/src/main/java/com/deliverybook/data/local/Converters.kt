package com.deliverybook.data.local

import androidx.room.TypeConverter

private const val NEIGHBOR_SEPARATOR = ";;"

class Converters {

    @TypeConverter
    fun fromNeighborsList(list: List<String>?): String? {
        if (list == null) return null
        if (list.isEmpty()) return ""
        return list.joinToString(separator = NEIGHBOR_SEPARATOR)
    }

    @TypeConverter
    fun toNeighborsList(serialized: String?): List<String> {
        if (serialized == null) return emptyList()
        if (serialized.isEmpty()) return emptyList()
        return serialized.split(NEIGHBOR_SEPARATOR)
    }
}

