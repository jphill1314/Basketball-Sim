package com.appdev.jphil.basketballcoach.database.conference

import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

object ConferenceDatabaseHelper {

    fun loadConferenceById(conferenceId: Int, database: BasketballDatabase): Conference? {
        val conferenceEntity = database.conferenceDao().getConferenceWithId(conferenceId)
        return conferenceEntity?.let { conference ->
            val teamEntities = database.teamDao().getTeamsInConference(conferenceId)
            Conference(conference.id, conference.name, generateTeams(database, teamEntities))
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

    fun loadAllConferences(database: BasketballDatabase): List<Conference> {
        val conferenceEntities = database.conferenceDao().getAllConferenceEntities()
        val conferences = mutableListOf<Conference>()
        conferenceEntities.forEach { conference ->
            val teamEntities = database.teamDao().getTeamsInConference(conference.id)
            conferences.add(Conference(conference.id, conference.name, generateTeams(database, teamEntities)))
        }
        return conferences
    }

    fun saveAllConferences(conferences: List<Conference>, database: BasketballDatabase) {
        conferences.forEach { conference -> saveConference(conference, database) }
    }

    private fun generateTeams(database: BasketballDatabase, teamEntities: List<TeamEntity>): List<Team> {
        val teams = mutableListOf<Team>()
        teamEntities.forEach { entity ->
            val team = TeamDatabaseHelper.createTeam(entity, database)
            teams.add(team!!)
        }
        return teams
    }
}