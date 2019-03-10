package com.appdev.jphil.basketballcoach.schedule

import android.content.res.Resources
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.util.RecordUtil
import javax.inject.Inject

class SchedulePresenter @Inject constructor(
    private val teamId: Int,
    private val resources: Resources,
    private val repository: ScheduleContract.Repository
): ScheduleContract.Presenter {

    init {
        repository.attachPresenter(this)
    }

    private var view: ScheduleContract.View? = null

    override fun fetchSchedule() {
        repository.fetchSchedule()
    }

    override fun onScheduleLoaded(games: List<GameEntity>, isUsersSchedule: Boolean) {
        val teamGames = games.filter { it.homeTeamId == teamId || it.awayTeamId == teamId }
        val dataModels = mutableListOf<ScheduleDataModel>()
        teamGames.forEach { game ->
            val homeTeamRecord = RecordUtil.getRecordAsPair(games, game.homeTeamId)
            val homeRecord = resources.getString(R.string.standings_dash, homeTeamRecord.first, homeTeamRecord.second)
            val awayTeamRecord = RecordUtil.getRecordAsPair(games, game.awayTeamId)
            val awayRecord = resources.getString(R.string.standings_dash, awayTeamRecord.first, awayTeamRecord.second)
            dataModels.add(ScheduleDataModel(
                game.id ?: 0,
                game.homeTeamName,
                homeRecord,
                game.homeScore,
                game.awayTeamName,
                awayRecord,
                game.awayScore,
                game.isFinal)
            )
        }
        view?.hideProgressBar()
        view?.displaySchedule(dataModels, isUsersSchedule)

    }

    override fun onFABClicked() {
        view?.disableFab()
        view?.showProgressBar()
        repository.simulateNextGame()
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        view?.startGameFragment(gameId, homeName, awayName, userIsHomeTeam)
    }

    override fun simulateToGame(gameId: Int) {
        view?.showProgressBar()
        repository.simulateToGame(gameId)
    }

    override fun simulateGame(gameId: Int) {
        view?.showProgressBar()
        repository.simulateGame(gameId)
    }

    override fun onViewAttached(view: ScheduleContract.View) {
        this.view = view
        fetchSchedule()
    }

    override fun onViewDetached() {
        view = null
    }

    override fun onDestroyed() {

    }
}