package com.appdev.jphil.basketballcoach.database.player

import androidx.room.*

@Dao
interface PlayerDao {
    // Player Entity
    @Query("SELECT * FROM PlayerEntity")
    suspend fun getAllPlayers(): List<PlayerEntity>

    @Query("SELECT * FROM PlayerEntity where teamId in (:teamId)")
    suspend fun getPlayersOnTeam(teamId: Int): List<PlayerEntity>

    @Query("SELECT * FROM PlayerEntity where id in (:playerId)")
    suspend fun getPlayerById(playerId: Int): PlayerEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(players: PlayerEntity)

    @Delete
    suspend fun deletePlayer(playerEntity: PlayerEntity)


    // Game Stats Entity
    @Query("SELECT * FROM GameStatsEntity where playerId in (:playerId)")
    suspend fun getAllGamesForPlayer(playerId: Int): List<GameStatsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameStats(stats: GameStatsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameStats(stats: List<GameStatsEntity>)

    @Query("DELETE FROM GameStatsEntity where playerId in (:playerId)")
    suspend fun deleteGameStatsForPlayer(playerId: Int)

    @Delete
    suspend fun deleteGameStats(gameStats: List<GameStatsEntity>)

    // Player Progression Entity
    @Query("SELECT * FROM PlayerProgressionEntity where playerId in (:playerId)")
    suspend fun getProgressForPlayer(playerId: Int): List<PlayerProgressionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerProgression(playerProgressionEntity: PlayerProgressionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerProgressions(playerProgressionEntity: List<PlayerProgressionEntity>)

    @Query("DELETE FROM PlayerProgressionEntity where playerId in (:playerId)")
    suspend fun deleteProgressionForPlayer(playerId: Int)
}
