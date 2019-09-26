package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.MiscTextContract
import kotlin.random.Random

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

    override fun timeOut(team: Team, extendsToFull: Boolean): String {
        return "\n${team.name} have called a timeout" +
                if (extendsToFull) " and it will extend to a full media timeout." else "."
    }

    override fun endOfHalf(half: Int, gameOver: Boolean): String {
        return when {
            half == 1 -> "And that will bring us to the end of the first half."
            half == 2 && gameOver -> "And that will be the end of the game!"
            half == 2 && !gameOver -> "And that is the end of regulation! This game is going to overtime!"
            half > 2 && gameOver -> "And that will finally bring this game to an end after ${half - 2} overtimes!"
            else -> "And that's the end of overtime number ${half -2}, but we're going to need another one to decide a winner tonight!"
        }
    }

    override fun conjunction(canBeNegative: Boolean): String {
        val conjunctions = mutableListOf("And, ", "")
        if (canBeNegative) {
            conjunctions.add("But, ")
        }
        return conjunctions.random()
    }
}