package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.playtext.TipOffPlayText
import com.appdev.jphil.basketball.textcontracts.TipOffTextContract

class TipOff(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    private val tipOffText: TipOffTextContract = TipOffPlayText()
) : BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    init {
        type = Plays.TIP_OFF

        generatePlay()
    }

    override fun generatePlay(): Int {
        val homeCenter = homeTeam.getPlayerAtPosition(5)
        val awayCenter = awayTeam.getPlayerAtPosition(5)

        homeTeamHasBall = homeCenter.rebounding + r.nextInt(randomBound) > awayCenter.rebounding + r.nextInt(randomBound)
        playerWithBall = r.nextInt(5) + 1
        playAsString = tipOffText.tipOffText(homeTeam, awayTeam, homeTeamHasBall)

        return 0
    }
}