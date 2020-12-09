package com.appdev.jphil.basketballcoach.database.game

import androidx.room.*

@Dao
interface GameDao {
    @Query("SELECT * FROM GameEntity")
    suspend fun getAllGames(): List<GameEntity>

    @Query("SELECT * FROM GameEntity where homeTeamId in (:teamId) or awayTeamId in (:teamId)")
    suspend fun getAllGamesWithTeamId(teamId: Int): List<GameEntity>

    @Query("SELECT * FROM GameEntity where id in (:gameId)")
    suspend fun getGameWithId(gameId: Int): GameEntity?

    @Query("SELECT * FROM GameEntity where isFinal in (:isFinal)")
    suspend fun getGamesWithIsFinal(isFinal: Boolean): List<GameEntity>

    @Query("SELECT min(id) FROM GameEntity where isFinal in (:isFinal)")
    suspend fun getFirstGameWithIsFinal(isFinal: Boolean): Int

    @Query("SELECT min(id) FROM GameEntity where isFinal in (:isFinal) and awayTeamId in (:teamId) or homeTeamId in (:teamId)")
    suspend fun getFistGameOfTeam(isFinal: Boolean, teamId: Int): Int

    @Query("SELECT * FROM GameEntity where tournamentId in (:tournamentId)")
    suspend fun getGamesWithTournamentId(tournamentId: Int): List<GameEntity>

    @Query("SELECT * FROM GameEntity where tournamentId not null")
    suspend fun getTournamentGames(): List<GameEntity>

    @Query("DELETE FROM GameEntity")
    suspend fun deleteAllGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity): Long

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("SELECT * FROM GameEventEntity where gameId in (:gameId)")
    suspend fun getAllGameEventsForGame(gameId: Int): List<GameEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameEvents(gameEvents: List<GameEventEntity>)

    @Query("DELETE FROM GameEventEntity")
    suspend fun deleteAllGameEvents()

    @Delete
    suspend fun deleteGameEvents(gameEvents: List<GameEventEntity>)
}
