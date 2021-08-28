package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDao
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import timber.log.Timber
import javax.inject.Inject

class NationalChampionshipHelper @Inject constructor(
    private val database: BasketballDatabase,
    private val gameDao: GameDao,
    private val teamDao: TeamDao,
    private val conferenceDao: ConferenceDao,
    private val relationalDao: RelationalDao
) {

    companion object {
        const val NATIONAL_CHAMPIONSHIP_ID = 100
        const val CHAMPIONSHIP_SIZE = 32
    }

    suspend fun createNationalChampionship() {
        Timber.d("Start generation")
        val games = gameDao.getAllGames()
        val dataModels = mutableListOf<TeamStatsDataModel>()
        val teams = mutableMapOf<Int, TeamStatsDataModel>()

        teamDao.getAllTeams().forEach { team ->
            dataModels.add(TeamStatsDataModel(team))
            teams[team.teamId] = dataModels.last()
        }

        var totalTempo = 0.0
        var totalOffEff = 0.0
        var totalDefEff = 0.0
        dataModels.forEach { team ->
            team.calculateRawStats(games)
            totalTempo += team.rawTempo
            totalOffEff += team.rawOffEff
            totalDefEff += team.rawDefEff
        }

        val rawTempo = totalTempo / dataModels.size
        val rawEff = (totalOffEff + totalDefEff) / (2 * dataModels.size)

        totalTempo = 0.0
        totalOffEff = 0.0
        totalDefEff = 0.0
        dataModels.forEach { team ->
            team.calculateAdjStats(rawTempo, rawEff, games, teams)
            totalTempo += team.adjTempo
            totalOffEff += team.adjOffEff
            totalDefEff += team.adjDefEff
        }

        Timber.d("Team are ranked")
        // TODO: rate all teams by more than just adj. eff.
        dataModels.sortByDescending { it.getAdjEff() }

        val conferenceChampsIds = conferenceDao.getConferenceChampionIds()
        val autoTeams = conferenceChampsIds.map { id -> dataModels.first { it.teamId == id } }
        val atLargeTeams = dataModels.filter {
            !conferenceChampsIds.contains(it.teamId)
        }.take(CHAMPIONSHIP_SIZE - autoTeams.size)

        val allRecruits = RecruitDatabaseHelper.loadAllRecruits(database)
        val allTeams = (autoTeams + atLargeTeams).sortedByDescending { it.getAdjEff() }.map { dataModel ->
            val teamRelations = relationalDao.loadTeamById(dataModel.teamId)
            GameDatabaseHelper.createTeam(teamRelations, allRecruits)
        }

        val tournament = Conference(
            id = NATIONAL_CHAMPIONSHIP_ID,
            name = "National Championship",
            teams = allTeams
        )
        tournament.generateTournament(emptyList())
        tournament.tournament?.generateNextRound(2018)

        tournament.tournament?.games?.map { GameEntity.from(it) }?.let { newGames ->
            gameDao.insertGames(newGames)
        }
        conferenceDao.insertConference(
            ConferenceEntity(
                tournament.id,
                tournament.name,
                false,
                -1
            )
        )
        Timber.d("Finish generation")
    }
}
