package com.appdev.jphil.basketballcoach.database

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.coach.ScoutingAssignmentEntity
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerProgressionEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

object BatchInsertHelper {

    suspend fun saveConferences(conferences: List<Conference>, database: BasketballDatabase) {
        val teams = mutableListOf<Team>()
        conferences.forEach { teams.addAll(it.teams) }
        saveTeams(teams, database)
        ConferenceDatabaseHelper.saveOnlyConferences(conferences, database)
    }

    suspend fun saveTeams(teams: List<Team>, database: BasketballDatabase) {
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

    suspend fun saveGamesTeamsAndRecruits(
        games: List<Game>,
        teams: List<Team>,
        recruits: List<Recruit>,
        database: BasketballDatabase
    ) {
        saveTeams(teams, database)

        val gameEntities = mutableListOf<GameEntity>()
        val stats = mutableListOf<GameStatsEntity>()
        games.forEach { game ->
            gameEntities.add(GameEntity.from(game))
            stats.addAll(GameDatabaseHelper.getStats(game))
        }
        database.gameDao().insertGames(gameEntities)
        database.playerDao().insertGameStats(stats)

        RecruitDatabaseHelper.saveRecruits(recruits, database)
    }
}
