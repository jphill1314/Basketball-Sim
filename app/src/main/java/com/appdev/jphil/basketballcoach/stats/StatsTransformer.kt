package com.appdev.jphil.basketballcoach.stats

import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import com.appdev.jphil.basketballcoach.compose.arch.Transformer
import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity
import com.appdev.jphil.basketballcoach.util.RecordUtil
import javax.inject.Inject

class StatsTransformer @Inject constructor() :
    Transformer<StatsContract.DataState, StatsContract.ViewState> {

    override fun transform(dataState: StatsContract.DataState): StatsContract.ViewState {
        return when (dataState.tabIndex) {
            0 -> getStandingsState(dataState)
            1 -> getRankingsState(dataState)
            else -> StatsContract.ViewState(
                isLoading = true,
                tabIndex = 0,
                uiModels = emptyList(),
                conferenceName = "",
                dropdownOptions = emptyList()
            )
        }
    }

    private fun getStandingsState(
        dataState: StatsContract.DataState
    ): StatsContract.ViewState {
        return StatsContract.ViewState(
            isLoading = false,
            tabIndex = 0,
            uiModels = listOf(StandingsHeader) + dataState.teams
                .filter { it.conferenceId == dataState.conferenceIndex }
                .map { RecordUtil.getTeamRecord(dataState.games, it) },
            conferenceName = dataState.conferences.first { it.id == dataState.conferenceIndex }.name,
            dropdownOptions = dataState.conferences.map { it.name }
        )
    }

    private fun getRankingsState(
        dataState: StatsContract.DataState
    ): StatsContract.ViewState {
        return StatsContract.ViewState(
            isLoading = false,
            tabIndex = 1,
            uiModels = listOf(RankingsHeader) + getRankings(dataState.teams, dataState.games),
            conferenceName = "",
            dropdownOptions = emptyList()
        )
    }

    private fun getRankings(allTeams: List<TeamEntity>, games: List<GameEntity>): List<UiModel> {
        // TODO: move this logic somewhere reusable
        val teams = mutableMapOf<Int, TeamStatsDataModel>()
        val dataModels = mutableListOf<TeamStatsDataModel>()
        allTeams.forEach { team ->
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
        dataModels.sortByDescending { it.getAdjEff() }

        return dataModels.map {
            TeamRankingModel(
                teamId = it.teamId,
                teamName = it.teamName,
                eff = it.getAdjEff(),
                pace = it.adjTempo,
                offEff = it.adjOffEff,
                defEff = it.adjDefEff
            )
        }
    }
}
