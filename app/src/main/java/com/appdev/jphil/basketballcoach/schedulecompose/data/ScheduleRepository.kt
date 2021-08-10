package com.appdev.jphil.basketballcoach.schedulecompose.data

import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.flurry.sdk.it
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val gameDao: GameDao
) {

    fun getGames() = gameDao
        .getAllGamesFlow()
        .map { entities ->
            entities.map { entity ->
                entity.toDataModel()
            }
        }

    private fun GameEntity.toDataModel() = ScheduleDataModel(
        gameId = id ?: -1,
        topTeamId = awayTeamId,
        bottomTeamId = homeTeamId,
        topTeamName = awayTeamName,
        bottomTeamName = homeTeamName,
        topTeamScore = awayScore,
        bottomTeamScore = homeScore,
        isInProgress = inProgress,
        isFinal = isFinal
    )
}