package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.Team

interface MiscTextContract {

    fun tenSecondViolation(team: Team): String
    fun shotClockViolation(team: Team): String
}