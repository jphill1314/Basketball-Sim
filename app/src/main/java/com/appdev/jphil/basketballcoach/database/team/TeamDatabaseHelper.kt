package com.appdev.jphil.basketballcoach.database.team

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDatabaseHelper
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerDatabaseHelper
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerProgressionEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper

object TeamDatabaseHelper {

    fun loadTeamById(teamId: Int, database: BasketballDatabase): Team? {
        return createTeam(database.teamDao().getTeamWithId(teamId), database)
    }

    fun loadUserTeam(database: BasketballDatabase): Team? {
        return createTeam(database.teamDao().getTeamIsUser(true), database)
    }

    fun createTeam(teamEntity: TeamEntity?, database: BasketballDatabase): Team? {
        return teamEntity?.let {
            val players = PlayerDatabaseHelper.loadAllPlayersOnTeam(it.teamId, database)
            val coaches = CoachDatabaseHelper.loadAllCoachesByTeamId(it.teamId, database)
            val recruits = mutableListOf<Recruit>()
            it.knownRecruits.forEach { id -> recruits.add(RecruitDatabaseHelper.loadRecruitWithId(id, database)) }
            it.createTeam(players, coaches, recruits)
        }
    }

    fun saveTeam(team: Team, database: BasketballDatabase) {
        team.roster.forEach { player ->
            database.playerDao().insertPlayer(PlayerEntity.from(player))
            player.progression.forEach {
                database.playerDao().insertPlayerProgression(PlayerProgressionEntity.from(it))
            }
        }
        team.coaches.forEach { coach -> database.coachDao().saveCoach(CoachEntity.from(coach)) }
        database.teamDao().insertTeam(TeamEntity.from(team))
    }
}