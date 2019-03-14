package com.appdev.jphil.basketballcoach.database.conference

import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper

object ConferenceDatabaseHelper {

    fun loadConferenceById(conferenceId: Int, database: BasketballDatabase): Conference? {
        val conferenceEntity = database.conferenceDao().getConferenceWithId(conferenceId)
        return conferenceEntity?.let { conference ->
            val teamEntities = database.teamDao().getTeamsInConference(conferenceId)
            val teams = mutableListOf<Team>()
            teamEntities.forEach { entity ->
                val players = database.playerDao().getPlayersOnTeam(entity.teamId)
                val coach = database.coachDao().getCoachByTeamId(entity.teamId)
                teams.add(entity.createTeam(players, coach))
            }
            Conference(conference.id, conference.name, teams)
        }
    }

    fun saveConference(conference: Conference, database: BasketballDatabase) {
        conference.teams.forEach { team -> TeamDatabaseHelper.saveTeam(team, database) }
        database.conferenceDao().insertConference(
            ConferenceEntity(
                conference.id,
                conference.name
            )
        )
    }
}