package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.Foul
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.FreeThrowTypes

object FoulHelper {

    fun manageFoul(game: Game, foul: Foul) {
        if (foul.foulType != FoulType.CLEAN) {
            updateGameAfterFoul(game, foul)
        }
    }

    private fun updateGameAfterFoul(game: Game, foul: Foul) {
        with(game) {
            deadball = true
            madeShot = false // allow media timeouts to be called
            timeInBackcourt = 0 // reset time for 10 second call
            lastPassWasGreat = false
            val isOnHomeTeam = incrementFouls(game, foul)

            when (foul.foulType) {
                FoulType.SHOOTING_LONG, FoulType.SHOOTING_CLOSE, FoulType.SHOOTING_MID -> resetShotClock(game)
                else -> handleNonShootingFoul(game, foul, isOnHomeTeam)
            }

            if (shootFreeThrows && timeRemaining == 0) {
                timeRemaining = 1
                shotClock = 1
            }
        }
    }

    private fun handleNonShootingFoul(game: Game, foul: Foul, isOnHomeTeam: Boolean) {
        with(game) {
            if (foul.isOnDefense || foul.foulType == FoulType.REBOUNDING) {
                addBonusFreeThrows(game, isOnHomeTeam)
                resetShotClock(game)
            }

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

    private fun incrementFouls(game: Game, foul: Foul): Boolean {
        return if (foul.isOnDefense) {
            if (foul.homeTeamHasBall) {
                game.awayFouls++
                false
            }
            else {
                game.homeFouls++
                true
            }
        }
        else {
            if (foul.homeTeamHasBall) {
                game.homeFouls++
                true
            }
            else {
                game.awayFouls++
                false
            }
        }
    }

    private fun addBonusFreeThrows(game: Game, isOnHomeTeam: Boolean) {
        with(game) {
            if (!shootFreeThrows) {
                if (isOnHomeTeam && homeFouls > 6) {
                    shootFreeThrows = true
                    freeThrowType = if (homeFouls >= 10) {
                        FreeThrowTypes.TWO_SHOTS
                    } else {
                        FreeThrowTypes.ONE_AND_ONE
                    }
                } else if (!isOnHomeTeam && awayFouls > 6) {
                    shootFreeThrows = true
                    freeThrowType = if (awayFouls >= 10) {
                        FreeThrowTypes.TWO_SHOTS
                    } else {
                        FreeThrowTypes.ONE_AND_ONE
                    }
                }
            }
        }
    }

    private fun resetShotClock(game: Game) {
        with(game) {
            if (shotClock < Game.resetShotClockTime) {
                shotClock = if (timeRemaining > Game.resetShotClockTime) Game.resetShotClockTime else timeRemaining
            }
            if (shootFreeThrows) {
                shotClock = if (timeRemaining > Game.lengthOfShotClock) Game.lengthOfShotClock else timeRemaining
            }
        }
    }
}