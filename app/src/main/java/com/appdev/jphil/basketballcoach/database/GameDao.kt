package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.*
import com.appdev.jphil.basketball.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM GameEntity")
    fun getAllGames(): List<GameEntity>

    @Query("SELECT * FROM GameEntity where homeTeamId in (:teamId) or awayTeamId in (:teamId)")
    fun getAllGamesWithTeamId(teamId: Int): List<GameEntity>

    @Query("SELECT * FROM GameEntity where id in (:gameId)")
    fun getGameWithId(gameId: Int): GameEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGames(games: List<GameEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: GameEntity)

    @Delete
    fun deleteGame(game: GameEntity)
}