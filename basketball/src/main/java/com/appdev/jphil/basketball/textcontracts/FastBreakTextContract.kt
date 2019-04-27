package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.players.Player

interface FastBreakTextContract {

    fun madeShot(shooter: Player): String
    fun missedShot(shooter: Player): String
}