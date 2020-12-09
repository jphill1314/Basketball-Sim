package com.appdev.jphil.basketballcoach.database.team

import androidx.room.*

@Dao
interface TeamDao {
    @Query("SELECT * FROM TeamEntity")
    suspend fun getAllTeams(): List<TeamEntity>

    @Query("SELECT * FROM TeamEntity where teamId in (:teamId)")
    suspend fun getTeamWithId(teamId: Int): TeamEntity?

    @Query("SELECT * FROM TeamEntity where conferenceId in (:conferenceId)")
    suspend fun getTeamsInConference(conferenceId: Int): List<TeamEntity>

    @Query("SELECT * FROM TeamEntity where isUser in (:isUser)")
    suspend fun getTeamIsUser(isUser: Boolean): TeamEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(teams: TeamEntity)

    @Delete
    suspend fun deleteTeam(team: TeamEntity)
}
