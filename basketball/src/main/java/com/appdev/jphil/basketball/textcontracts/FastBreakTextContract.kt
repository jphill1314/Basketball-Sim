package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.players.Player

interface FastBreakTextContract {

    fun madeLayup(shooter: Player): String
    fun madeLayupWithFoul(shooter: Player, fouler: Player): String
    fun missedLayup(shooter: Player): String
    fun missedLayupWithFoul(shooter: Player, fouler: Player): String
    fun blockedLayup(shooter: Player, blocker: Player): String

    fun madeThree(shooter: Player): String
    fun missedThree(shooter: Player): String
}
