package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.Pass
import com.appdev.jphil.basketball.plays.Press
import com.appdev.jphil.basketball.plays.enums.Plays
import kotlin.random.Random

object BackCourtPlays {

    fun getBackCourtPlay(game: Game): MutableList<BasketballPlay> {
        with (game) {
            val liveBallModifier = when {
                deadball -> 0
                consecutivePresses != 1 -> 0
                else -> 50
            }
            val press: Boolean = if (homeTeamHasBall) {
                Random.nextInt(100) < awayTeam.pressFrequency - liveBallModifier
            } else {
                Random.nextInt(100) < homeTeam.pressFrequency - liveBallModifier
            }

            val backcourtPlays: MutableList<BasketballPlay> = if (press) {
                mutableListOf(Press(this, consecutivePresses++))
            } else {
                mutableListOf(Pass(this))
            }

            if (backcourtPlays.last().type != Plays.FOUL) {
                deadball = false
            }

            timeInBackcourt += timeRemaining - backcourtPlays[0].timeRemaining

            return backcourtPlays
        }
    }

}