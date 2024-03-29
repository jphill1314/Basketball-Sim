package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.players.Player

interface FoulTextContract {

    fun offenseOffBallFoul(fouler: Player, fouled: Player): String
    fun defenseOffBallFoul(fouler: Player, fouled: Player): String

    fun offenseOnBallFoul(fouler: Player, fouled: Player): String
    fun defenseOnBallFoul(fouler: Player, fouled: Player): String

    fun offensiveReboundingFoul(fouler: Player, fouled: Player): String
    fun defensiveReboundingFoul(fouler: Player, fouled: Player): String

    fun intentionalFoul(fouler: Player, fouled: Player): String
}
