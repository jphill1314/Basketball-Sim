package com.appdev.jphil.basketballcoach.database.team

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDatabaseHelper
import com.appdev.jphil.basketballcoach.database.player.PlayerDatabaseHelper

object TeamDatabaseHelper {

    suspend fun loadTeamById(teamId: Int, database: BasketballDatabase): Team {
        return createTeam(database.teamDao().getTeamWithId(teamId), database)
    }

    private suspend fun createTeam(teamEntity: TeamEntity, database: BasketballDatabase): Team {
        val players = PlayerDatabaseHelper.loadAllPlayersOnTeam(teamEntity.teamId, database)
        val coaches = CoachDatabaseHelper.loadAllCoachesByTeamId(teamEntity.teamId, database)
        val recruits = database.relationalDao().loadAllRecruits().map { r -> r.create() }
        return teamEntity.createTeam(players, coaches, recruits)
    }

    suspend fun saveTeam(team: Team, database: BasketballDatabase) {
        team.roster.forEach { player -> PlayerDatabaseHelper.savePlayer(player, database) }
        team.coaches.forEach { coach -> CoachDatabaseHelper.saveCoach(coach, database) }
        database.teamDao().insertTeam(TeamEntity.from(team))
    }
}
