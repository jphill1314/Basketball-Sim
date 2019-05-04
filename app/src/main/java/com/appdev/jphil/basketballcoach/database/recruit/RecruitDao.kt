package com.appdev.jphil.basketballcoach.database.recruit

import android.arch.persistence.room.*

@Dao
interface RecruitDao {

    @Query("SELECT * FROM RecruitEntity")
    fun getAllRecruits(): List<RecruitEntity>

    @Query("SELECT * FROM RecruitEntity where id in (:recruitId)")
    fun getRecruitWithId(recruitId: Int): RecruitEntity

    @Query("SELECT * FROM RecruitInterestEntity where recruitId in (:recruitId)")
    fun getAllInterestsWithRecruitID(recruitId: Int): List<RecruitInterestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecruits(recruitEntities: List<RecruitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInterests(recruitInterests: List<RecruitInterestEntity>)

    @Query("DELETE FROM RecruitEntity")
    fun deleteAllRecruits()

    @Query("DELETE FROM RecruitInterestEntity")
    fun deleteAllRecruitInterests()
}