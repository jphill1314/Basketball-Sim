package com.appdev.jphil.basketballcoach.database.coach

import androidx.room.*

@Dao
interface CoachDao {

    @Query("SELECT * FROM CoachEntity where teamId in (:teamId)")
    suspend fun getCoachesByTeamId(teamId: Int): List<CoachEntity>

    @Query("SELECT * FROM CoachEntity where id in (:id)")
    suspend fun getCoachById(id: Int): CoachEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoach(coachEntity: CoachEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoaches(coachEntity: List<CoachEntity>)

    @Query("SELECT * FROM ScoutingAssignmentEntity where coachId in (:coachId)")
    suspend fun getScoutingAssignmentByCoachId(coachId: Int): ScoutingAssignmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveScoutingAssignment(scoutingAssignmentEntity: ScoutingAssignmentEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveScoutingAssignments(scoutingAssignmentEntity: List<ScoutingAssignmentEntity>)

    @Delete
    suspend fun deleteScoutingAssignment(scoutingAssignmentEntity: ScoutingAssignmentEntity?)
}
