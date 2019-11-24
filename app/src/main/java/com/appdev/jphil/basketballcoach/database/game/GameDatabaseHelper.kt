package com.appdev.jphil.basketballcoach.database.game

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper

object GameDatabaseHelper {

    fun saveGames(games: List<Game>, database: BasketballDatabase) {
        games.forEach { game ->
            game.pauseGame()
            database.gameDao().insertGame(GameEntity.from(game))
            TeamDatabaseHelper.saveTeam(game.homeTeam, database)
            TeamDatabaseHelper.saveTeam(game.awayTeam, database)
            saveStats(game, database)
        }
    }

    fun saveGameAndStats(game: Game, database: BasketballDatabase) {
        database.gameDao().insertGame(GameEntity.from(game))
        saveStats(game, database)
    }

    fun loadGameByIdWithTeams(gameId: Int, teams: Map<Int, Team>, database: BasketballDatabase): Game? {
        database.gameDao().getGameWithId(gameId)?.let { game ->
            val homeTeam = teams[game.homeTeamId] ?: TeamDatabaseHelper.loadTeamById(game.homeTeamId, database)!!
            val awayTeam = teams[game.awayTeamId] ?: TeamDatabaseHelper.loadTeamById(game.awayTeamId, database)!!
            return game.createGame(homeTeam, awayTeam)
        }

        return null
    }

    fun loadGameById(gameId: Int, database: BasketballDatabase): Game? {
        val game = database.gameDao().getGameWithId(gameId)
        return game?.let { createGame(it, database) }
    }

    fun loadGamesForTournament(tournamentId: Int, database: BasketballDatabase): List<Game> {
        val games = mutableListOf<Game>()
        database.gameDao().getGamesWithTournamentId(tournamentId).forEach { entity ->
            games.add(createGame(entity, database))
        }
        return games
    }

    private fun createGame(entity: GameEntity, database: BasketballDatabase): Game {
        val homeTeam = TeamDatabaseHelper.loadTeamById(entity.homeTeamId, database)!!
        val awayTeam = TeamDatabaseHelper.loadTeamById(entity.awayTeamId, database)!!
        return entity.createGame(homeTeam, awayTeam)
    }

    fun saveOnlyGames(games: List<Game>, database: BasketballDatabase) {
        games.forEach { game -> database.gameDao().insertGame(GameEntity.from(game)) }
    }

    fun saveGameEvents(events: List<GameEventEntity>, database: BasketballDatabase) {
        database.gameDao().insertGameEvents(events)
    }

    fun loadGameEvents(gameId: Int, database: BasketballDatabase): List<GameEventEntity> {
        return database.gameDao().getAllGameEventsForGame(gameId)
    }

    fun loadCompletedGameEntities(database: BasketballDatabase): List<GameEntity> {
        return database.gameDao().getGamesWithIsFinal(true)
    }

    fun loadAllGameEntities(database: BasketballDatabase): List<GameEntity> {
        return database.gameDao().getAllGames()
    }

    fun getFirstGameWithIsFinal(isFinal: Boolean, database: BasketballDatabase): Int {
        return database.gameDao().getFirstGameWithIsFinal(isFinal)
    }

    fun getFirstGameOfTeam(teamId: Int, isFinal: Boolean, database: BasketballDatabase): Int {
        return database.gameDao().getFistGameOfTeam(isFinal, teamId)
    }

    fun deleteAllGames(database: BasketballDatabase) {
        database.gameDao().deleteAllGames()
        database.gameDao().deleteAllGameEvents()
    }

    private fun saveStats(game: Game, database: BasketballDatabase) {
        if (game.isFinal) {
            val stats = mutableListOf<GameStatsEntity>()
            game.homeTeam.players.forEach { player ->
                stats.add(GameStatsEntity.generate(
                        player,
                        game.season,
                        game.awayTeam.schoolName,
                        true,
                        game.homeScore,
                        game.awayScore,
                        game.id!!
                    ))
            }
            game.awayTeam.players.forEach { player ->
                stats.add(GameStatsEntity.generate(
                        player,
                        game.season,
                        game.homeTeam.schoolName,
                        false,
                        game.homeScore,
                        game.awayScore,
                        game.id!!
                    ))
            }
            database.playerDao().insertGameStats(stats)
        }
    }
}