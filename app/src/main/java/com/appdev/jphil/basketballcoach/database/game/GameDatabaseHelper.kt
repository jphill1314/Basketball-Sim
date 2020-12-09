package com.appdev.jphil.basketballcoach.database.game

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper

object GameDatabaseHelper {

    suspend fun saveGames(games: List<Game>, database: BasketballDatabase) {
        games.forEach { game ->
            game.pauseGame()
            database.gameDao().insertGame(GameEntity.from(game))
            TeamDatabaseHelper.saveTeam(game.homeTeam, database)
            TeamDatabaseHelper.saveTeam(game.awayTeam, database)
            database.playerDao().insertGameStats(getStats(game))
        }
    }

    suspend fun saveGameAndStats(game: Game, database: BasketballDatabase) {
        database.gameDao().insertGame(GameEntity.from(game))
        database.playerDao().insertGameStats(getStats(game))
    }

    suspend fun loadGameByIdWithTeams(gameId: Int, teams: Map<Int, Team>, database: BasketballDatabase): Game? {
        database.gameDao().getGameWithId(gameId)?.let { game ->
            return createGameWithTeams(game, teams, database)
        }

        return null
    }

    suspend fun loadGameById(gameId: Int, database: BasketballDatabase): Game? {
        val game = database.gameDao().getGameWithId(gameId)
        return game?.let { createGame(it, database) }
    }

    suspend fun loadGamesForTournament(tournamentId: Int, teams: Map<Int, Team>, database: BasketballDatabase): List<Game> {
        val games = mutableListOf<Game>()
        database.gameDao().getGamesWithTournamentId(tournamentId).forEach { entity ->
            games.add(createGameWithTeams(entity, teams, database))
        }
        return games
    }

    suspend fun hasTournamentGames(database: BasketballDatabase): Boolean {
        return database.gameDao().getTournamentGames().isNotEmpty()
    }

    private suspend fun createGame(entity: GameEntity, database: BasketballDatabase): Game {
        val homeTeam = TeamDatabaseHelper.loadTeamById(entity.homeTeamId, database)!!
        val awayTeam = TeamDatabaseHelper.loadTeamById(entity.awayTeamId, database)!!
        return entity.createGame(homeTeam, awayTeam)
    }

    private suspend fun createGameWithTeams(game: GameEntity, teams: Map<Int, Team>, database: BasketballDatabase): Game {
        val homeTeam = teams[game.homeTeamId] ?: TeamDatabaseHelper.loadTeamById(game.homeTeamId, database)!!
        val awayTeam = teams[game.awayTeamId] ?: TeamDatabaseHelper.loadTeamById(game.awayTeamId, database)!!
        return game.createGame(homeTeam, awayTeam)
    }

    suspend fun saveOnlyGames(games: List<Game>, database: BasketballDatabase) {
        games.forEach { game -> database.gameDao().insertGame(GameEntity.from(game)) }
    }

    suspend fun saveGameEvents(events: List<GameEventEntity>, database: BasketballDatabase) {
        database.gameDao().insertGameEvents(events)
    }

    suspend fun loadGameEvents(gameId: Int, database: BasketballDatabase): List<GameEventEntity> {
        return database.gameDao().getAllGameEventsForGame(gameId)
    }

    suspend fun loadCompletedGameEntities(database: BasketballDatabase): List<GameEntity> {
        return database.gameDao().getGamesWithIsFinal(true)
    }

    suspend fun loadAllGameEntities(database: BasketballDatabase): List<GameEntity> {
        return database.gameDao().getAllGames()
    }

    suspend fun getFirstGameWithIsFinal(isFinal: Boolean, database: BasketballDatabase): Int {
        return database.gameDao().getFirstGameWithIsFinal(isFinal)
    }

    suspend fun deleteAllGames(database: BasketballDatabase) {
        database.gameDao().deleteAllGames()
        database.gameDao().deleteAllGameEvents()
    }

    fun getStats(game: Game): List<GameStatsEntity> {
        val stats = mutableListOf<GameStatsEntity>()
        if (game.isFinal) {
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
        }
        return stats
    }
}
