package com.appdev.jphil.basketballcoach.rankings

import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import javax.inject.Inject

class RankingsRepository @Inject constructor(
    private val database: BasketballDatabase
) : RankingsContract.Repository {

    lateinit var presenter: RankingsContract.Presenter

    override suspend fun fetchData(): List<TeamStatsDataModel> {
        val teams = mutableMapOf<Int, TeamStatsDataModel>()
        val dataModels = mutableListOf<TeamStatsDataModel>()
        TeamDatabaseHelper.loadAllTeams(database).forEach { team ->
            dataModels.add(TeamStatsDataModel(team))
            teams[team.teamId] = dataModels.last()
        }
        val games = GameDatabaseHelper.loadAllGameEntities(database)

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

        dataModels.add(TeamStatsDataModel("Average", -1, -1, false, TeamColor.Red).apply {
            adjTempo = totalTempo / teams.size
            adjOffEff = totalOffEff / teams.size
            adjDefEff = totalDefEff / teams.size
        })
        dataModels.sortByDescending { it.getAdjEff() }

        return dataModels
    }

    override fun attachPresenter(presenter: RankingsContract.Presenter) {
        this.presenter = presenter
    }
}
