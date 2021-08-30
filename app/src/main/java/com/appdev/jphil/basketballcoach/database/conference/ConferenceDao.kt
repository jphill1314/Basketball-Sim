package com.appdev.jphil.basketballcoach.database.conference

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConferenceDao {
    @Query("SELECT * FROM ConferenceEntity")
    suspend fun getAllConferenceEntities(): List<ConferenceEntity>

    @Query("SELECT * FROM ConferenceEntity where id in (:id)")
    suspend fun getConferenceWithId(id: Int): ConferenceEntity?

    @Query("select championId from ConferenceEntity")
    suspend fun getConferenceChampionIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConferences(conferences: List<ConferenceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConference(conference: ConferenceEntity)

    @Query("SELECT * FROM ConferenceEntity")
    fun getAllConferenceEntitiesFlow(): Flow<List<ConferenceEntity>>

    @Delete
    suspend fun deleteConference(conference: ConferenceEntity)
}
