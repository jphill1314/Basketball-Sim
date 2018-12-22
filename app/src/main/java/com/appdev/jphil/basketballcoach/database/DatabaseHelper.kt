package com.appdev.jphil.basketballcoach.database

import com.appdev.jphil.basketball.Team
import javax.inject.Inject

class DatabaseHelper @Inject constructor(private val database: BasketballDatabase) {

    fun loadTeamById(teamId: Int): Team? {
        val teamEntity = database.teamDao().getTeamWithId(teamId)
        return if (teamEntity == null) {
            null
        } else {
            val players = database.playerDao().getPlayersOnTeam(teamId)
            return teamEntity.createTeam(players)
        }
    }

    fun saveTeam(team: Team) {
        team.roster.forEach { player -> database.playerDao().insertPlayer(PlayerEntity(player)) }
        database.teamDao().insertTeam(TeamEntity(team))
    }
}