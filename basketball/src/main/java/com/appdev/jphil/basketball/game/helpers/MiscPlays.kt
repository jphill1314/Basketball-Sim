package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.FreeThrows
import com.appdev.jphil.basketball.plays.IntentionalFoul
import com.appdev.jphil.basketball.plays.Rebound

object MiscPlays {

    fun getIntentionalFoul(game: Game): MutableList<BasketballPlay> {
        with (game) {
            if (!deadball) {
                if (homeTeamHasBall) {
                    if (awayTeam.intentionallyFoul) {
                        return mutableListOf(IntentionalFoul(this))
                    }
                } else {
                    if (homeTeam.intentionallyFoul) {
                        return mutableListOf(IntentionalFoul(this))
                    }
                }
            }
            return mutableListOf()
        }
    }

    fun getFreeThrows(game: Game): MutableList<BasketballPlay> {
        with (game) {
            shootFreeThrows = false
            val plays = mutableListOf<BasketballPlay>()
            val freeThrows = FreeThrows(this, numberOfFreeThrows)
            addPoints(freeThrows.points)
            plays.add(freeThrows)

            if(!freeThrows.madeLastShot){
                plays.add(Rebound(this))
                deadball = false
            }

            return plays
        }
    }
}