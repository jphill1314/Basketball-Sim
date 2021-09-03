package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.tournament.NationalChampionship
import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDao
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import timber.log.Timber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    data class ChampionshipLoadingState(
        val stepCounter: Int = 0,
        val teamNames: List<String> = emptyList(),
        val isFinished: Boolean = false
    )

    private val _state = MutableStateFlow(ChampionshipLoadingState())
    val state = _state.asStateFlow()

    suspend fun createNationalChampionship() {
        Timber.d("Step 1")
        startNextStep()
        val games = gameDao.getAllGames()
        val dataModels = mutableListOf<TeamStatsDataModel>()
        val teams = mutableMapOf<Int, TeamStatsDataModel>()

        teamDao.getAllTeams().forEach { team ->
            dataModels.add(TeamStatsDataModel(team))
            teams[team.teamId] = dataModels.last()
        }

        Timber.d("Step 2")
        startNextStep()
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

        Timber.d("Step 3")
        startNextStep()
        totalTempo = 0.0
        totalOffEff = 0.0
        totalDefEff = 0.0
        dataModels.forEach { team ->
            team.calculateAdjStats(rawTempo, rawEff, games, teams)
            totalTempo += team.adjTempo
            totalOffEff += team.adjOffEff
            totalDefEff += team.adjDefEff
        }

        Timber.d("Step 4")
        startNextStep()
        // TODO: rate all teams by more than just adj. eff.
        dataModels.sortByDescending { it.getAdjEff() }

        val conferenceChampsIds = conferenceDao.getConferenceChampionIds()
        val autoTeams = conferenceChampsIds.map { id -> dataModels.first { it.teamId == id } }
        val atLargeTeams = dataModels.filter {
            !conferenceChampsIds.contains(it.teamId)
        }.take(CHAMPIONSHIP_SIZE - autoTeams.size)

        val allRecruits = RecruitDatabaseHelper.loadAllRecruits(database)
        val allTeams = (autoTeams + atLargeTeams).sortedByDescending { it.getAdjEff() }.map { dataModel ->
            addTeam(dataModel.teamName)
            val teamRelations = relationalDao.loadTeamById(dataModel.teamId)
            GameDatabaseHelper.createTeam(teamRelations, allRecruits)
        }

        Timber.d("Step 5")
        // TODO: this is by far the longest part
        // maybe that means I should move the team stuff here
        // It could be announcing the pairngs too!!!
        startNextStep()
        val tournament = NationalChampionship(
            id = NATIONAL_CHAMPIONSHIP_ID,
            teams = allTeams
        )
        tournament.generateNextRound(2018)
        tournament.games.map {
            GameEntity.from(
                it,
                homeTeamSeed = it.homeTeam.postSeasonTournamentSeed,
                awayTeamSeed = it.awayTeam.postSeasonTournamentSeed
            )
        }.let { gameDao.insertGames(it) }
        tournament.teams.forEach { TeamDatabaseHelper.saveTeam(it, database) }

        _state.update { _state.value.copy(isFinished = true) }
    }

    private fun startNextStep() {
        _state.update { _state.value.copy(stepCounter = _state.value.stepCounter + 1) }
    }

    private fun addTeam(name: String) {
        val currentState = _state.value
        val seed = (currentState.teamNames.size / 4) + 1
        _state.update {
            currentState.copy(teamNames = currentState.teamNames + listOf("#$seed) $name"))
        }
    }
}
