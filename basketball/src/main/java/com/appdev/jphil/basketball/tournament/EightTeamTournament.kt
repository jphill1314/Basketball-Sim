package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

class EightTeamTournament(
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

        val gameSize = games.size
        val finalGameSize = games.filter { it.isFinal }.size
        when {
            gameSize == 0 -> {
                newGames.add(NewGameHelper.newGame(sortedTeams[0], sortedTeams[7], season, id))
                newGames.add(NewGameHelper.newGame(sortedTeams[1], sortedTeams[6], season, id))
                newGames.add(NewGameHelper.newGame(sortedTeams[2], sortedTeams[5], season, id))
                newGames.add(NewGameHelper.newGame(sortedTeams[3], sortedTeams[4], season, id))
            }
            finalGameSize == 2 && gameSize == 4 -> {
                newGames.add(NewGameHelper.newGame(getWinner(games[0]), getWinner(games[1]), season, id))
            }
            finalGameSize == 4 && gameSize == 5 -> {
                newGames.add(NewGameHelper.newGame(getWinner(games[3]), getWinner(games[2]), season, id))
            }
            finalGameSize == 6 && gameSize == 6 -> {
                newGames.add(NewGameHelper.newGame(getWinner(games[4]), getWinner(games[5]), season, id))
            }
        }
        games.addAll(newGames)
        return newGames
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
        check(games.size <= 7) { "More than 7 games in 8 team tournament! Total games: ${games.size}" }
    }

    private fun getWinner(game: Game): Team {
        return if (game.homeScore > game.awayScore) game.homeTeam else game.awayTeam
    }
}
