package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

class TenTeamTournament(
    private val id: Int,
    teams: List<Team>,
    val dataModels: List<StandingsDataModel>
) : Tournament {

    private val sortedTeams = mutableListOf<Team>()
    private val games = mutableListOf<Game>()
    private val scheduleDataModels = MutableList(9) {
        val round = getRoundForGameIndex(it)
        TournamentDataModel.emptyDataModel(round, round == 1)
    }

    init {
        dataModels.sortedWith(
            compareBy(
                { -it.getConferenceWinPercentage() },
                { -it.conferenceWins },
                { -it.getWinPercentage() },
                { -it.totalWins }
            )
        ).forEach { dataModel -> sortedTeams.add(teams.first { it.teamId == dataModel.teamId }) }
    }

    override fun getScheduleDataModels() = scheduleDataModels

    override fun generateNextRound(season: Int): List<Game> {
        val newGames = mutableListOf<Game>()

        val gamesSize = games.size
        val finalGamesSize = games.filter { it.isFinal }.size
        when {
            gamesSize == 0 -> {
                newGames.add(NewGameHelper.newGame(sortedTeams[7], sortedTeams[8], season, id))
                newGames.add(NewGameHelper.newGame(sortedTeams[6], sortedTeams[9], season, id))
            }
            finalGamesSize == 1 && gamesSize == 2 -> {
                newGames.add(NewGameHelper.newGame(sortedTeams[0], getWinner(games[0]), season, id))
                newGames.add(NewGameHelper.newGame(sortedTeams[3], sortedTeams[4], season, id))
                newGames.add(NewGameHelper.newGame(sortedTeams[2], sortedTeams[5], season, id))
            }
            finalGamesSize == 2 && gamesSize == 5 -> {
                newGames.add(NewGameHelper.newGame(sortedTeams[1], getWinner(games[1]), season, id))
            }
            finalGamesSize == 4 && gamesSize == 6 -> {
                newGames.add(NewGameHelper.newGame(getWinner(games[2]), getWinner(games[3]), season, id))
            }
            finalGamesSize == 6 && gamesSize == 7 -> {
                newGames.add(NewGameHelper.newGame(getWinner(games[5]), getWinner(games[4]), season, id))
            }
            finalGamesSize == 8 && gamesSize == 8 -> {
                newGames.add(NewGameHelper.newGame(getWinner(games[6]), getWinner(games[7]), season, id))
            }
        }
        games.addAll(newGames)
        if (games.size == 2) makeInitialDataModels() else updateDataModels()
        return newGames
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
        check(games.size <= 9) { "More than 9 games in 10 team tournament! Total games: ${games.size}" }
        if (games.size == 2) makeInitialDataModels() else updateDataModels()
    }

    override fun getId() = id

    override fun getGames() = games

    private fun updateDataModels() {
        games.forEachIndexed { index, game ->
            val round = getRoundForGameIndex(index)
            scheduleDataModels[index] = TournamentDataModel.from(game, round, round == 1)
        }
    }

    private fun makeInitialDataModels() {
        updateDataModels()
        scheduleDataModels[2].homeTeamName = sortedTeams[0].abbreviation
        scheduleDataModels[3].homeTeamName = sortedTeams[3].abbreviation
        scheduleDataModels[4].homeTeamName = sortedTeams[2].abbreviation
        scheduleDataModels[5].homeTeamName = sortedTeams[1].abbreviation
    }

    private fun getRoundForGameIndex(index: Int): Int {
        return when (index) {
            0, 1 -> 1
            in 2..5 -> 2
            6, 7 -> 3
            else -> 4
        }
    }

    private fun getWinner(game: Game): Team {
        return if (game.homeScore > game.awayScore) game.homeTeam else game.awayTeam
    }
}
