package com.appdev.jphil.basketball.teams

import kotlin.random.Random

enum class TeamColor(val type: Int) {
    Red(0),
    Pink(1),
    Purple(2),
    DeepPurple(3),
    Indigo(4),
    Blue(5),
    LightBlue(6),
    Cyan(7),
    Teal(8),
    Green(9),
    LightGreen(10),
    Yellow(11),
    Orange(12),
    DeepOrange(13),
    BlueGrey(14);

    companion object {
        fun fromInt(type: Int) = when (type) {
            0 -> Red
            1 -> Pink
            2 -> Purple
            3 -> DeepPurple
            4 -> Indigo
            5 -> Blue
            6 -> LightBlue
            7 -> Cyan
            8 -> Teal
            9 -> Green
            10 -> LightGreen
            11 -> Yellow
            12 -> Orange
            13 -> DeepOrange
            14 -> BlueGrey
            else -> Red
        }

        fun random() = fromInt(Random.nextInt(14))
    }
}