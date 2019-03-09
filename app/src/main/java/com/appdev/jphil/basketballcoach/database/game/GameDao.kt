package com.appdev.jphil.basketballcoach.database.game

import android.arch.persistence.room.*

@Dao
interface GameDao {
    @Query("SELECT * FROM GameEntity")
    fun getAllGames(): List<GameEntity>

    @Query("SELECT * FROM GameEntity where homeTeamId in (:teamId) or awayTeamId in (:teamId)")
    fun getAllGamesWithTeamId(teamId: Int): List<GameEntity>

    @Query("SELECT * FROM GameEntity where id in (:gameId)")
    fun getGameWithId(gameId: Int): GameEntity

    @Query("SELECT * FROM GameEntity where isFinal in (:isFinal)")
    fun getGamesWithIsFinal(isFinal: Boolean): List<GameEntity>

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

    @Delete
    fun deleteGameEvents(gameEvents: List<GameEventEntity>)
}