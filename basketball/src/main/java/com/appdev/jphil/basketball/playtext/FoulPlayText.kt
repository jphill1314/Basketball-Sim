package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.players.Player
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

    override fun offensiveReboundingFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for the illegal box out!"
    }

    override fun defensiveReboundingFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for the over the back foul!"
    }

    override fun intentionalFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} intentionally fouls ${fouled.fullName}."
    }
}