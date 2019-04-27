package com.appdev.jphil.basketballcoach.database.game

import com.appdev.jphil.basketball.game.Game
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
            if (game.isFinal) {
                game.homeTeam.players.forEach { player ->
                    database.playerDao()
                        .insertGameStats(GameStatsEntity.generate(player, game.season, game.awayTeam.name, true))
                }
                game.awayTeam.players.forEach { player ->
                    database.playerDao()
                        .insertGameStats(GameStatsEntity.generate(player, game.season, game.homeTeam.name, false))
                }
            }
        }
    }

    fun loadGamesForTeam(teamId: Int, database: BasketballDatabase): List<Game> {
        val gameEntities = database.gameDao().getAllGamesWithTeamId(teamId)
        val games = mutableListOf<Game>()
        gameEntities.forEach { entity -> games.add(createGame(entity, database)) }
        return games
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

    fun loadGameEntitiesForTournament(tournamentId: Int, database: BasketballDatabase): List<GameEntity> {
        return database.gameDao().getGamesWithTournamentId(tournamentId)
    }

    private fun createGame(entity: GameEntity, database: BasketballDatabase): Game {
        val homeTeam = database.teamDao().getTeamWithId(entity.homeTeamId)!!.createTeam(
                database.playerDao().getPlayersOnTeam(entity.homeTeamId),
                database.coachDao().getCoachesByTeamId(entity.homeTeamId)
            )
        val awayTeam = database.teamDao().getTeamWithId(entity.awayTeamId)!!.createTeam(
                database.playerDao().getPlayersOnTeam(entity.awayTeamId),
                database.coachDao().getCoachesByTeamId(entity.awayTeamId)
            )
        return entity.createGame(homeTeam, awayTeam)
    }

    fun saveOnlyGames(games: List<Game>, database: BasketballDatabase) {
        games.forEach {game -> database.gameDao().insertGame(GameEntity.from(game)) }
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

    fun deleteAllGames(database: BasketballDatabase) {
        database.gameDao().deleteAllGames()
        database.gameDao().deleteAllGameEvents()
    }
}