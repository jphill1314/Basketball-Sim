package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import com.appdev.jphil.basketball.textcontracts.FreeThrowTextContract

class FreeThrows(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    foulText: FoulTextContract,
    private val numberOfShots: Int,
    private val ftText: FreeThrowTextContract
) :
    BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, foulText) {

    var madeLastShot = true

    init {
        type = Plays.FREE_THROW
        this.location = 1
        foul = Foul(
            homeTeamHasBall,
            timeRemaining,
            shotClock,
            homeTeam,
            awayTeam,
            playerWithBall,
            location,
            foulText,
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
            playAsString = ftText.freeThrowText(shooter, made, numberOfShots)
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
            playAsString = ftText.oneAndOneText(shooter, made)
        }
        return made
    }
}