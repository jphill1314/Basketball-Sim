package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RelationalDao {

    @Transaction
    @Query("select * from TeamEntity where teamId in (:id)")
    suspend fun loadTeamById(id: Int): TeamRelations

    @Transaction
    @Query("select * from GameEntity where id in (:gameId)")
    suspend fun loadGameWithTeams(gameId: Int): GameRelations

    @Transaction
    @Query("select * from ConferenceEntity")
    suspend fun loadAllConferenceTournamentData(): List<ConferenceTournamentRelations>

    @Transaction
    @Query("select * from ConferenceEntity where id in (:id)")
    suspend fun loadConferenceTournamentData(id: Int): ConferenceTournamentRelations
}
