package com.appdev.jphil.basketballcoach.database.recruit

import androidx.room.*

@Dao
interface RecruitDao {

    @Query("SELECT * FROM RecruitEntity")
    suspend fun getAllRecruits(): List<RecruitEntity>

    @Query("SELECT * FROM RecruitEntity where id in (:recruitId)")
    suspend fun getRecruitWithId(recruitId: Int): RecruitEntity

    @Query("SELECT * FROM RecruitInterestEntity where recruitId in (:recruitId)")
    suspend fun getAllInterestsWithRecruitID(recruitId: Int): List<RecruitInterestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecruits(recruitEntities: List<RecruitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterests(recruitInterests: List<RecruitInterestEntity>)

    @Query("DELETE FROM RecruitEntity")
    suspend fun deleteAllRecruits()

    @Query("DELETE FROM RecruitInterestEntity")
    suspend fun deleteAllRecruitInterests()
}
