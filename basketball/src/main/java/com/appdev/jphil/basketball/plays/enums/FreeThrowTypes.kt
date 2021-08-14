package com.appdev.jphil.basketball.plays.enums

enum class FreeThrowTypes {
    ONE_SHOT, TWO_SHOTS, THREE_SHOTS, ONE_AND_ONE;

    companion object {
        fun typeToNumber(type: FreeThrowTypes) = when (type) {
            ONE_SHOT -> 1
            TWO_SHOTS -> 2
            THREE_SHOTS -> 3
            ONE_AND_ONE -> -1
        }

        fun numberToType(num: Int) = when (num) {
            1 -> ONE_SHOT
            2 -> TWO_SHOTS
            3 -> THREE_SHOTS
            else -> ONE_AND_ONE
        }
    }
}
