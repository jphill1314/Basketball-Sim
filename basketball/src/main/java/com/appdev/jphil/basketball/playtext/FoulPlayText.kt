package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.textcontracts.FoulTextContract

class FoulPlayText : FoulTextContract {

    override fun offenseOffBallFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for an illegal screen!"
    }

    override fun defenseOffBallFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for a defensive foul away from the ball!"
    }

    override fun offenseOnBallFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for the charge!"
    }

    override fun defenseOnBallFoul(fouler: Player, fouled: Player): String {
        return "${fouler.fullName} is called for a foul on the ball handler!"
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