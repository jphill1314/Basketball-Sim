package com.appdev.jphil.basketballcoach.database

import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.Team
import javax.inject.Inject

class DatabaseHelper @Inject constructor(private val database: BasketballDatabase) {

    fun loadTeamById(teamId: Int): Team? {
        val teamEntity = database.teamDao().getTeamWithId(teamId)
        return if (teamEntity == null) {
            null
        } else {
            val players = database.playerDao().getPlayersOnTeam(teamId)
            teamEntity.createTeam(players)
        }
    }

    fun loadConferenceById(conferenceId: Int): Conference? {
        val conferenceEntity = database.conferenceDao().getConferenceWithId(conferenceId)
        return if (conferenceEntity == null) {
            null
        } else {
            val teamEntities = database.teamDao().getTeamsInConference(conferenceId)
            val teams = mutableListOf<Team>()
            teamEntities.forEach { entity ->
                val players = database.playerDao().getPlayersOnTeam(entity.teamId)
                teams.add(entity.createTeam(players))
            }
            Conference(conferenceEntity.id, conferenceEntity.name, teams)
        }
    }

    fun saveTeam(team: Team, conferenceId: Int) {
        team.roster.forEach { player -> database.playerDao().insertPlayer(PlayerEntity(player)) }
        database.teamDao().insertTeam(TeamEntity(team, conferenceId))
    }

    fun saveConference(conference: Conference) {
        conference.teams.forEach { team -> saveTeam(team, conference.id) }
        database.conferenceDao().insertConference(ConferenceEntity(conference.id, conference.name))
    }
}