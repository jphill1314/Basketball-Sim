package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.Pass
import com.appdev.jphil.basketball.plays.Rebound

object FastBreakPlays {

    fun getFastBreakPlay(game: Game): MutableList<BasketballPlay> {
        with (game) {
            val play = Pass(this)
            return if (play.points == 0) {
                mutableListOf(play, Rebound(this))
            } else {
                madeShot = true
                deadball = true
                if (homeTeamHasBall) {
                    homeScore += play.points
                } else {
                    awayScore += play.points
                }
                mutableListOf(play)
            }
        }
    }
}