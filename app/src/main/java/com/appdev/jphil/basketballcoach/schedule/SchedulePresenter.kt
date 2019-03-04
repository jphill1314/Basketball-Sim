package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketball.Game
import javax.inject.Inject

class SchedulePresenter @Inject constructor(
    private val repository: ScheduleContract.Repository
): ScheduleContract.Presenter {

    init {
        repository.attachPresenter(this)
    }

    private var view: ScheduleContract.View? = null

    override fun fetchSchedule() {
        repository.fetchSchedule()
    }

    override fun onScheduleLoaded(games: List<Game>) {
        view?.hideProgressBar()
        view?.displaySchedule(games)
    }

    override fun onFABClicked() {
        repository.simulateNextGame()
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String) {
        view?.startGameFragment(gameId, homeName, awayName)
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