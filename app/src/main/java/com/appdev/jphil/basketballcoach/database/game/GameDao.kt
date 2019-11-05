package com.appdev.jphil.basketballcoach.database.game

import androidx.room.*

@Dao
interface GameDao {
    @Query("SELECT * FROM GameEntity")
    fun getAllGames(): List<GameEntity>

    @Query("SELECT * FROM GameEntity where homeTeamId in (:teamId) or awayTeamId in (:teamId)")
    fun getAllGamesWithTeamId(teamId: Int): List<GameEntity>

    @Query("SELECT * FROM GameEntity where id in (:gameId)")
    fun getGameWithId(gameId: Int): GameEntity?

    @Query("SELECT * FROM GameEntity where isFinal in (:isFinal)")
    fun getGamesWithIsFinal(isFinal: Boolean): List<GameEntity>

    @Query("SELECT min(id) FROM GameEntity where isFinal in (:isFinal)")
    fun getFirstGameWithIsFinal(isFinal: Boolean): Int

    @Query("SELECT min(id) FROM GameEntity where isFinal in (:isFinal) and awayTeamId in (:teamId) or homeTeamId in (:teamId)")
    fun getFistGameOfTeam(isFinal: Boolean, teamId: Int): Int

    @Query("SELECT * FROM GameEntity where tournamentId in (:tournamentId)")
    fun getGamesWithTournamentId(tournamentId: Int): List<GameEntity>

    @Query("DELETE FROM GameEntity")
    fun deleteAllGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGames(games: List<GameEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: GameEntity)

    @Delete
    fun deleteGame(game: GameEntity)

    @Query("SELECT * FROM GameEventEntity where gameId in (:gameId)")
    fun getAllGameEventsForGame(gameId: Int): List<GameEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameEvents(gameEvents: List<GameEventEntity>)

    @Query("DELETE FROM GameEventEntity")
    fun deleteAllGameEvents()

    @Delete
    fun deleteGameEvents(gameEvents: List<GameEventEntity>)
}