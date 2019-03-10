package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.textcontracts.FreeThrowTextContract

class FTPlayText : FreeThrowTextContract {

    override fun freeThrowText(shooter: Player, makes: Int, attempts: Int): String {
        return when (attempts) {
            1 -> {
                when (makes) {
                    0 -> "${shooter.firstName} misses his free throw."
                    else -> "${shooter.firstName} makes his free throw."
                }
            }
            2 -> {
                when (makes) {
                    0 -> "${shooter.firstName} misses both of his free throws."
                    1 -> "${shooter.firstName} makes one of his free throws."
                    else -> "${shooter.firstName} makes both of his free throws."
                }
            }
            else -> {
                when (makes) {
                    0 -> "${shooter.firstName} misses all $attempts of his free throws."
                    else -> "${shooter.firstName} makes $makes of $attempts of his free throws."
                }
            }
        }
    }

    override fun oneAndOneText(shooter: Player, makes: Int): String {
        return when (makes) {
            0 -> "${shooter.fullName} misses the front end of the 1 and 1."
            1 -> "${shooter.fullName} makes his first shot, but misses the second."
            else -> "${shooter.fullName} makes both shots from the 1 and 1."
        }
    }
}