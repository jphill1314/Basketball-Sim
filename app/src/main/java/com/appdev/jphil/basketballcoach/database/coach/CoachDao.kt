package com.appdev.jphil.basketballcoach.database.coach

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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
}
