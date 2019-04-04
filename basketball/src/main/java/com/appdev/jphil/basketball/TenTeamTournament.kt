package com.appdev.jphil.basketball

import com.appdev.jphil.basketball.game.Game

class TenTeamTournament(
    val id: Int,
    teams: List<Team>,
    dataModels: List<StandingsDataModel>
) {

    private val sortedTeams = mutableListOf<Team>()
    val games = mutableListOf<Game>()

    init {
        dataModels.sortedWith(compareBy(
            { -it.getConferenceWinPercentage() },
            { -it.conferenceWins },
            { -it.getWinPercentage() },
            { -it.totalWins }
        )).forEach { dataModel ->
            val team = teams.filter { it.teamId == dataModel.teamId }
            sortedTeams.add(team[0])
        }
    }

    fun generateNextRound(season: Int) {
        if (games.filter { it.isFinal }.size == games.size) {
            when (games.size) {
                0 -> {
                    games.add(newGame(sortedTeams[7], sortedTeams[8], season))
                    games.add(newGame(sortedTeams[6], sortedTeams[9], season))
                }
                2 -> {
                    games.add(newGame(sortedTeams[0], getWinner(games[0]), season))
                    games.add(newGame(sortedTeams[3], sortedTeams[4], season))
                    games.add(newGame(sortedTeams[2], sortedTeams[5], season))
                    games.add(newGame(sortedTeams[1], getWinner(games[1]), season))
                }
                6 -> {
                    games.add(newGame(getWinner(games[2]), getWinner(games[3]), season))
                    games.add(newGame(getWinner(games[5]), getWinner(games[4]), season))
                }
                8 -> {
                    games.add(newGame(getWinner(games[6]), getWinner(games[7]), season))
                }
            }
        }
    }

    fun getWinnerOfTournament(): Team? {
        return if (games.size == 9 && games[9].isFinal) {
            getWinner(games[9])
        } else {
            null
        }
    }

    private fun newGame(homeTeam: Team, awayTeam: Team, season: Int): Game {
        return Game(
            homeTeam,
            awayTeam,
            true,
            season,
            null,
            id,
            false
        )
    }

    private fun getWinner(game: Game): Team {
        return if (game.homeScore > game.awayScore) game.homeTeam else game.awayTeam
    }
}