package com.appdev.jphil.basketballcoach.stats

import com.appdev.jphil.basketballcoach.database.conference.ConferenceDao
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import javax.inject.Inject

class StatsRepository @Inject constructor(
    private val teamDao: TeamDao,
    private val conferenceDao: ConferenceDao,
    private val gameDao: GameDao
) {

    suspend fun getAllTeams() = teamDao.getAllTeams()

    suspend fun getAllConferences() = conferenceDao.getAllConferenceEntities()

    suspend fun getAllGames() = gameDao.getAllGames()
}
