package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

class TenTeamTournament(
    private val id: Int,
    teams: List<Team>,
    val dataModels: List<StandingsDataModel>
): Tournament {

    private val sortedTeams = mutableListOf<Team>()
    private val games = mutableListOf<Game>()
    private val scheduleDataModels = MutableList(9) {
        val round = getRoundForGameIndex(it)
        TournamentDataModel.emptyDataModel(round, round == 1)
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
            if (games.size == 2) makeInitialDataModels() else updateDataModels()
        }
    }

    override fun getWinnerOfTournament(): Team? {
        return if (games.size == 9 && games.last().isFinal) {
            getWinner(games.last())
        } else {
            null
        }
    }

    override fun replaceGames(newGames: List<Game>) {
        games.clear()
        games.addAll(newGames)
        if (games.size == 2) makeInitialDataModels() else updateDataModels()
    }

    override fun getAllGames() = games

    override fun numberOfGames() = games.size

    override fun getId() = id

    private fun updateDataModels() {
        games.forEachIndexed { index, game ->
            val round = getRoundForGameIndex(index)
            scheduleDataModels[index] = TournamentDataModel.from(game, round, round == 1)
        }
    }

    private fun makeInitialDataModels() {
        updateDataModels()
        scheduleDataModels[2].homeTeamName = sortedTeams[0].name
        scheduleDataModels[3].homeTeamName = sortedTeams[3].name
        scheduleDataModels[4].homeTeamName = sortedTeams[2].name
        scheduleDataModels[5].homeTeamName = sortedTeams[1].name
    }

    private fun getRoundForGameIndex(index: Int): Int {
        return when (index) {
            0, 1 -> 1
            in 2..5 ->  2
            6, 7 -> 3
            else -> 4
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