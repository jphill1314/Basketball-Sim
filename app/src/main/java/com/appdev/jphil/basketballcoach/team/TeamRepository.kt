package com.appdev.jphil.basketballcoach.team

import com.appdev.jphil.basketballcoach.database.coach.CoachDao
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerDao
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val relationalDao: RelationalDao,
    private val playerDao: PlayerDao,
    private val coachDao: CoachDao
) {

    fun getTeamRelations(teamId: Int) = relationalDao.loadTeamByIdFlow(teamId)

    suspend fun updatePlayers(playerEntities: List<PlayerEntity>) = playerDao.insertPlayers(playerEntities)

    suspend fun updateCoach(coachEntity: CoachEntity) = coachDao.saveCoach(coachEntity)
}
