package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.*
import com.appdev.jphil.basketball.plays.enums.FoulType
import kotlin.random.Random

object FrontCourtPlays {

    fun getFrontCourtPlay(game: Game): MutableList<BasketballPlay> {
        with (game) {
            consecutivePresses = 1
            val shotUrgency: Int = if (homeTeamHasBall) {
                Game.lengthOfHalf / homeTeam.pace
            } else {
                Game.lengthOfHalf / awayTeam.pace
            }

            madeShot = false
            return when {
                location == 1 && shotClock < (Game.lengthOfShotClock - shotUrgency) -> getShot(game)
                location == 1 && Random.nextDouble() > 0.7 -> getShot(game)
                shotClock <= 5 && Random.nextDouble() > 0.05 -> getShot(game)
                lastPassWasGreat -> getShot(game)
                else -> getPass(game)
            }
        }
    }

    private fun getShot(game: Game): MutableList<BasketballPlay> {
        with(game) {
            val plays = mutableListOf<BasketballPlay>()
            val shot = getShotOrPostMove(game).also { plays.add(it) }
            location = 1
            if (homeTeamHasBall) {
                homeScore += shot.points
            } else {
                awayScore += shot.points
            }
            updateTimeRemaining(shot)
            resetShotClock()
            val rebound = getRebound(game, shot)
            if (rebound == null) {
                if (!getFreeThrows(game, shot)) {
                    // made shot and no free throws
                    madeShot = true
                    deadball = true
                    changePossession()
                } else {
                    madeShot = false
                    deadball = true
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
                shot.points != 0 -> game.numberOfFreeThrows = 1
                shot.foul.foulType == FoulType.SHOOTING_LONG -> game.numberOfFreeThrows = 3
                else -> game.numberOfFreeThrows = 2
            }
            game.shootFreeThrows = true
            return true
        }
        return false
    }

    private fun getPass(game: Game): MutableList<BasketballPlay> {
        with(game) {
            val pass = Pass(game)
            updateTimeRemaining(pass)
            playerWithBall = pass.playerWithBall
            lastPassWasGreat = pass.isGreatPass
            location = pass.location
            deadball = false

            if (pass.homeTeamHasBall != pass.homeTeamStartsWithBall) {
                changePossession()
            }

            val plays = mutableListOf<BasketballPlay>(pass)
            if (timeRemaining == 0 && half != 1 && plays.last().foul.foulType == FoulType.CLEAN) {
                if (homeTeamHasBall && homeScore < awayScore && homeScore + 3 >= awayScore) {
                    // Buzzer beater for tie / win
                    plays.add(Shot(game, lastPassWasGreat, getPasser(game), true))
                } else if (!homeTeamHasBall && awayScore < homeScore && awayScore + 3 >= homeScore) {
                    plays.add(Shot(game, lastPassWasGreat, getPasser(game), true))
                }
            } else {
                FoulHelper.manageFoul(game, pass.foul)
                ClockViolationHelper.getShotClockViolation(this, pass)
            }

            return plays
        }
    }

    private fun getPasser(game: Game): Player {
        with (game) {
            return if (homeTeamHasBall) {
                homeTeam.getPlayerAtPosition(lastPlayerWithBall)
            } else {
                awayTeam.getPlayerAtPosition(lastPlayerWithBall)
            }
        }
    }

    private fun getShotOrPostMove(game: Game): BasketballPlay {
        with(game) {
            val shooter = if (homeTeamHasBall) {
                homeTeam.getPlayerAtPosition(playerWithBall)
            } else {
                awayTeam.getPlayerAtPosition(playerWithBall)
            }
            val positionChanceMod = when (shooter.courtIndex) {
                4, 5 -> 15
                3 -> 45
                2 -> 55
                else -> 65
            }
            return if (Random.nextInt(100) + positionChanceMod < shooter.postMove) {
                PostMove(game, lastPassWasGreat, getPasser(game))
            } else {
                Shot(game, lastPassWasGreat, getPasser(game), shotClock <= 5)
            }
        }
    }
}