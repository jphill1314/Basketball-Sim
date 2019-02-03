package com.appdev.jphil.basketballcoach.database.game

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson

class GameTypeConverter {

    @TypeConverter
    fun listToJson(value: MutableList<Boolean>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): MutableList<Boolean> {
        return Gson().fromJson(value, Array<Boolean>::class.java).toMutableList()
    }
}