package com.appdev.jphil.basketball.schedule

import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team
import kotlin.random.Random

object NonConferenceScheduleGen {

    fun generateNonConferenceSchedule(
        conferences: List<Conference>,
        numberOfNonConGames: Int,
        season: Int
    ): MutableList<Game> {
        val games = mutableListOf<Game>()
        var teamsToGames = mutableMapOf<Team, Int>()

        conferences.forEachIndexed { index, conference ->
            if (conferences.size > index + 1) {
                conference.teams.forEach { team ->
                    val response = getGamesForTeam(
                        team,
                        teamsToGames,
                        conferences.drop(index + 1),
                        teamsToGames[team] ?: numberOfNonConGames,
                        numberOfNonConGames,
                        season
                    )
                    games.addAll(response.first)
                    teamsToGames = response.second
                }
            }
        }

        return games
    }

    private fun getGamesForTeam(
        team: Team,
        teamsToGames: MutableMap<Team, Int>,
        conferences: List<Conference>,
        newGames: Int,
        numberOfNonConGames: Int,
        season: Int
    ): Pair<MutableList<Game>, MutableMap<Team, Int>> {
        val games = mutableListOf<Game>()
        val opponents = mutableListOf<Team>()

        while (games.size < newGames) {
            val opponent = conferences.random().teams.random()
            if (!opponents.contains(opponent) && teamsToGames[opponent]?.compareTo(numberOfNonConGames) ?: -1 < 0) {
                games.add(if (Random.nextBoolean()) {
                    Game(team, opponent, false, season)
                } else {
                    Game(opponent, team, false, season)
                })
                teamsToGames[opponent] = (teamsToGames[opponent] ?: 0) + 1
                opponents.add(opponent)
            }
        }

        return Pair(games, teamsToGames)
    }

}