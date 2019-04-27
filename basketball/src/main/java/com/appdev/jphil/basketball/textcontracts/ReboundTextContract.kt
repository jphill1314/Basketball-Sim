package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.players.Player

interface ReboundTextContract {

    fun offensiveRebound(player: Player): String
    fun defensiveRebound(player: Player): String
}