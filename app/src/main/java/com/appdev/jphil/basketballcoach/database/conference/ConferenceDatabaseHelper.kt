package com.appdev.jphil.basketballcoach.database.conference

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.tournament.Tournament
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEntity
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
                generateTeams(database, teamEntities).map { (_, team) -> team }
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
        val teams = database.teamDao().getAllTeams()
        val conferences = mutableListOf<Conference>()
        val games = GameDatabaseHelper.loadAllGameEntities(database).toMutableList()
        conferenceEntities.forEach { conference ->
            val confTeams = generateTeams(database, teams.filter { it.conferenceId == conference.id })
            val conf = Conference(
                conference.id,
                conference.name,
                confTeams.map { (_, team) -> team }
            )
            conf.tournament = createTournament(conf, games, confTeams, database)
            conferences.add(conf)
        }
        database.gameDao().insertGames(games)
        return conferences
    }

    fun saveAllConferences(conferences: List<Conference>, database: BasketballDatabase) {
        conferences.forEach { conference -> saveConference(conference, database) }
    }

    private fun generateTeams(database: BasketballDatabase, teamEntities: List<TeamEntity>): Map<Int, Team> {
        val teams = mutableMapOf<Int, Team>()
        teamEntities.forEach { entity ->
            TeamDatabaseHelper.createTeam(entity, database)?.let {
                teams[it.teamId] = it
            }
        }
        return teams
    }

    fun createTournament(conference: Conference, games: List<GameEntity>, teams: Map<Int, Team>, database: BasketballDatabase): Tournament? {
        if (!games.none { it.tournamentId == null && !it.isFinal }) {
            return null
        }
        conference.generateTournament(conference.teams.map { team -> RecordUtil.getRecord(games, team) })
        conference.tournament?.let { tournament ->
            updateTournament(tournament, games, teams, database)
        }
        return conference.tournament
    }

    private fun updateTournament(tournament: Tournament, games: List<GameEntity>, teams: Map<Int, Team>, database: BasketballDatabase) {
        tournament.replaceGames(games.filter { it.tournamentId == tournament.getId() }
            .map { it.createGame(teams[it.homeTeamId]!!, teams[it.awayTeamId]!!) })
        tournament.generateNextRound(2018).forEach { // TODO: inject
            it.id = database.gameDao().insertGame(GameEntity.from(it)).toInt()
        }
    }
}