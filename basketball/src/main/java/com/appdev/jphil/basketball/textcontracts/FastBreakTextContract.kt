package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.Player

interface FastBreakTextContract {

    fun madeShot(shooter: Player): String
    fun missedShot(shooter: Player): String
}