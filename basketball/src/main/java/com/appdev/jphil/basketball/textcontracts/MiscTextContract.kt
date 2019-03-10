package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.Team

interface MiscTextContract {

    fun tenSecondViolation(team: Team): String
    fun shotClockViolation(team: Team): String
    fun mediaTimeOut(): String
    fun timeOut(team: Team): String
    fun endOfHalf(half: Int, gameOver: Boolean): String
}