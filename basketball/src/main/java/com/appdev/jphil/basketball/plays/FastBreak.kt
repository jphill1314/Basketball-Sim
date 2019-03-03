package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Team

class FastBreak(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int
    ): BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    init {
        type = Plays.SHOT
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

    // TODO: add chance for player to shoot a 3, or get fouled, or blocked, etc.
    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        playAsString = "${shooter.fullName} takes the layup off the fast break and "
        val timeChange = timeUtil.smartTimeChange(r.nextInt(5), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange
        return if (r.nextInt(shooter.closeRangeShot) > 3) {
            playAsString += "makes it."
            homeTeamHasBall = !homeTeamHasBall
            2
        } else {
            playAsString += "misses the wide open layup!"
            0
        }
    }
}