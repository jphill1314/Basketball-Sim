package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.FoulTextContract

class IntentionalFoul(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    foulText: FoulTextContract
) : BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, foulText) {

    init {
        generatePlay()
    }

    override fun generatePlay(): Int {
        foul = Foul(
            homeTeamHasBall,
            timeRemaining,
            shotClock,
            homeTeam,
            awayTeam,
            playerWithBall,
            location,
            foulText,
            FoulType.INTENTIONAL
        )

        val timeChange = timeUtil.smartTimeChange(r.nextInt(5), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange
        playAsString = foul.playAsString

        return 0
    }
}