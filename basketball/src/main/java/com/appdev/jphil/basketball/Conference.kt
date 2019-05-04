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

        for (x in 0 until teams.size) {
            for (y in 0 until teams.size) {
                if (x != y) {
                    games.add(Game(teams[x], teams[y], false, season))
                }
            }
        }

        games.shuffle()

        val maxSize = teams.size / 2
        val gameWeeks = mutableListOf(GameWeek(0, maxSize))
        var index = 1
        while (games.size > 0) {
            if (gameWeeks.last().isAtMaxSize() || index > games.size) {
                gameWeeks.add(GameWeek(gameWeeks.size, maxSize))
                index = 1
            }

            if (gameWeeks.last().addGame(games[games.size - index])) {
                games.remove(games[games.size - index])
            } else {
                index++
            }
        }

        gameWeeks.forEach { gameWeek ->
            gameWeek.getGames().forEach { game ->
                games.add(game)
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