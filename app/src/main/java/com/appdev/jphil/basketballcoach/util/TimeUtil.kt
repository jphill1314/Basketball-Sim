package com.appdev.jphil.basketballcoach.util

object TimeUtil {

    fun getFormattedTime(timeInSeconds: Int, shotClock: Int): String {
        val min = timeInSeconds / 60
        val sec = timeInSeconds - min * 60
        return if (sec > 9) "$min:$sec ($shotClock)" else "$min:0$sec ($shotClock)"
    }
}