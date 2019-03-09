package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.Player

interface ReboundTextContract {

    fun offensiveRebound(player: Player): String
    fun defensiveRebound(player: Player): String
}