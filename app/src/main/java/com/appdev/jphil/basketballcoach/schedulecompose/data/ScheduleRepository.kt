package com.appdev.jphil.basketballcoach.schedulecompose.data

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.flurry.sdk.it
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val gameDao: GameDao,
    private val database: BasketballDatabase
) {

    suspend fun getGames(): Flow<List<ScheduleDataModel>> {
        val userTeam = TeamDatabaseHelper.loadUserTeam(database)!!.teamId
        return gameDao.getAllGamesFlow()
            .map { entities ->
                entities.map { entity ->
                    entity.toDataModel(userTeam)
                }
            }
    }

    suspend fun getGamesForDialog(): Flow<List<ScheduleDataModel>> {
        val userTeam = TeamDatabaseHelper.loadUserTeam(database)!!.teamId
        val firstId = gameDao.getFirstGameWithIsFinal(false) - 1
        return gameDao.getAllGamesWithIsFinalFlow(true, firstId).map { entities ->
            entities.map { it.toDataModel(userTeam) }
        }
    }

    private fun GameEntity.toDataModel(userTeamId: Int) = ScheduleDataModel(
        gameId = id ?: -1,
        topTeamId = awayTeamId,
        bottomTeamId = homeTeamId,
        topTeamName = awayTeamName,
        bottomTeamName = homeTeamName,
        topTeamScore = awayScore,
        bottomTeamScore = homeScore,
        isInProgress = inProgress,
        isFinal = isFinal,
        isHomeTeamUser = homeTeamId == userTeamId
    )
}
