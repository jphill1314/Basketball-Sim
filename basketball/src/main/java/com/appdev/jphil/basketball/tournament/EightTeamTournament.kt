package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

class EightTeamTournament(
    private val id: Int,
    teams: List<Team>,
    val dataModels: List<StandingsDataModel>
): Tournament {
    private val sortedTeams = mutableListOf<Team>()
    private val games = mutableListOf<Game>()
    private val scheduleDataModels = MutableList(7) {
        TournamentDataModel.emptyDataModel(getRoundForGameIndex(it), false)
    }

    init {
        dataModels.sortedWith(compareBy(
            { -it.getConferenceWinPercentage() },
            { -it.conferenceWins },
            { -it.getWinPercentage() },
            { -it.totalWins }
        )).forEach { dataModel -> sortedTeams.add(teams.first { it.teamId == dataModel.teamId }) }
    }

    override fun getScheduleDataModels() = scheduleDataModels

    override fun generateNextRound(season: Int) {
        if (games.filter { it.isFinal }.size == games.size) {
            when (games.size) {
                0 -> {
                    games.add(newGame(sortedTeams[0], sortedTeams[7], season))
                    games.add(newGame(sortedTeams[1], sortedTeams[6], season))
                    games.add(newGame(sortedTeams[2], sortedTeams[5], season))
                    games.add(newGame(sortedTeams[3], sortedTeams[4], season))
                }
                4 -> {
                    games.add(newGame(getWinner(games[0]), getWinner(games[1]), season))
                    games.add(newGame(getWinner(games[3]), getWinner(games[2]), season))
                }
                6 -> games.add(newGame(getWinner(games[4]), getWinner(games[5]), season))
            }
            updateDataModels()
        }
    }

    override fun getWinnerOfTournament(): Team? {
        return if (games.size == 7 && games.last().isFinal) {
            getWinner(games.last())
        } else {
            null
        }
    }

    override fun replaceGames(newGames: List<Game>) {
        games.clear()
        games.addAll(newGames)
        updateDataModels()
    }

    override fun getAllGames() = games

    override fun numberOfGames() = games.size

    override fun getId() = id

    private fun updateDataModels() {
        games.forEachIndexed { index, game ->
            scheduleDataModels[index] = TournamentDataModel.from(game, getRoundForGameIndex(index), false)
        }
    }

    private fun getRoundForGameIndex(index: Int): Int {
        return when (index) {
            in 0..3 -> 1
            4, 5 -> 2
            else -> 3
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