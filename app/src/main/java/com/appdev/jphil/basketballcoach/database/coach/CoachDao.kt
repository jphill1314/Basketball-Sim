package com.appdev.jphil.basketballcoach.database.coach

import androidx.room.*

@Dao
abstract class CoachDao {

    @Query("SELECT * FROM CoachEntity where teamId in (:teamId)")
    abstract fun getCoachesByTeamId(teamId: Int): List<CoachEntity>

    @Query("SELECT * FROM CoachEntity where id in (:id)")
    abstract fun getCoachById(id: Int): CoachEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveCoach(coachEntity: CoachEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveCoaches(coachEntity: List<CoachEntity>)

    @Query("SELECT * FROM ScoutingAssignmentEntity where coachId in (:coachId)")
    abstract fun getScoutingAssignmentByCoachId(coachId: Int): ScoutingAssignmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveScoutingAssignment(scoutingAssignmentEntity: ScoutingAssignmentEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveScoutingAssignments(scoutingAssignmentEntity: List<ScoutingAssignmentEntity>)

    @Delete
    abstract fun deleteScoutingAssignment(scoutingAssignmentEntity: ScoutingAssignmentEntity?)
}