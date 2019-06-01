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

            val plays = mutableListOf<BasketballPlay>()

            madeShot = false
            if (((shotClock < (Game.lengthOfShotClock - shotUrgency) || Random.nextDouble() > 0.7) && location == 1) || (shotClock <= 5 && Random.nextDouble() > 0.05) || lastPassWasGreat) {
                plays.add(getShotOrPostMove(this))
                val shot = plays[0]
                if ( shot.points == 0 && shot.foul.foulType == FoulType.CLEAN) {
                    // missed shot need to get a rebound
                    plays.add(Rebound(this))
                    deadball = false
                } else if (shot.foul.foulType != FoulType.CLEAN) {
                    shootFreeThrows = true
                    if (shot.points != 0) {
                        addPoints(shot.points)
                        numberOfFreeThrows = 1
                    }
                    else if (shot.foul.foulType == FoulType.SHOOTING_LONG) {
                        numberOfFreeThrows = 3
                    }
                    else {
                        numberOfFreeThrows = 2
                    }

                    deadball = true
                } else {
                    // made shot
                    madeShot = true
                    deadball = true
                    if (homeTeamHasBall) {
                        homeScore += shot.points
                    }
                    else {
                        awayScore += shot.points
                    }
                }
            }
            else {
                plays.add(Pass(this))
                deadball = false

                if (plays.last().timeRemaining == 0 && half != 1) {
                    if (homeTeamHasBall && homeScore < awayScore && homeScore + 3 >= awayScore) {
                        // Buzzer beater for tie / win
                        plays.add(Shot(this, lastPassWasGreat,
                            getPasser(this), true))
                    } else if (!homeTeamHasBall && awayScore < homeScore && awayScore + 3 >= homeScore) {
                        plays.add(Shot(this, lastPassWasGreat,
                            getPasser(this), true))
                    }
                }
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
                PostMove(this, lastPassWasGreat,
                    getPasser(this)
                )
            } else {
                Shot(this, lastPassWasGreat,
                    getPasser(this), shotClock <= 5)
            }
        }
    }
}