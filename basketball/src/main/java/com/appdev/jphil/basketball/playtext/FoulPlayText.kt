package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.textcontracts.FoulTextContract

class FoulPlayText : FoulTextContract {

    override fun offenseOffBallFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for an off-ball foul!"
    }

    override fun defenseOffBallFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for an off-ball foul!"
    }

    override fun offenseOnBallFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for the offensive foul!"
    }

    override fun defenseOnBallFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for a foul!"
    }

    override fun reboundingFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for the illegal box out!"
    }
}