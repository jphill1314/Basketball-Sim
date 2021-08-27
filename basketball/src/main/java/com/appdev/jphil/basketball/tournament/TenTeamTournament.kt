package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

class TenTeamTournament(
    override val id: Int,
    teams: List<Team>,
    val dataModels: List<StandingsDataModel>
) : Tournament {

    private val sortedTeams = mutableListOf<Team>()
    override val games = mutableListOf<Game>()

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
    }

    private fun getWinner(game: Game): Team {
        return if (game.homeScore > game.awayScore) game.homeTeam else game.awayTeam
    }
}
