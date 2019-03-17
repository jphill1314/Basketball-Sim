package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.textcontracts.FastBreakTextContract
import com.appdev.jphil.basketball.textcontracts.FoulTextContract

class FastBreak(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    foulText: FoulTextContract,
    private val fastBreakText: FastBreakTextContract
    ): BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, foulText) {

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
            foulText,
            FoulType.CLEAN
        )
        points = generatePlay()
    }

    // TODO: add chance for player to shoot a 3, or get fouled, or blocked, etc.
    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        val timeChange = timeUtil.smartTimeChange(r.nextInt(5), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange
        return if (r.nextInt(shooter.closeRangeShot) > 3) {
            playAsString = fastBreakText.madeShot(shooter)
            homeTeamHasBall = !homeTeamHasBall
            2
        } else {
            playAsString = fastBreakText.missedShot(shooter)
            0
        }
    }
}