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

    override fun mediaTimeOut(): String {
        return "\nAnd that will bring us to a media timeout."
    }

    override fun timeOut(team: Team): String {
        return "\n${team.name} have called a timeout."
    }

    override fun endOfHalf(half: Int, gameOver: Boolean): String {
        return "\n" + when {
            half == 1 -> "And that will bring us to the end of the first half."
            half == 2 && gameOver -> "And that will be the end of the game!"
            half == 2 && !gameOver -> "And that is the end of regulation! This game is going to overtime!"
            half > 2 && gameOver -> "And that will finally bring this game to an end after ${half - 2} overtimes!"
            else -> "And that's the end of overtime number ${half -2}, but we're going to need another one to decide a winner tonight!"
        }
    }
}