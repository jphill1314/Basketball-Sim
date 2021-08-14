package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.teams.Team

interface TipOffTextContract {

    fun tipOffText(home: Team, away: Team, homeTeamHasBall: Boolean): String
}
