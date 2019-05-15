package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.players.Player

interface PostMoveTextContract {

    fun madeShot(shooter: Player, fouled: Boolean): String
    fun missedShot(shooter: Player, fouled: Boolean): String
}