package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

object NewGameHelper {
    fun newGame(homeTeam: Team, awayTeam: Team, season: Int, id: Int): Game {
        return Game(
            homeTeam,
            awayTeam,
            true,
            season,
            false,
            null,
            id,
            false
        )
    }
}