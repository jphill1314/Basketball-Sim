package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationalDao {

    @Transaction
    @Query("select * from TeamEntity where teamId in (:id)")
    suspend fun loadTeamById(id: Int): TeamRelations

    @Transaction
    @Query("select * from TeamEntity where isUser in (:isUser)")
    suspend fun loadUserTeam(isUser: Boolean = true): TeamRelations

    @Transaction
    @Query("select * from TeamEntity where isUser in (:isUser)")
    suspend fun loadUserTeamNullable(isUser: Boolean = true): TeamRelations?

    @Transaction
    @Query("select * from TeamEntity where teamId in (:id)")
    fun loadTeamByIdFlow(id: Int): Flow<TeamRelations>

    @Transaction
    @Query("select * from TeamEntity where postseasonTournamentId in (:tournamentId)")
    suspend fun loadTeamsByTournamentId(tournamentId: Int): List<TeamRelations>

    @Transaction
    @Query("select * from GameEntity where id in (:gameId)")
    suspend fun loadGameWithTeams(gameId: Int): GameRelations

    @Transaction
    @Query("select * from ConferenceEntity")
    suspend fun loadAllConferenceTournamentData(): List<ConferenceTournamentRelations>

    @Transaction
    @Query("select * from ConferenceEntity")
    suspend fun loadAllConferenceData(): List<ConferenceRelations>

    @Transaction
    @Query("select * from ConferenceEntity where id in (:confId)")
    suspend fun loadConferenceDataById(confId: Int): ConferenceRelations

    @Transaction
    @Query("select * from ConferenceEntity where id in (:id)")
    suspend fun loadConferenceTournamentData(id: Int): ConferenceTournamentRelations

    @Transaction
    @Query("select * from RecruitEntity")
    suspend fun loadAllRecruits(): List<RecruitRelations>

    @Transaction
    @Query("select * from RecruitEntity")
    fun loadAllRecruitsFlow(): Flow<List<RecruitRelations>>

    @Transaction
    @Query("select * from RecruitEntity where id in (:recruitId)")
    fun loadRecruitByIdFlow(recruitId: Int): Flow<RecruitRelations>
}
