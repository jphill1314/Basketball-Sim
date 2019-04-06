package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.simulation.SimulationContract
import javax.inject.Inject

class TournamentPresenter @Inject constructor(
    private val repository: TournamentContract.Repository,
    private val gameSimRepository: SimulationContract.GameSimRepository
) : TournamentContract.Presenter {

    private var view: TournamentContract.View? = null

    init {
        repository.attachPresenter(this)
        gameSimRepository.attachPresenter(this)
    }

    override fun onTournamentLoaded(games: MutableList<TournamentDataModel>) {
        view?.onTournamentLoaded(games)
    }

    override fun simToGame(gameId: Int) {
        gameSimRepository.simToGame(gameId)
    }

    override fun simGame(gameId: Int) {
        gameSimRepository.simGame(gameId)
    }

    override fun onSimCompleted() {
        repository.fetchData()
    }

    override fun onSeasonCompleted() {
        repository.fetchData()
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        view?.startGameFragment(gameId, homeName, awayName, userIsHomeTeam)
    }

    override fun onFABClicked() {
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