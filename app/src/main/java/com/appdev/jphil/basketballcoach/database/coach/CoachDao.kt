package com.appdev.jphil.basketballcoach.database.coach

import android.arch.persistence.room.*

@Dao
abstract class CoachDao {

    @Query("SELECT * FROM CoachEntity where teamId in (:teamId)")
    abstract fun getCoachesByTeamId(teamId: Int): List<CoachEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveCoach(coachEntity: CoachEntity)

    @Query("SELECT * FROM ScoutingAssignmentEntity where coachId in (:coachId)")
    abstract fun getScoutingAssignmentByCoachId(coachId: Int): ScoutingAssignmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveScoutingAssignment(scoutingAssignmentEntity: ScoutingAssignmentEntity?)

    @Delete
    abstract fun deleteScoutingAssignment(scoutingAssignmentEntity: ScoutingAssignmentEntity?)
}