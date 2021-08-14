package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.FastBreak
import com.appdev.jphil.basketball.plays.Rebound

object FastBreakPlays {

    fun getFastBreakPlay(game: Game): MutableList<BasketballPlay> {
        with(game) {
            val play = FastBreak(this)
            updateTimeRemaining(play)
            return if (play.points == 0) {
                val rebound = Rebound(this)
                playerWithBall = rebound.playerWithBall
                if (rebound.homeTeamStartsWithBall != rebound.homeTeamHasBall) {
                    changePossession()
                }
                FoulHelper.manageFoul(game, rebound.foul)
                mutableListOf(play, rebound)
            } else {
                madeShot = true
                deadball = true
                if (homeTeamHasBall) {
                    homeScore += play.points
                } else {
                    awayScore += play.points
                }
                changePossession()
                mutableListOf(play)
            }
        }
    }
}
