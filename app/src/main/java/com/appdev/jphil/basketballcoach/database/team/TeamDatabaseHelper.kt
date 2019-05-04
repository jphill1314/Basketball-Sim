package com.appdev.jphil.basketballcoach.database.team

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerProgressionEntity

object TeamDatabaseHelper {

    fun loadTeamById(teamId: Int, database: BasketballDatabase): Team? {
        return createTeam(database.teamDao().getTeamWithId(teamId), database)
    }

    fun loadUserTeam(database: BasketballDatabase): Team? {
        return createTeam(database.teamDao().getTeamIsUser(true), database)
    }

    fun createTeam(teamEntity: TeamEntity?, database: BasketballDatabase): Team? {
        return teamEntity?.let {
            val players = database.playerDao().getPlayersOnTeam(it.teamId)
            val coaches = database.coachDao().getCoachesByTeamId(it.teamId)
            val progress = mutableListOf<PlayerProgressionEntity?>()
            players.forEach { player ->
                progress.add(database.playerDao().getProgressForPlayer(player.id!!))
            }
            it.createTeam(players, coaches, progress)
        }
    }

    fun saveTeam(team: Team, database: BasketballDatabase) {
        team.roster.forEach { player ->
            database.playerDao().insertPlayer(PlayerEntity.from(player))
            database.playerDao().insertPlayerProgression(PlayerProgressionEntity.from(player.progression))
        }
        team.coaches.forEach { coach -> database.coachDao().saveCoach(CoachEntity.from(coach)) }
        database.teamDao().insertTeam(TeamEntity.from(team))
    }
}