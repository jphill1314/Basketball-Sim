package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.game.extensions.makeSubs
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.FreeThrows
import com.appdev.jphil.basketball.plays.IntentionalFoul
import com.appdev.jphil.basketball.plays.Rebound

object MiscPlays {

    fun canIntentionalFoul(game: Game): Boolean {
        with(game) {
            if (!deadball) {
                return if (homeTeamHasBall) awayTeam.intentionallyFoul else homeTeam.intentionallyFoul
            }
            return false
        }
    }

    fun getIntentionalFoul(game: Game): MutableList<BasketballPlay> {
        with(game) {
            if (!deadball) {
                if (homeTeamHasBall) {
                    if (awayTeam.intentionallyFoul) {
                        val foul = IntentionalFoul(game)
                        FoulHelper.manageFoul(game, foul.foul)
                        return mutableListOf(foul)
                    }
                } else {
                    if (homeTeam.intentionallyFoul) {
                        val foul = IntentionalFoul(game)
                        FoulHelper.manageFoul(game, foul.foul)
                        return mutableListOf(foul)
                    }
                }
            }
            return mutableListOf()
        }
    }

    fun getFreeThrows(game: Game): MutableList<BasketballPlay> {
        with(game) {
            shootFreeThrows = false
            location = 1
            val plays = mutableListOf<BasketballPlay>()
            val freeThrows = FreeThrows(game, freeThrowType)
            if (homeTeamHasBall) {
                homeScore += freeThrows.points
            } else {
                awayScore += freeThrows.points
            }
            plays.add(freeThrows)

            if (!freeThrows.madeLastShot) {
                val rebound = Rebound(game)
                if (rebound.homeTeamStartsWithBall != rebound.homeTeamHasBall) {
                    changePossession()
                }
                deadball = false
                madeShot = false
                plays.add(rebound)
            } else {
                deadball = true
                madeShot = false
                makeSubs()
                changePossession()
            }
            playerWithBall = plays.last().playerWithBall
            resetShotClock()
            return plays
        }
    }
}
