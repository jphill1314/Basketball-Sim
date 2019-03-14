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
            val coach = database.coachDao().getCoachByTeamId(teamId)
            it.createTeam(players, coach)
        }
    }

    fun loadUserTeam(database: BasketballDatabase): Team? {
        val teamEntity = database.teamDao().getTeamIsUser(true)
        return teamEntity?.let {
            val players = database.playerDao().getPlayersOnTeam(it.teamId)
            val coach = database.coachDao().getCoachByTeamId(it.teamId)
            it.createTeam(players, coach)
        }
    }

    fun saveTeam(team: Team, database: BasketballDatabase) {
        team.roster.forEach { player -> database.playerDao().insertPlayer(PlayerEntity.from(player)) }
        database.coachDao().saveCoach(CoachEntity.from(team.coach))
        database.teamDao().insertTeam(TeamEntity.from(team))
    }
}