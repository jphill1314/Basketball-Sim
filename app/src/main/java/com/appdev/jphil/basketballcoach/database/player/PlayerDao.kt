package com.appdev.jphil.basketballcoach.database.player

import android.arch.persistence.room.*

@Dao
interface PlayerDao {
    // Player Entity
    @Query("SELECT * FROM PlayerEntity")
    fun getAllPlayers(): List<PlayerEntity>

    @Query("SELECT * FROM PlayerEntity where teamId in (:teamId)")
    fun getPlayersOnTeam(teamId: Int): List<PlayerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayers(players: List<PlayerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(players: PlayerEntity)

    @Delete
    fun deletePlayer(playerEntity: PlayerEntity)


    // Game Stats Entity
    @Query("SELECT * FROM GameStatsEntity where playerId in (:playerId)")
    fun getAllGamesForPlayer(playerId: Int): List<GameStatsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameStats(stats: GameStatsEntity)

    @Delete
    fun deleteGameStats(gameStats: List<GameStatsEntity>)
}