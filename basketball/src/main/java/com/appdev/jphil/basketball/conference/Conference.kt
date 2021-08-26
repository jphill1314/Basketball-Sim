package com.appdev.jphil.basketball.conference

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.tournament.EightTeamTournament
import com.appdev.jphil.basketball.tournament.TenTeamTournament
import com.appdev.jphil.basketball.tournament.Tournament
import com.appdev.jphil.basketball.tournament.TournamentType

class Conference(
    val id: Int,
    val name: String,
    val teams: List<Team>
) {

    var tournament: Tournament? = null

    // TODO: make this more flexible
    val tournamentType = when (teams.size) {
        8 -> TournamentType.EIGHT
        else -> TournamentType.TEN
    }

    fun generateSchedule(season: Int): List<Game> {
        val games = mutableListOf<Game>()

        for (x in teams.indices) {
            for (y in teams.indices) {
                if (x != y) {
                    games.add(Game(teams[x], teams[y], false, season, true))
                }
            }
        }

        return games
    }

    fun generateTournament(dataModels: List<StandingsDataModel>) {
        if (tournament == null) {
            tournament = when (teams.size) {
                8 -> EightTeamTournament(id, teams, dataModels)
                10 -> TenTeamTournament(id, teams, dataModels)
                else -> throw IllegalStateException("Can't make a conference with ${teams.size} teams!")
            }
        }
    }
}
