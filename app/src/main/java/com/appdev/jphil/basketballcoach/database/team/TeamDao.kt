package com.appdev.jphil.basketballcoach.database.team

import androidx.room.*

@Dao
interface TeamDao {
    @Query("SELECT * FROM TeamEntity")
    fun getAllTeams(): List<TeamEntity>

    @Query("SELECT * FROM TeamEntity where teamId in (:teamId)")
    fun getTeamWithId(teamId: Int): TeamEntity?

    @Query("SELECT * FROM TeamEntity where conferenceId in (:conferenceId)")
    fun getTeamsInConference(conferenceId: Int): List<TeamEntity>

    @Query("SELECT * FROM TeamEntity where isUser in (:isUser)")
    fun getTeamIsUser(isUser: Boolean): TeamEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeam(teams: TeamEntity)

    @Delete
    fun deleteTeam(team: TeamEntity)
}