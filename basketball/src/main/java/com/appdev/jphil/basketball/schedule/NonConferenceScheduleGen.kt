package com.appdev.jphil.basketball.schedule

import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

object NonConferenceScheduleGen {

    fun generateNonConferenceSchedule(
        conferences: List<Conference>,
        numberOfNonConGames: Int,
        season: Int
    ): MutableList<Game> {
        val games = mutableListOf<Game>()
        val teams = mutableListOf<GenDataModel>()

        conferences.forEach { conf -> conf.teams.forEach { team -> teams.add(GenDataModel(team)) } }

        val totalTeams = teams.size
        var loops = 0
        while (games.size < totalTeams * numberOfNonConGames / 2 && loops++ < 1000) {
            val homeTeam = teams.random()
            val awayTeam = teams.random()

            if (homeTeam.hasValidOpponent(awayTeam)) {
                games.add(Game(homeTeam.team, awayTeam.team, false, season))
                homeTeam.opponents.add(awayTeam)
                awayTeam.opponents.add(homeTeam)

                if (homeTeam.opponents.size >= numberOfNonConGames) {
                    teams.remove(homeTeam)
                }
                if (awayTeam.opponents.size >= numberOfNonConGames) {
                    teams.remove(awayTeam)
                }
            }
        }
        return games
    }

    private data class GenDataModel(val team: Team) {
        val opponents = mutableListOf<GenDataModel>()
        fun hasValidOpponent(other: GenDataModel) =
            this != other && team.conferenceId != other.team.conferenceId && !opponents.contains(other)
    }
}