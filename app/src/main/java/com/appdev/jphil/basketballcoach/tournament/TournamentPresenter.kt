package com.appdev.jphil.basketballcoach.tournament

import android.view.View
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketballcoach.simulation.SimulationContract
import javax.inject.Inject

class TournamentPresenter @Inject constructor(
    private val repository: TournamentContract.Repository,
    private val gameSimRepository: SimulationContract.GameSimRepository
) : TournamentContract.Presenter {

    private var view: TournamentContract.View? = null
    private var isSimming = false

    init {
        repository.attachPresenter(this)
        gameSimRepository.attachPresenter(this)
    }

    override fun updateSchedule(finishedGame: Game) {
        repository.fetchData()
    }

    override fun onTournamentLoaded(games: MutableList<TournamentDataModel>) {
        if (!isSimming) {
            view?.setProgressBarVisibility(View.GONE)
        }
        view?.onTournamentLoaded(games)
    }

    override fun simToGame(gameId: Int) {
        isSimming = true
        view?.setProgressBarVisibility(View.VISIBLE)
        gameSimRepository.simToGame(gameId)
    }

    override fun simGame(gameId: Int) {
        isSimming = true
        view?.setProgressBarVisibility(View.VISIBLE)
        gameSimRepository.simGame(gameId)
    }

    override fun onSimCompleted() {
        isSimming = false
        repository.fetchData()
    }

    override fun onSeasonCompleted(conferenceTournamentIsFinished: Boolean) {
        repository.fetchData()
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        view?.setProgressBarVisibility(View.GONE)
        view?.startGameFragment(gameId, homeName, awayName, userIsHomeTeam)
    }

    override fun onFABClicked() {
        isSimming = true
        view?.setProgressBarVisibility(View.VISIBLE)
        gameSimRepository.startNextGame()
    }

    override fun onViewAttached(view: TournamentContract.View) {
        this.view = view
        repository.fetchData()
    }

    override fun onViewDetached() {
        view = null
    }
}