package com.appdev.jphil.basketballcoach.database.conference

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ConferenceDao {
    @Query("SELECT * FROM ConferenceEntity")
    suspend fun getAllConferenceEntities(): List<ConferenceEntity>

    @Query("SELECT * FROM ConferenceEntity where id in (:id)")
    suspend fun getConferenceWithId(id: Int): ConferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConferences(conferences: List<ConferenceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConference(conference: ConferenceEntity)

    @Delete
    suspend fun deleteConference(conference: ConferenceEntity)
}
