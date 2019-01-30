package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.*

@Dao
interface TeamDao {
    @Query("SELECT * FROM TeamEntity")
    fun getAllTeams(): List<TeamEntity>

    @Query("SELECT * FROM TeamEntity where teamId in (:teamId)")
    fun getTeamWithId(teamId: Int): TeamEntity?

    @Query("SELECT * FROM TeamEntity where conferenceId in (:conferenceId)")
    fun getTeamsInConference(conferenceId: Int): List<TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeam(teams: TeamEntity)

    @Delete
    fun deleteTeam(team: TeamEntity)
}