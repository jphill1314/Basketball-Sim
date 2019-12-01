package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.CoachTalk
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.game.extensions.makeSubs

object TimeoutHelper {

    fun isTimeoutCalled(game: Game): Boolean {
        with (game) {
            if(deadball && !madeShot) {
                if(callMediaTimeout(this)) {
                    gamePlays.last().playAsString += miscText.mediaTimeOut()
                    return true
                }
            }
            if ((homeTeamHasBall || deadball) && homeTeam.coachWantsTimeout(homeScore - awayScore) && homeTimeouts > 0) {
                gamePlays.last().playAsString += miscText.timeOut(homeTeam, coachTimeoutExtends(this))
                homeTimeouts--
                return true
            } else if ((!homeTeamHasBall || deadball) && awayTeam.coachWantsTimeout(awayScore - homeScore) && awayTimeouts > 0) {
                gamePlays.last().playAsString += miscText.timeOut(awayTeam, coachTimeoutExtends(this))
                awayTimeouts--
                return true
            }
        }
        return false
    }

    fun runTimeout(game: Game) {
        with (game) {
            homeTeam.coachTalk(!isNeutralCourt, homeScore - awayScore, userIsCoaching)
            awayTeam.coachTalk(false, awayScore - homeScore, userIsCoaching)

            updateTimePlayed(true, false)

            homeTeam.userWantsTimeout = false
            awayTeam.userWantsTimeout = false

            homeTeam.lastScoreDiff = homeScore - awayScore
            awayTeam.lastScoreDiff = awayScore - homeScore

            deadball = true
            madeShot = false

            makeSubs()
        }
    }

    private fun callMediaTimeout(game: Game): Boolean {
        with (game) {
            return if (half == 1) {
                if (timeRemaining < 16 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[0]) {
                    mediaTimeOuts[0] = true
                    true
                }
                else if (timeRemaining < 12 * 60 && timeRemaining > 8 * 60 && !mediaTimeOuts[1]) {
                    mediaTimeOuts[1] = true
                    true
                }
                else if (timeRemaining < 8 * 60 && timeRemaining > 4 * 60 && !mediaTimeOuts[2]) {
                    mediaTimeOuts[2] = true
                    true
                }
                else if (timeRemaining < 4 * 60 && !mediaTimeOuts[3]) {
                    mediaTimeOuts[3] = true
                    true
                }
                else {
                    false
                }
            }
            else if (half == 2) {
                if (timeRemaining < 16 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[4]) {
                    mediaTimeOuts[4] = true
                    true
                }
                else if (timeRemaining < 12 * 60 && timeRemaining > 8 * 60 && !mediaTimeOuts[5]) {
                    mediaTimeOuts[5] = true
                    true
                }
                else if (timeRemaining < 8 * 60 && timeRemaining > 4 * 60 && !mediaTimeOuts[6]) {
                    mediaTimeOuts[6] = true
                    true
                }
                else if (timeRemaining < 4 * 60 && !mediaTimeOuts[7]) {
                    mediaTimeOuts[7] = true
                    true
                }
                else {
                    false
                }
            }
            else {
                false
            }
        }
    }

    private fun coachTimeoutExtends(game: Game): Boolean {
        with (game) {
            return if (half == 1) {
                if (timeRemaining < 16.5 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[0]) {
                    mediaTimeOuts[0]= true
                    true
                }
                else if (timeRemaining < 12.5 * 60 && timeRemaining > 8 * 60 && !mediaTimeOuts[1]) {
                    mediaTimeOuts[1] = true
                    true
                }
                else if (timeRemaining < 8.5 * 60 && timeRemaining > 4 * 60 && !mediaTimeOuts[2]) {
                    mediaTimeOuts[2] = true
                    true
                }
                else if (timeRemaining < 4.5 * 60 && !mediaTimeOuts[3]) {
                    mediaTimeOuts[3] = true
                    true
                }
                else {
                    false
                }
            }
            else if (half == 2) {
                if (timeRemaining < 16.5 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[4]) {
                    mediaTimeOuts[4] = true
                    true
                }
                else if (timeRemaining < 12.5 * 60 && timeRemaining > 8 * 60 && !mediaTimeOuts[5]) {
                    mediaTimeOuts[5] = true
                    true
                }
                else if (timeRemaining < 8.5 * 60 && timeRemaining > 4 * 60 && !mediaTimeOuts[6]) {
                    mediaTimeOuts[6] = true
                    true
                }
                else if (timeRemaining < 4.5 * 60 && !mediaTimeOuts[7]) {
                    mediaTimeOuts[7] = true
                    true
                }
                else {
                    false
                }
            }
            else {
                false
            }
        }
    }
}