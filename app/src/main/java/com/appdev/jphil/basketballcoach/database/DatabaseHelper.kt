package com.appdev.jphil.basketballcoach.database

import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity
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

    fun loadGamesForTeam(teamId: Int): List<Game> {
        val gameEntities = database.gameDao().getAllGamesWithTeamId(teamId)
        val games = mutableListOf<Game>()
        gameEntities.forEach { entity ->
            val homeTeam = database.teamDao().getTeamWithId(entity.homeTeamId)!!.
                createTeam(database.playerDao().getPlayersOnTeam(entity.homeTeamId))
            val awayTeam = database.teamDao().getTeamWithId(entity.awayTeamId)!!.
                createTeam(database.playerDao().getPlayersOnTeam(entity.awayTeamId))
            games.add(
                Game(homeTeam, awayTeam, entity.isNeutralCourt, entity.id, entity.isFinal).apply {
                    homeScore = entity.homeScore
                    awayScore = entity.awayScore
                }
            )
        }
        return games
    }

    fun loadGameById(gameId: Int): Game {
        val entity = database.gameDao().getGameWithId(gameId)
        val homeTeam = database.teamDao().getTeamWithId(entity.homeTeamId)!!.
            createTeam(database.playerDao().getPlayersOnTeam(entity.homeTeamId))
        val awayTeam = database.teamDao().getTeamWithId(entity.awayTeamId)!!.
            createTeam(database.playerDao().getPlayersOnTeam(entity.awayTeamId))
        return Game(homeTeam, awayTeam, entity.isNeutralCourt, entity.id, entity.isFinal).apply {
            homeScore = entity.homeScore
            awayScore = entity.awayScore
        }
    }

    fun saveTeam(team: Team, conferenceId: Int) {
        team.roster.forEach { player -> database.playerDao().insertPlayer(
            PlayerEntity(
                player
            )
        ) }
        database.teamDao().insertTeam(TeamEntity(team, conferenceId))
    }

    fun saveConference(conference: Conference) {
        conference.teams.forEach { team -> saveTeam(team, conference.id) }
        database.conferenceDao().insertConference(
            ConferenceEntity(
                conference.id,
                conference.name
            )
        )
    }

    fun saveGames(games: List< Game>) {
        games.forEach { game -> database.gameDao().insertGame(
            GameEntity(
                game.id,
                game.homeTeam.teamId,
                game.awayTeam.teamId,
                game.isNeutralCourt,
                game.isFinal,
                game.homeScore,
                game.awayScore
            )
        )
        }
    }
}