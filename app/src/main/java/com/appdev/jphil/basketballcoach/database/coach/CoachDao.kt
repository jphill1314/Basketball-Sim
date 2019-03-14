package com.appdev.jphil.basketballcoach.database.coach

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
abstract class CoachDao {

    @Query("SELECT * FROM CoachEntity where teamId in (:teamId)")
    abstract fun getCoachByTeamId(teamId: Int): CoachEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveCoach(coachEntity: CoachEntity)
}