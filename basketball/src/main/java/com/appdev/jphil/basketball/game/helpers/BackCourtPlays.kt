package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.Pass
import com.appdev.jphil.basketball.plays.Press
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

            val play = if (press) {
                homeTeam.addFatigueFromPress()
                awayTeam.addFatigueFromPress()
                Press(game, consecutivePresses++)
            } else {
                Pass(game)
            }

            timeInBackcourt += timeRemaining - play.timeRemaining
            updateTimeRemaining(play)

            playerWithBall = play.playerWithBall
            location = play.location
            deadball = false

            FoulHelper.manageFoul(game, play.foul)
            ClockViolationHelper.getBackCourtViolation(game, play)
            ClockViolationHelper.getShotClockViolation(game, play)

            return mutableListOf(play)
        }
    }

}