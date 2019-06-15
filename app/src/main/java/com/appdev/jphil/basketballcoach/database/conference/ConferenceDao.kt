package com.appdev.jphil.basketballcoach.database.conference

import androidx.room.*

@Dao
interface ConferenceDao {
    @Query("SELECT * FROM ConferenceEntity")
    fun getAllConferenceEntities(): List<ConferenceEntity>

    @Query("SELECT * FROM ConferenceEntity where id in (:id)")
    fun getConferenceWithId(id: Int): ConferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConferences(conferences: List<ConferenceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConference(conference: ConferenceEntity)

    @Delete
    fun deleteConference(conference: ConferenceEntity)
}