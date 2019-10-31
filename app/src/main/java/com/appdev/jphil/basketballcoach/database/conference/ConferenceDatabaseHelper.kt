package com.appdev.jphil.basketballcoach.database.conference

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.tournament.TenTeamTournament
import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.tournament.Tournament
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamEntity
import com.appdev.jphil.basketballcoach.util.RecordUtil

object ConferenceDatabaseHelper {

    fun loadConferenceById(conferenceId: Int, database: BasketballDatabase): Conference? {
        val conferenceEntity = database.conferenceDao().getConferenceWithId(conferenceId)
        return conferenceEntity?.let { conference ->
            val teamEntities = database.teamDao().getTeamsInConference(conferenceId)
            Conference(
                conference.id,
                conference.name,
                generateTeams(database, teamEntities)
            )
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

    fun loadAllConferenceEntities(database: BasketballDatabase): List<ConferenceEntity> {
        return database.conferenceDao().getAllConferenceEntities()
    }

    fun loadAllConferences(database: BasketballDatabase): List<Conference> {
        val conferenceEntities = database.conferenceDao().getAllConferenceEntities()
        val conferences = mutableListOf<Conference>()
        conferenceEntities.forEach { conference ->
            val teamEntities = database.teamDao().getTeamsInConference(conference.id)
            val conf = Conference(
                conference.id,
                conference.name,
                generateTeams(database, teamEntities)
            )
            conferences.add(conf)
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

    fun createTournament(conference: Conference, database: BasketballDatabase): Tournament? {
        val games = GameDatabaseHelper.loadAllGameEntities(database)
        val standings = mutableListOf<StandingsDataModel>()
        conference.teams.forEach { team -> standings.add(RecordUtil.getRecord(games, team)) }

        conference.generateTournament(standings.sortedWith(compareBy(
            { -it.getConferenceWinPercentage() },
            { -it.conferenceWins },
            { -it.getWinPercentage() },
            { -it.totalWins }
        )))

        conference.tournament?.let { tournament ->
            tournament.replaceGames(GameDatabaseHelper.loadGamesForTournament(conference.id, database))
            if (tournament.numberOfGames() == 0) {
                tournament.generateNextRound(2018) // TODO: inject
                GameDatabaseHelper.saveOnlyGames(tournament.getAllGames(), database)
                tournament.replaceGames(GameDatabaseHelper.loadGamesForTournament(conference.id, database))
            }
        }
        return conference.tournament
    }
}