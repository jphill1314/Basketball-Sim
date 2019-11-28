package com.appdev.jphil.basketballcoach.database

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.coach.ScoutingAssignmentEntity
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerProgressionEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

object BatchInsertHelper {

    fun saveConferences(conferences: List<Conference>, database: BasketballDatabase) {
        val teams = mutableListOf<Team>()
        conferences.forEach { teams.addAll(it.teams) }
        saveTeams(teams, database)
        ConferenceDatabaseHelper.saveOnlyConferences(conferences, database)
    }

    fun saveTeams(teams: List<Team>, database: BasketballDatabase) {
        val players = mutableListOf<PlayerEntity>()
        val progress = mutableListOf<PlayerProgressionEntity>()
        val coaches = mutableListOf<CoachEntity>()
        val assignments = mutableListOf<ScoutingAssignmentEntity>()
        val teamEntities = mutableListOf<TeamEntity>()
        teams.forEach { team ->
            team.roster.forEach { player ->
                progress.addAll(player.progression.map { PlayerProgressionEntity.from(it) })
                players.add(PlayerEntity.from(player))
            }
            team.coaches.forEach { coach ->
                assignments.add(ScoutingAssignmentEntity.from(coach.id!!, coach.scoutingAssignment))
                coaches.add(CoachEntity.from(coach))
            }
            teamEntities.add(TeamEntity.from(team))
        }

        database.playerDao().insertPlayers(players)
        database.playerDao().insertPlayerProgressions(progress)
        database.coachDao().saveScoutingAssignments(assignments)
        database.coachDao().saveCoaches(coaches)
        database.teamDao().insertTeams(teamEntities)
    }

}