package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.Foul
import com.appdev.jphil.basketball.plays.enums.FoulType

object FoulHelper {

    fun manageFoul(game: Game, foul: Foul) {
        if (foul.foulType != FoulType.CLEAN) {
            updateGameAfterFoul(game, foul)
        }
    }

    private fun updateGameAfterFoul(game: Game, foul: Foul) {
        with (game) {
            deadball = true
            madeShot = false // allow media timeouts to be called
            timeInBackcourt = 0 // reset time for 10 second call
            lastPassWasGreat = false
            var isOnHomeTeam = false

            if (foul.isOnDefense) {
                if (foul.homeTeamHasBall) {
                    awayFouls++
                }
                else {
                    homeFouls++
                    isOnHomeTeam = true
                }
            }
            else {
                if (foul.homeTeamHasBall) {
                    homeFouls++
                    isOnHomeTeam = true
                }
                else {
                    awayFouls++
                }
            }

            if (foul.isOnDefense || foul.foulType == FoulType.REBOUNDING) {
                if (!shootFreeThrows) {
                    if (isOnHomeTeam && awayFouls > 6) {
                        shootFreeThrows = true
                        numberOfFreeThrows = if (awayFouls >= 10) {
                            2
                        } else {
                            -1
                        }
                    } else if (!isOnHomeTeam && homeFouls > 6) {
                        shootFreeThrows = true
                        numberOfFreeThrows = if (homeFouls >= 10) {
                            2
                        } else {
                            -1
                        }
                    }
                }

                if (shotClock < Game.resetShotClockTime) {
                    shotClock = if (timeRemaining > Game.resetShotClockTime) Game.resetShotClockTime else timeRemaining
                }
                if (shootFreeThrows) {
                    shotClock = if (timeRemaining > Game.lengthOfShotClock) Game.lengthOfShotClock else timeRemaining
                }
            }

            if (!foul.isOnDefense && foul.foulType != FoulType.REBOUNDING) {
                changePossession()
            } else if (foul.foulType == FoulType.REBOUNDING) {
                if (foul.isOnDefense) {
                    if (homeTeamHasBall) {
                        if (foul.defense.teamId == homeTeam.teamId) {
                            changePossession()
                        }
                    } else {
                        if (foul.defense.teamId == awayTeam.teamId) {
                            changePossession()
                        }
                    }
                } else {
                    if (homeTeamHasBall) {
                        if (foul.offense.teamId == homeTeam.teamId) {
                            changePossession()
                        }
                    } else {
                        if (foul.offense.teamId == awayTeam.teamId) {
                            changePossession()
                        }
                    }
                }
            }
        }
    }
}