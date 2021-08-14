package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.EndOfHalf
import com.appdev.jphil.basketball.plays.TipOff

object HalfTimeHelper {

    fun startHalf(game: Game) {
        with(game) {
            updateTimePlayed(false, false)

            if (half != 2) {
                // Tip off starts game and all overtime periods
                val tipOff = TipOff(this)
                homeTeamHasBall = tipOff.homeTeamHasBall
                homeTeamHasPossessionArrow = !tipOff.homeTeamHasBall
                playerWithBall = tipOff.playerWithBall
                gamePlays.add(tipOff)
                deadball = false
                madeShot = false
                if (half != 1) {
                    gamePlays.add(EndOfHalf(this, false, homeTeamHasBall))
                    homeTimeouts++
                    awayTimeouts++
                }
            } else {
                val homeTeamHadBall = homeTeamHasBall
                homeTeamHasBall = homeTeamHasPossessionArrow
                deadball = true
                madeShot = false

                gamePlays.add(EndOfHalf(this, false, homeTeamHadBall))

                if (homeTimeouts == Game.maxTimeouts) {
                    homeTimeouts--
                }
                if (awayTimeouts == Game.maxTimeouts) {
                    awayTimeouts--
                }
            }

            timeRemaining = if (half < 3) Game.lengthOfHalf else Game.lengthOfOvertime
            lastTimeRemaining = timeRemaining
            shotClock = Game.lengthOfShotClock
            location = 0

            homeTeam.lastScoreDiff = homeScore - awayScore
            awayTeam.lastScoreDiff = awayScore - homeScore

            if (half < 3) {
                homeFouls = 0
                awayFouls = 0
            }

            if (half == 2) {
                // half time
                updateTimePlayed(false, true)
            } else {
                // coach talk before game or between overtime periods
                TimeoutHelper.runTimeout(this)
                deadball = false
                madeShot = false
            }
        }
    }
}
