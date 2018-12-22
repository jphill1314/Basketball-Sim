package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team


class Shot(homeTeamHasBall: Boolean, timeRemaining: Int, shotClock: Int, homeTeam: Team, awayTeam: Team, playerWithBall: Int, location: Int, val assisted: Boolean, val rushed: Boolean) :
        BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    init {
        super.type = Plays.SHOT
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        val defender = defense.getPlayerAtPosition(playerWithBall)

        // First determine where the shot will be taken from
        val shotClose = (shooter.closeRangeShot * (1 - (offense.offenseFavorsThrees / 100.0)) * (1 - (defense.defenseFavorsThrees / 100.0)) + r.nextInt(randomBound))
        val shotMid = (shooter.midRangeShot * .2 + r.nextInt(randomBound))
        val shotLong = (shooter.longRangeShot * (offense.offenseFavorsThrees / 100.0) * (defense.defenseFavorsThrees / 100.0) + r.nextInt(randomBound))

        playAsString = "${shooter.fullName} takes a shot from "
        val shotLocation: Int = if (location == 0) {
            // shot taken from around half court
            playAsString += "near half court"
            offense.threePointAttempts++
            shooter.threePointAttempts++
            4
        } else if (location == -1) {
            // shot taken from beyond half court
            playAsString += "well beyond half court"
            offense.threePointAttempts++
            shooter.threePointAttempts++
            5
        } else if (shotClose > shotMid && shotClose > shotLong) {
            // shot taken from close range
            playAsString += "close range"
            offense.twoPointAttempts++
            shooter.twoPointAttempts++
            1
        } else if (shotMid > shotClose && shotMid > shotLong) {
            // shot taken from mid range
            playAsString += "mid range"
            offense.twoPointAttempts++
            shooter.twoPointAttempts++
            2
        } else {
            // shot taken from 3
            playAsString += "three"
            offense.threePointAttempts++
            shooter.threePointAttempts++
            3
        }

        var shotSuccess: Int = getShotSuccess(shotLocation, shooter, defender)

        if (assisted) {
            shotSuccess += 30
        }
        if (rushed) {
            shotSuccess -= 30
        }

        foul = when (shotLocation) {
            1 -> Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.SHOOTING_CLOSE)
            2 -> Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.SHOOTING_MID)
            else -> Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.SHOOTING_LONG)
        }

        if (foul.foulType != FoulType.CLEAN) {
            type = Plays.FOUL
            shotSuccess -= 30
        }

        val timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange
        return getMadeShot(shotLocation, shotSuccess, shooter)
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

    private fun getMadeShot(shotLocation: Int, shotSuccess: Int, shooter: Player): Int{
        return when (shotLocation) {
            // 2 point shot
            1 -> {
                if (shotSuccess > ((r.nextDouble() * shooter.closeRangeShot) * (r.nextDouble() * 5))) {
                    playAsString += " and makes it!"
                    if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                    }
                    else{
                        playAsString += " ${foul.playAsString} ${shooter.fullName} will get a chance at a three point play!"
                    }
                    offense.twoPointMakes++
                    shooter.twoPointMakes++
                    2
                } else {
                    playAsString += " and misses it!"
                    if(type == Plays.FOUL){
                        playAsString += " But ${foul.playAsString} ${shooter.fullName} will instead shoot free throws."
                        offense.twoPointAttempts--
                        shooter.twoPointAttempts--
                    }
                    0
                }
            }
            2 -> {
                if (shotSuccess > ((r.nextDouble() * shooter.midRangeShot) * (r.nextDouble() * 5))) {
                    playAsString += " and makes it!"
                    if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                    }
                    else{
                        playAsString += " ${foul.playAsString} ${shooter.fullName} will get a chance at a three point play!"
                    }
                    offense.twoPointMakes++
                    shooter.twoPointMakes++
                    2
                } else {
                    playAsString += " and misses it!"
                    if(type == Plays.FOUL){
                        playAsString += " But ${foul.playAsString} ${shooter.fullName} will instead shoot free throws."
                        offense.twoPointAttempts--
                        shooter.twoPointAttempts--
                    }
                    0
                }
            }
            3 -> {
                if (shotSuccess > ((r.nextDouble() * shooter.longRangeShot) * (r.nextDouble() * 6))) {
                    playAsString += " and makes it!"
                    if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                    }
                    else{
                        playAsString += " ${foul.playAsString} ${shooter.fullName} will get a chance at a four point play!"
                    }
                    offense.threePointMakes++
                    shooter.threePointMakes++
                    3
                } else {
                    playAsString += " and misses it!"
                    if(type == Plays.FOUL){
                        playAsString += " But ${foul.playAsString} ${shooter.fullName} will instead shoot free throws."
                        offense.threePointAttempts--
                        shooter.threePointAttempts--
                    }
                    0
                }
            }
            4 -> {
                if (r.nextInt(100) < 10) {
                    playAsString += " and makes it!"
                    if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                    }
                    else{
                        playAsString += " ${foul.playAsString} ${shooter.fullName} will get a chance at a four point play!"
                    }
                    offense.threePointMakes++
                    shooter.threePointMakes++
                    3
                } else {
                    playAsString += " and misses it!"
                    if(type == Plays.FOUL){
                        playAsString += " But ${foul.playAsString} ${shooter.fullName} will instead shoot free throws."
                        offense.threePointAttempts--
                        shooter.threePointAttempts--
                    }
                    0
                }
            }
            else -> {
                if (r.nextInt(100) < 2) {
                    playAsString += " and makes it!"
                    if (type != Plays.FOUL) {
                        homeTeamHasBall = !homeTeamHasBall
                    }
                    else{
                        playAsString += " ${foul.playAsString} ${shooter.fullName} will get a chance at a four point play!"
                    }
                    offense.threePointMakes++
                    shooter.threePointMakes++
                    3
                } else {
                    playAsString += " and misses it!"
                    if(type == Plays.FOUL){
                        playAsString += " But ${foul.playAsString} ${shooter.fullName} will instead shoot free throws."
                        offense.threePointAttempts--
                        shooter.threePointAttempts--
                    }
                    0
                }
            }
        }
    }
}