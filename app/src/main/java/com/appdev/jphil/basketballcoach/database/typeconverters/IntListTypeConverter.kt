package com.appdev.jphil.basketballcoach.database.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson

class IntListTypeConverter {

    @TypeConverter
    fun listToJson(list: List<Int>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun JsonToList(json: String): MutableList<Int> {
        return Gson().fromJson(json, Array<Int>::class.java).toMutableList()
    }
}