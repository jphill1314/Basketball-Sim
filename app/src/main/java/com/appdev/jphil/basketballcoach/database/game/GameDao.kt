package com.appdev.jphil.basketballcoach.database.game

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM GameEntity")
    suspend fun getAllGames(): List<GameEntity>

    @Query("SELECT * FROM GameEntity where homeTeamId in (:teamId) or awayTeamId in (:teamId)")
    suspend fun getAllGamesWithTeamId(teamId: Int): List<GameEntity>

    @Query("SELECT * FROM GameEntity where id in (:gameId)")
    suspend fun getGameWithId(gameId: Int): GameEntity?

    @Query("SELECT * FROM GameEntity where isFinal in (:isFinal)")
    suspend fun getGamesWithIsFinal(isFinal: Boolean): List<GameEntity>

    @Query("SELECT min(id) FROM GameEntity where isFinal in (:isFinal)")
    suspend fun getFirstGameWithIsFinal(isFinal: Boolean): Int

    @Query("SELECT min(id) FROM GameEntity where isFinal in (:isFinal) and awayTeamId in (:teamId) or homeTeamId in (:teamId)")
    suspend fun getFistGameOfTeam(isFinal: Boolean, teamId: Int): Int

    @Query("SELECT * FROM GameEntity where tournamentId in (:tournamentId)")
    suspend fun getGamesWithTournamentId(tournamentId: Int): List<GameEntity>

    @Query("SELECT * FROM GameEntity where tournamentId not null")
    suspend fun getTournamentGames(): List<GameEntity>

    @Query("select * from GameEntity where tournamentId is null")
    suspend fun getNonTournamentGames(): List<GameEntity>

    @Query("DELETE FROM GameEntity")
    suspend fun deleteAllGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity): Long

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("SELECT * FROM GameEventEntity where gameId in (:gameId)")
    suspend fun getAllGameEventsForGame(gameId: Int): List<GameEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameEvents(gameEvents: List<GameEventEntity>)

    @Query("DELETE FROM GameEventEntity")
    suspend fun deleteAllGameEvents()

    @Delete
    suspend fun deleteGameEvents(gameEvents: List<GameEventEntity>)

    @Query("SELECT * FROM GameEntity where (homeTeamId in (:teamId) or awayTeamId in (:teamId)) and tournamentId is null")
    fun getGamesForTeam(teamId: Int): Flow<List<GameEntity>>

    @Query("SELECT * FROM GameEntity")
    fun getAllGamesFlow(): Flow<List<GameEntity>>

    @Query("SELECT * FROM GameEntity where isFinal in (:isFinal) and id > (:firstGameId)")
    fun getAllGamesWithIsFinalFlow(isFinal: Boolean, firstGameId: Int): Flow<List<GameEntity>>
}
