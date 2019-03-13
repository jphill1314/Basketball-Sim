package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.playtext.ShotPlayText
import com.appdev.jphil.basketball.textcontracts.ShotTextContract


class Shot(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    val assisted: Boolean, // TODO: when adding assists -> need a way to know which player passed the ball
    val rushed: Boolean,
    private val shotText: ShotTextContract = ShotPlayText()
) :
    BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    init {
        super.type = Plays.SHOT
        foul = Foul(
            homeTeamHasBall,
            timeRemaining,
            shotClock,
            homeTeam,
            awayTeam,
            playerWithBall,
            location,
            FoulType.CLEAN
        )
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        val defender = defense.getPlayerAtPosition(playerWithBall)

        // First determine where the shot will be taken from
        val shotClose =
            (shooter.closeRangeShot * (1 - (offense.offenseFavorsThrees / 100.0)) * (1 - (defense.defenseFavorsThrees / 100.0)) + r.nextInt(
                randomBound
            ))
        val shotMid = (shooter.midRangeShot * .2 + r.nextInt(randomBound))
        val shotLong =
            (shooter.longRangeShot * (offense.offenseFavorsThrees / 100.0) * (defense.defenseFavorsThrees / 100.0) + r.nextInt(
                randomBound
            ))

        val shotLocation: Int = getShotLocation(shooter, shotClose, shotMid, shotLong)

        var shotSuccess: Int = getShotSuccess(shotLocation, shooter, defender)

        if (assisted) {
            shotSuccess += 30
        }
        if (rushed) {
            shotSuccess -= 30
        }

        val foulType = when (shotLocation) {
            1 -> FoulType.SHOOTING_CLOSE
            2 -> FoulType.SHOOTING_MID
            else -> FoulType.SHOOTING_LONG
        }

        foul = Foul(
                homeTeamHasBall,
                timeRemaining,
                shotClock,
                homeTeam,
                awayTeam,
                playerWithBall,
                location,
                foulType
        )

        if (foul.foulType != FoulType.CLEAN) {
            type = Plays.FOUL
            shotSuccess -= 30
        }

        val timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange
        return getMadeShot(shotLocation, shotSuccess, shooter)
    }

    private fun getShotLocation(shooter: Player, shotClose: Double, shotMid: Double, shotLong: Double): Int {
        return if (location == 0) {
            // shot taken from around half court
            offense.threePointAttempts++
            shooter.threePointAttempts++
            4
        } else if (location == -1) {
            // shot taken from beyond half court
            offense.threePointAttempts++
            shooter.threePointAttempts++
            5
        } else if (shotClose > shotMid && shotClose > shotLong) {
            // shot taken from close range
            offense.twoPointAttempts++
            shooter.twoPointAttempts++
            1
        } else if (shotMid > shotClose && shotMid > shotLong) {
            // shot taken from mid range
            offense.twoPointAttempts++
            shooter.twoPointAttempts++
            2
        } else {
            // shot taken from 3
            offense.threePointAttempts++
            shooter.threePointAttempts++
            3
        }
    }

    private fun getShotSuccess(shotLocation: Int, shooter: Player, defender: Player): Int {
        return when (shotLocation) {
            1 -> {
                if (defender.onBallDefense + defender.postDefense > r.nextInt(200 * 2)) {
                    shooter.closeRangeShot - 10
                } else {
                    shooter.closeRangeShot
                }
            }
            2 -> {
                if (defender.onBallDefense + defender.postDefense + defender.perimeterDefense > r.nextInt(300 * 4)) {
                    shooter.midRangeShot - 10
                } else {
                    shooter.midRangeShot
                }
            }
            3 -> {
                if (defender.onBallDefense + defender.perimeterDefense > r.nextInt(200 * 4)) {
                    shooter.longRangeShot - 10
                } else {
                    shooter.longRangeShot
                }
            }
            4 -> {
                if (defender.onBallDefense + defender.perimeterDefense > r.nextInt(200 * 8)) {
                    shooter.longRangeShot - 20
                } else {
                    shooter.longRangeShot - 10
                }
            }
            5 -> {
                if (defender.onBallDefense + defender.perimeterDefense > r.nextInt(200 * 8)) {
                    shooter.longRangeShot - 30
                } else {
                    shooter.longRangeShot - 20
                }
            }
            else -> 0
        }
    }

    private fun getMadeShot(shotLocation: Int, shotSuccess: Int, shooter: Player): Int {
        return when (shotLocation) {
            // 2 point shot
            1 -> {
                if (shotSuccess > ((r.nextDouble() * shooter.closeRangeShot) * (r.nextDouble() * 5))) {
                    playAsString = if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                        shotText.shortMake(shooter)
                    } else {
                        shotText.shortFoul(shooter, foul.fouler!!, true)
                    }
                    offense.twoPointMakes++
                    shooter.twoPointMakes++
                    2
                } else {
                    playAsString = if (type != Plays.FOUL) {
                        shotText.shortMiss(shooter)
                    } else {
                        offense.twoPointAttempts--
                        shooter.twoPointAttempts--
                        shotText.shortFoul(shooter, foul.fouler!!, false)
                    }
                    0
                }
            }
            2 -> {
                if (shotSuccess > ((r.nextDouble() * shooter.midRangeShot) * (r.nextDouble() * 5))) {
                    playAsString = if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                        shotText.midMake(shooter)
                    } else {
                        shotText.midFoul(shooter, foul.fouler!!, true)
                    }
                    offense.twoPointMakes++
                    shooter.twoPointMakes++
                    2
                } else {
                    playAsString = if (type != Plays.FOUL) {
                        shotText.midMiss(shooter)
                    } else {
                        offense.twoPointAttempts--
                        shooter.twoPointAttempts--
                        shotText.midFoul(shooter, foul.fouler!!, false)
                    }
                    0
                }
            }
            3 -> {
                if (shotSuccess > ((r.nextDouble() * shooter.longRangeShot) * (r.nextDouble() * 6))) {
                    playAsString = if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                        shotText.longMake(shooter)
                    } else {
                        shotText.longFoul(shooter, foul.fouler!!, true)
                    }
                    offense.threePointMakes++
                    shooter.threePointMakes++
                    3
                } else {
                    playAsString = if (type != Plays.FOUL) {
                        shotText.longMiss(shooter)
                    } else {
                        offense.threePointAttempts--
                        shooter.threePointAttempts--
                        shotText.longFoul(shooter, foul.fouler!!, false)
                    }
                    0
                }
            }
            4 -> {
                if (r.nextInt(100) < 10) {
                    playAsString = if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                        shotText.halfCourtMake(shooter)
                    } else {
                        shotText.halfCourtFoul(shooter, foul.fouler!!, true)
                    }
                    offense.threePointMakes++
                    shooter.threePointMakes++
                    3
                } else {
                    playAsString = if (type != Plays.FOUL) {
                        shotText.halfCourtMiss(shooter)
                    } else {
                        offense.threePointAttempts--
                        shooter.threePointAttempts--
                        shotText.halfCourtFoul(shooter, foul.fouler!!, false)
                    }
                    0
                }
            }
            else -> {
                if (r.nextInt(100) < 2) {
                    playAsString = if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                        shotText.beyondHalfCourtMake(shooter)
                    } else {
                        shotText.beyondHalfCourtFoul(shooter, foul.fouler!!, true)
                    }
                    offense.threePointMakes++
                    shooter.threePointMakes++
                    3
                } else {
                    playAsString = if (type != Plays.FOUL) {
                        shotText.beyondHalfCourtMiss(shooter)
                    } else {
                        offense.threePointAttempts--
                        shooter.threePointAttempts--
                        shotText.beyondHalfCourtFoul(shooter, foul.fouler!!, false)
                    }
                    0
                }
            }
        }
    }
}