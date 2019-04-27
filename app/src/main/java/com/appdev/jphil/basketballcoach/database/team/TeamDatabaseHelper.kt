package com.appdev.jphil.basketballcoach.database.team

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity

object TeamDatabaseHelper {

    fun loadTeamById(teamId: Int, database: BasketballDatabase): Team? {
        val teamEntity = database.teamDao().getTeamWithId(teamId)
        return teamEntity?.let {
            val players = database.playerDao().getPlayersOnTeam(teamId)
            val coaches = database.coachDao().getCoachesByTeamId(teamId)
            it.createTeam(players, coaches)
        }
    }

    fun loadUserTeam(database: BasketballDatabase): Team? {
        val teamEntity = database.teamDao().getTeamIsUser(true)
        return teamEntity?.let {
            val players = database.playerDao().getPlayersOnTeam(it.teamId)
            val coaches = database.coachDao().getCoachesByTeamId(it.teamId)
            it.createTeam(players, coaches)
        }
    }

    fun saveTeam(team: Team, database: BasketballDatabase) {
        team.roster.forEach { player -> database.playerDao().insertPlayer(PlayerEntity.from(player)) }
        team.coaches.forEach { coach -> database.coachDao().saveCoach(CoachEntity.from(coach)) }
        database.teamDao().insertTeam(TeamEntity.from(team))
    }
}