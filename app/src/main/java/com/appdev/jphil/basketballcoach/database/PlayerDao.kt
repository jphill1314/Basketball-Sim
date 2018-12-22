package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.*

@Dao
interface PlayerDao {
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
}