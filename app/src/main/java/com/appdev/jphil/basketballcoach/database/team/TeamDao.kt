package com.appdev.jphil.basketballcoach.database.team

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TeamDao {
    @Query("SELECT * FROM TeamEntity")
    suspend fun getAllTeams(): List<TeamEntity>

    @Query("SELECT * FROM TeamEntity where teamId in (:teamId)")
    suspend fun getTeamWithId(teamId: Int): TeamEntity

    @Query("SELECT * FROM TeamEntity where isUser in (:isUser)")
    suspend fun getTeamIsUser(isUser: Boolean = true): TeamEntity

    @Query("SELECT teamId FROM TeamEntity where isUser in (:isUser)")
    suspend fun getUserTeamId(isUser: Boolean = true): Int

    @Query("SELECT teamId FROM TeamEntity where isUser in (:isUser)")
    suspend fun getNullableUserTeamId(isUser: Boolean = true): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(teams: TeamEntity)

    @Query("delete from TeamEntity")
    suspend fun deleteAllTeams()
}
