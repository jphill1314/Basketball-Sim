package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.FastBreak
import com.appdev.jphil.basketball.plays.Rebound
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.FreeThrowTypes

object FastBreakPlays {

    fun getFastBreakPlay(game: Game): MutableList<BasketballPlay> {
        with(game) {
            val play = FastBreak(this)
            val plays = mutableListOf<BasketballPlay>(play)
            updateTimeRemaining(play)

            location = 1
            if (homeTeamHasBall) {
                homeScore += play.points
            } else {
                awayScore += play.points
            }
            updateTimeRemaining(play)
            resetShotClock()
            val rebound = getRebound(game, play)
            if (rebound == null) {
                if (!getFreeThrows(game, play)) {
                    // made shot and no free throws
                    madeShot = true
                    deadball = true
                    changePossession()
                } else {
                    FoulHelper.manageFoul(game, play.foul)
                }
            } else {
                plays.add(rebound)
                playerWithBall = rebound.playerWithBall
                if (rebound.homeTeamStartsWithBall != rebound.homeTeamHasBall) {
                    changePossession()
                }
                FoulHelper.manageFoul(game, rebound.foul)
            }
            return plays
        }
    }

    private fun getRebound(game: Game, shot: BasketballPlay): Rebound? {
        if (shot.points == 0 && shot.foul.foulType == FoulType.CLEAN) {
            return Rebound(game)
        }
        return null
    }

    private fun getFreeThrows(game: Game, shot: BasketballPlay): Boolean {
        if (shot.foul.foulType != FoulType.CLEAN) {
            when {
                shot.points != 0 -> game.freeThrowType = FreeThrowTypes.ONE_SHOT
                shot.foul.foulType == FoulType.SHOOTING_LONG -> game.freeThrowType = FreeThrowTypes.THREE_SHOTS
                else -> game.freeThrowType = FreeThrowTypes.TWO_SHOTS
            }
            game.shootFreeThrows = true
            return true
        }
        return false
    }
}
