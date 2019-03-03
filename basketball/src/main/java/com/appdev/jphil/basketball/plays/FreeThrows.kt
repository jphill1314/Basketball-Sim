package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Team

class FreeThrows(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    private val numberOfShots: Int
) :
    BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    var madeLastShot = true

    init {
        type = Plays.FREE_THROW
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
        var made = 0
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        if (numberOfShots > 0) {
            for (i in 1..numberOfShots) {
                offense.freeThrowShots++
                shooter.freeThrowShots++
                if (r.nextInt(100) < shooter.freeThrowShot) {
                    made++
                    offense.freeThrowMakes++
                    shooter.freeThrowMakes++
                    if (i == numberOfShots) {
                        homeTeamHasBall = !homeTeamHasBall
                    }
                } else if (i == numberOfShots) {
                    madeLastShot = false
                }
            }
            playAsString = when (numberOfShots) {
                1 -> {
                    when (made) {
                        0 -> "${shooter.firstName} misses his free throw"
                        else -> "${shooter.firstName} makes his free throw"
                    }
                }
                2 -> {
                    when (made) {
                        0 -> "${shooter.firstName} misses both of his free throws"
                        1 -> "${shooter.firstName} makes one of his free throws"
                        else -> "${shooter.firstName} makes both of his free throws"
                    }
                }
                else -> {
                    when (made) {
                        0 -> "${shooter.firstName} misses all $numberOfShots of his free throws"
                        else -> "${shooter.firstName} makes $made of $numberOfShots of his free throws"
                    }
                }
            }
        } else {
            // 1 and 1 situation
            if (r.nextInt(100) < shooter.freeThrowShot) {
                made++
                offense.freeThrowShots++
                offense.freeThrowMakes++
                shooter.freeThrowShots++
                shooter.freeThrowMakes++
                if (r.nextInt(100) < shooter.freeThrowShot) {
                    made++
                    offense.freeThrowShots++
                    offense.freeThrowMakes++
                    shooter.freeThrowShots++
                    shooter.freeThrowMakes++
                    homeTeamHasBall = !homeTeamHasBall
                } else {
                    offense.freeThrowShots++
                    shooter.freeThrowShots++
                    madeLastShot = false
                }
            } else {
                offense.freeThrowShots++
                shooter.freeThrowShots++
                madeLastShot = false
            }
            playAsString = when (made) {
                0 -> "${shooter.fullName} misses the front end of the 1 and 1."
                1 -> "${shooter.fullName} makes his first shot, but misses the second."
                else -> "${shooter.fullName} makes both shots from the 1 and 1."
            }
        }
        return made
    }
}