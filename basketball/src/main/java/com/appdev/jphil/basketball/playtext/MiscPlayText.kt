package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.textcontracts.MiscTextContract

class MiscPlayText : MiscTextContract {

    override fun tenSecondViolation(team: Team): String {
        return "\n${team.name} have turned the ball over on a 10 second violation!\""
    }

    override fun shotClockViolation(team: Team): String {
        return "\n${team.name} have turned the ball over on a shot clock violation!"
    }
}