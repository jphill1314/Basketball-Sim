package com.appdev.jphil.basketballcoach.schedulecompose.data

import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleRepository constructor(
    private val gameDao: GameDao
) {

    fun getGamesFroTeam(teamId: Int) = gameDao
        .getGamesForTeam(teamId)
        .map { it.toDataModel() }

    private fun GameEntity.toDataModel() = ScheduleDataModel(
        topTeamName = awayTeamName,
        bottomTeamName = homeTeamName,
        topTeamScore = awayScore.toString(),
        bottomTeamScore = homeScore.toString()
    )
}