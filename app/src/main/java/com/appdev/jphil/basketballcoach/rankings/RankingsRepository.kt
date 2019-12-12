package com.appdev.jphil.basketballcoach.rankings

import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import kotlinx.coroutines.*
import javax.inject.Inject

class RankingsRepository @Inject constructor(
    private val database: BasketballDatabase
) : RankingsContract.Repository {

    lateinit var presenter: RankingsContract.Presenter

    override fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
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

            dataModels.forEach { team ->
                // TODO: fix adjustments to not be so bad
                team.calculateAdjStats(rawTempo, rawEff, games, teams)
            }

            dataModels.sortByDescending { it.getAdjEff() }

            withContext(Dispatchers.Main) {
                presenter.onData(dataModels)
            }
        }
    }

    override fun attachPresenter(presenter: RankingsContract.Presenter) {
        this.presenter = presenter
    }
}