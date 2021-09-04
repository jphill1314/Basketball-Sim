package com.appdev.jphil.basketballcoach.database.conference

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

object ConferenceDatabaseHelper {

    suspend fun loadConferenceById(conferenceId: Int, database: BasketballDatabase): Conference? {
        val conferenceEntity = database.conferenceDao().getConferenceWithId(conferenceId)
        return conferenceEntity?.let { conference ->
            val teamEntities = database.teamDao().getTeamsInConference(conferenceId)
            Conference(
                conference.id,
                conference.name,
                generateTeams(database, teamEntities).map { (_, team) -> team }
            )
        }
    }

    suspend fun saveConference(conference: Conference, database: BasketballDatabase) {
        conference.teams.forEach { team -> TeamDatabaseHelper.saveTeam(team, database) }
        database.conferenceDao().insertConference(
            ConferenceEntity(
                conference.id,
                conference.name,
                conference.tournament?.getWinnerOfTournament() != null,
                conference.tournament?.getWinnerOfTournament()?.teamId ?: -1
            )
        )
    }

    suspend fun loadAllConferenceEntities(database: BasketballDatabase): List<ConferenceEntity> {
        return database.conferenceDao().getAllConferenceEntities()
    }

    suspend fun saveOnlyConferences(conferences: List<Conference>, database: BasketballDatabase) {
        database.conferenceDao().insertConferences(
            conferences.map {
                ConferenceEntity(
                    it.id,
                    it.name,
                    it.tournament?.getWinnerOfTournament() != null,
                    it.tournament?.getWinnerOfTournament()?.teamId ?: -1
                )
            }
        )
    }

    private suspend fun generateTeams(database: BasketballDatabase, teamEntities: List<TeamEntity>): Map<Int, Team> {
        val teams = mutableMapOf<Int, Team>()
        teamEntities.forEach { entity ->
            TeamDatabaseHelper.createTeam(entity, database)?.let {
                teams[it.teamId] = it
            }
        }
        return teams
    }
}
