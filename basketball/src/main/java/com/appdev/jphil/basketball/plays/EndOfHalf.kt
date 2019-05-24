package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import com.appdev.jphil.basketball.textcontracts.MiscTextContract

class EndOfHalf(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    foulText: FoulTextContract,
    miscText: MiscTextContract,
    half: Int,
    gameOver: Boolean
) : BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, foulText) {

    init {
        playAsString = miscText.endOfHalf(half, gameOver)
    }

    override fun generatePlay(): Int {
        return 0
    }
}