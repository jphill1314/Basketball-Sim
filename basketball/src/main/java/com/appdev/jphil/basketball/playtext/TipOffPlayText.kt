package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.TipOffTextContract

class TipOffPlayText : TipOffTextContract {

    override fun tipOffText(home: Team, away: Team, homeTeamHasBall: Boolean): String {
        return if (homeTeamHasBall) {
            val center = home.getPlayerAtPosition(5)
            "${center.fullName} wins the tip off! ${home.name} will start the game with the ball."
        } else {
            val center = away.getPlayerAtPosition(5)
            "${center.fullName} wins the tip off! ${away.name} will start the game with the ball."
        }
    }
}