package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.textcontracts.FastBreakTextContract

class FastBreakPlayText : FastBreakTextContract {

    override fun madeShot(shooter: Player): String {
        return "${shooter.fullName} takes the layup off the fast break and makes it!"
    }

    override fun missedShot(shooter: Player): String {
        return "${shooter.fullName} takes the layup off the fast break and misses it!"
    }
}