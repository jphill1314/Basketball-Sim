package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

class NationalChampionship(
    override val id: Int,
    override val teams: List<Team>,
) : Tournament {

    init {
        val sortedIds = teams.map { it.teamId }
        teams.forEach {
            if (it.postSeasonTournamentId != id) {
                it.postSeasonTournamentId = id
                it.postSeasonTournamentSeed = (sortedIds.indexOf(it.teamId) / 4) + 1
            }
        }
    }

    override val games = mutableListOf<Game>()

    override fun generateNextRound(season: Int): List<Game> {
        val newGames = mutableListOf<Game>()

        when (games.size) {
            0 -> newGames.addAll(generateFirstRound(season))
            in 16..23 -> newGames.addAll(generateSecondRound(season))
            in 24..27 -> newGames.addAll(generateThirdRound(season))
            in 28..29 -> newGames.addAll(generateFourthRound(season))
            30 -> newGames.addAll(generateFifthRound(season))
        }

        games.addAll(newGames)
        return newGames
    }

    override fun getWinnerOfTournament(): Team? {
        if (games.size == 31 && games.last().isFinal) {
            games.last().winner
        }
        return null
    }

    override fun replaceGames(newGames: List<Game>) {
        games.clear()
        games.addAll(newGames)
    }

    private val Game.winner: Team
        get() = if (homeScore > awayScore) homeTeam else awayTeam

    private fun generateFirstRound(season: Int): List<Game> {
        val newGames = mutableListOf<Game>()

        // Region 1
        // Game 0 - 1 v 8
        newGames.add(NewGameHelper.newGame(teams[0], teams[31], season, id))
        // Game 1 - 4 v 5
        newGames.add(NewGameHelper.newGame(teams[15], teams[16], season, id))
        // Game 2 - 3 v 6
        newGames.add(NewGameHelper.newGame(teams[8], teams[23], season, id))
        // Game 3 - 2 v 7
        newGames.add(NewGameHelper.newGame(teams[7], teams[24], season, id))

        // Region 2
        // Game 4 - 1 v 8
        newGames.add(NewGameHelper.newGame(teams[3], teams[28], season, id))
        // Game 5 - 4 v 5
        newGames.add(NewGameHelper.newGame(teams[12], teams[19], season, id))
        // Game 6 - 3 v 6
        newGames.add(NewGameHelper.newGame(teams[11], teams[20], season, id))
        // Game 7 - 2 v 7
        newGames.add(NewGameHelper.newGame(teams[4], teams[27], season, id))

        // Region 3
        // Game 8 - 1 v 8
        newGames.add(NewGameHelper.newGame(teams[2], teams[29], season, id))
        // Game 9 - 4 v 5
        newGames.add(NewGameHelper.newGame(teams[13], teams[18], season, id))
        // Game 10 - 3 v 6
        newGames.add(NewGameHelper.newGame(teams[10], teams[21], season, id))
        // Game 11 - 2 v 7
        newGames.add(NewGameHelper.newGame(teams[5], teams[26], season, id))

        // Region 4
        // Game 12 - 1 v 8
        newGames.add(NewGameHelper.newGame(teams[1], teams[30], season, id))
        // Game 13 - 4 v 5
        newGames.add(NewGameHelper.newGame(teams[14], teams[17], season, id))
        // Game 14 - 3 v 6
        newGames.add(NewGameHelper.newGame(teams[9], teams[22], season, id))
        // Game 15 - 2 v 7
        newGames.add(NewGameHelper.newGame(teams[6], teams[25], season, id))

        return newGames
    }

    private fun generateSecondRound(season: Int): List<Game> {
        val newGames = mutableListOf<Game>()
        val finalGamesSize = games.filter { it.isFinal }.size
        val gamesSize = games.size
        when  {
            // Game 16
            finalGamesSize == 2 && gamesSize < 17 ->
                newGames.add(NewGameHelper.newGame(games[0].winner, games[1].winner, season, id))
            // Game 17
            finalGamesSize == 4 && gamesSize < 18 ->
                newGames.add(NewGameHelper.newGame(games[2].winner, games[3].winner, season, id))
            // Game 18
            finalGamesSize == 6 && gamesSize < 19 ->
                newGames.add(NewGameHelper.newGame(games[4].winner, games[5].winner, season, id))
            // Game 19
            finalGamesSize == 8 && gamesSize < 20 ->
                newGames.add(NewGameHelper.newGame(games[6].winner, games[7].winner, season, id))
            // Game 20
            finalGamesSize == 10 && gamesSize < 21  ->
                newGames.add(NewGameHelper.newGame(games[8].winner, games[9].winner, season, id))
            // Game 21
            finalGamesSize == 12 && gamesSize < 22 ->
                newGames.add(NewGameHelper.newGame(games[10].winner, games[11].winner, season, id))
            // Game 22
            finalGamesSize == 14 && gamesSize < 23 ->
                newGames.add(NewGameHelper.newGame(games[12].winner, games[13].winner, season, id))
            // Game 23
            finalGamesSize == 16 && gamesSize < 24 ->
                newGames.add(NewGameHelper.newGame(games[14].winner, games[15].winner, season, id))
        }
        return newGames
    }

    private fun generateThirdRound(season: Int): List<Game> {
        val newGames = mutableListOf<Game>()
        val finalGamesSize = games.filter { it.isFinal }.size
        val gamesSize = games.size
        when {
            // Game 24
            finalGamesSize == 18 && gamesSize < 25 -> newGames.add(NewGameHelper.newGame(games[16].winner, games[17].winner, season, id))
            // Game 25
            finalGamesSize == 20 && gamesSize < 26 -> newGames.add(NewGameHelper.newGame(games[18].winner, games[19].winner, season, id))
            // Game 26
            finalGamesSize == 22 && gamesSize < 27 -> newGames.add(NewGameHelper.newGame(games[20].winner, games[21].winner, season, id))
            // Game 27
            finalGamesSize == 24 && gamesSize < 28 -> newGames.add(NewGameHelper.newGame(games[22].winner, games[23].winner, season, id))
        }
        return newGames
    }

    private fun generateFourthRound(season: Int): List<Game> {
        val newGames = mutableListOf<Game>()
        val finalGamesSize = games.filter { it.isFinal }.size
        val gamesSize = games.size
        when {
            // Game 28
            finalGamesSize == 26 && gamesSize < 29 -> newGames.add(NewGameHelper.newGame(games[24].winner, games[25].winner, season, id))
            // Game 29
            finalGamesSize == 28 && gamesSize < 30 -> newGames.add(NewGameHelper.newGame(games[26].winner, games[27].winner, season, id))
        }
        return newGames
    }

    private fun generateFifthRound(season: Int): List<Game> {
        val newGames = mutableListOf<Game>()
        val finalGamesSize = games.filter { it.isFinal }.size
        val gamesSize = games.size
        when {
            // Game 30
            finalGamesSize == 30 && gamesSize < 31 -> newGames.add(NewGameHelper.newGame(games[28].winner, games[29].winner, season, id))
        }
        return newGames
    }
}
