package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.textcontracts.FastBreakTextContract

class FastBreakPlayText : FastBreakTextContract {

    override fun madeLayup(shooter: Player): String {
        return "${shooter.fullName} takes the layup off the fast break and makes it!"
    }

    override fun madeLayupWithFoul(shooter: Player, fouler: Player): String {
        return "${shooter.fullName} goes up for the layout, draws a foul, and makes the shot! " +
            "${fouler.fullName} tried to stop ${shooter.firstName}, but was only able to muster " +
            "some weak contact."
    }

    override fun missedLayup(shooter: Player): String {
        return "${shooter.fullName} takes the layup off the fast break and misses it!"
    }

    override fun missedLayupWithFoul(shooter: Player, fouler: Player): String {
        return "${shooter.fullName} goes up for the layup, but is fouled hard by " +
            "${fouler.fullName} and is unable to convert."
    }

    override fun blockedLayup(shooter: Player, blocker: Player): String {
        return "${shooter.fullName} goes up for the layup and is blocked by ${blocker.fullName}! " +
            "What a spectacular play to deny the easy layup!"
    }

    override fun madeThree(shooter: Player): String {
        return "${shooter.fullName} pulls up for three and drains it!"
    }

    override fun missedThree(shooter: Player): String {
        return "${shooter.fullName} pulls up for three and misses it! He had an easy layup, but got greed and now will have nothing to show for it."
    }
}
