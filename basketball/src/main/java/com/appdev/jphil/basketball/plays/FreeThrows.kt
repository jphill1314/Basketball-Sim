package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Team

class FreeThrows(homeTeamHasBall: Boolean, timeRemaining: Int, shotClock: Int, homeTeam: Team, awayTeam: Team, playerWithBall: Int, location: Int, val numberOfShots: Int) :
        BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    var madeLastShot = true

    init {
        type = Plays.FREE_THROW
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.CLEAN)
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
                    if(i == numberOfShots){
                        homeTeamHasBall = !homeTeamHasBall
                    }
                } else if (i == numberOfShots) {
                    madeLastShot = false
                }
            }
            playAsString = if (numberOfShots > 1) {
                if (made == 0) {
                    "${shooter.fullName} misses all $numberOfShots of his free throws!"
                } else {
                    "${shooter.fullName} makes $made of $numberOfShots free throws."
                }
            } else {
                if (made == 1) {
                    "${shooter.fullName} makes his free throw."
                } else {
                    "${shooter.fullName} misses his free throw."
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
            playAsString = if (made == 0) {
                "${shooter.fullName} misses the front end of the 1 and 1."
            } else if (made == 1) {
                "${shooter.fullName} makes his first shot, but misses the second."
            } else {
                "${shooter.fullName} makes both shots from the 1 and 1."
            }
        }
        return made
    }
}