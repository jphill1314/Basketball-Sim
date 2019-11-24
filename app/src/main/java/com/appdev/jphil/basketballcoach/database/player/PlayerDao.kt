package com.appdev.jphil.basketballcoach.database.player

import androidx.room.*

@Dao
interface PlayerDao {
    // Player Entity
    @Query("SELECT * FROM PlayerEntity")
    fun getAllPlayers(): List<PlayerEntity>

    @Query("SELECT * FROM PlayerEntity where teamId in (:teamId)")
    fun getPlayersOnTeam(teamId: Int): List<PlayerEntity>

    @Query("SELECT * FROM PlayerEntity where id in (:playerId)")
    fun getPlayerById(playerId: Int): PlayerEntity

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameStats(stats: List<GameStatsEntity>)

    @Query("DELETE FROM GameStatsEntity where playerId in (:playerId)")
    fun deleteGameStatsForPlayer(playerId: Int)

    @Delete
    fun deleteGameStats(gameStats: List<GameStatsEntity>)

    // Player Progression Entity
    @Query("SELECT * FROM PlayerProgressionEntity where playerId in (:playerId)")
    fun getProgressForPlayer(playerId: Int): List<PlayerProgressionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayerProgression(playerProgressionEntity: PlayerProgressionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayerProgressions(playerProgressionEntity: List<PlayerProgressionEntity>)

    @Query("DELETE FROM PlayerProgressionEntity where playerId in (:playerId)")
    fun deleteProgressionForPlayer(playerId: Int)
}