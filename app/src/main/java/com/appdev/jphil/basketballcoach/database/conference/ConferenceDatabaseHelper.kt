package com.appdev.jphil.basketballcoach.database.conference

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper

object ConferenceDatabaseHelper {

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
}
