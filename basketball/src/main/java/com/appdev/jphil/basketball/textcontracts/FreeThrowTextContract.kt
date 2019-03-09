package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.Player

interface FreeThrowTextContract {

    fun freeThrowText(shooter: Player, makes: Int, attempts: Int): String
    fun oneAndOneText(shooter: Player, makes: Int): String
}