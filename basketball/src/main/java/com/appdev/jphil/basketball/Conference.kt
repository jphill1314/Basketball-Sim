package com.appdev.jphil.basketball

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

class Conference(
    val id: Int,
    val name: String,
    val teams: List<Team>
) {

    var tournament: TenTeamTournament? = null

    fun generateSchedule(season: Int): List<Game> {
        val games = mutableListOf<Game>()

        for (x in teams.indices) {
            for (y in teams.indices) {
                if (x != y) {
                    games.add(Game(teams[x], teams[y], false, season))
                }
            }
        }

        return games
    }

    fun generateTournament(dataModels: List<StandingsDataModel>) {
        if (tournament == null) {
            tournament = TenTeamTournament(id, teams, dataModels)
        }
    }
}