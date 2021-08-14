package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay

object ClockViolationHelper {

    fun getShotClockViolation(game: Game, play: BasketballPlay) {
        with(game) {
            if (shotClock == 0 && timeRemaining > 0) {
                play.playAsString += if (homeTeamHasBall) {
                    miscText.shotClockViolation(homeTeam)
                } else {
                    miscText.shotClockViolation(awayTeam)
                }
                handleTurnover(this)
            }
        }
    }

    fun getBackCourtViolation(game: Game, play: BasketballPlay) {
        with(game) {
            if (timeInBackcourt >= 10 && location == -1) {
                val overshoot = timeInBackcourt - 10
                timeRemaining += overshoot
                shotClock += overshoot
                play.playAsString += if (homeTeamHasBall) {
                    miscText.tenSecondViolation(homeTeam)
                } else {
                    miscText.tenSecondViolation(awayTeam)
                }
                handleTurnover(this)
            }
        }
    }

    private fun handleTurnover(game: Game) {
        with(game) {
            if (homeTeamHasBall) {
                homeTeam.turnovers++
            } else {
                awayTeam.turnovers ++
            }
            changePossession()
        }
    }
}
