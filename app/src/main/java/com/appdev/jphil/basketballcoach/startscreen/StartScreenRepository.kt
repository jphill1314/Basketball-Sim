package com.appdev.jphil.basketballcoach.startscreen

import com.appdev.jphil.basketballcoach.database.team.TeamDao
import javax.inject.Inject

class StartScreenRepository @Inject constructor(
    private val teamDao: TeamDao
) {

    suspend fun doesGameExist() = teamDao.getNullableUserTeamId() != null

    suspend fun getUserTeamEntity() = teamDao.getTeamIsUser()
}
