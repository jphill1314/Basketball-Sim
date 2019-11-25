package com.appdev.jphil.basketballcoach.tournament

import android.content.res.Resources
import android.view.View
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.simdialog.SimDialogDataModel
import com.appdev.jphil.basketballcoach.simdialog.SimDialogState
import com.appdev.jphil.basketballcoach.simulation.SimulationContract
import javax.inject.Inject

class TournamentPresenter @Inject constructor(
    private val resources: Resources,
    private val repository: TournamentContract.Repository,
    private val gameSimRepository: SimulationContract.GameSimRepository
) : TournamentContract.Presenter {

    private var view: TournamentContract.View? = null
    private var isSimming = false
    private var state: SimDialogState? = null
    private val simGames = mutableListOf<SimDialogDataModel>()
    private var totalGames = 0
    private var teamsSaved = 0

    init {
        repository.attachPresenter(this)
        gameSimRepository.attachPresenter(this)
    }

    override fun onSimulationStarted(totalGames: Int) {
        simGames.clear()
        teamsSaved = 1
        this.totalGames = totalGames + 1
        state = SimDialogState()
        state?.let {
            it.games.postValue(simGames)
            it.text.postValue(resources.getString(R.string.sim_game_x_of_y, 1, this.totalGames))
            it.canCancel.postValue(true)
            view?.setDialogState(it)
        }
    }

    override fun updateSchedule(finishedGame: Game) {
        simGames.add(0, SimDialogDataModel(
            finishedGame.homeTeam.schoolName,
            finishedGame.awayTeam.schoolName,
            finishedGame.homeScore,
            finishedGame.awayScore
        ))
        state?.games?.postValue(simGames)
        state?.text?.postValue(if (simGames.size == totalGames) {
            resources.getString(R.string.saving_sim_results)
        } else {
            resources.getString(R.string.sim_game_x_of_y, simGames.size + 1, totalGames)
        })
    }

    override fun onTournamentLoaded(games: MutableList<TournamentDataModel>) {
        if (!isSimming) {
            view?.hideDialog()
        }
        view?.onTournamentLoaded(games)
    }

    override fun simToGame(gameId: Int) {
        isSimming = true
        view?.showDialog()
        gameSimRepository.simToGame(gameId)
    }

    override fun simGame(gameId: Int) {
        isSimming = true
        view?.showDialog()
        gameSimRepository.simGame(gameId)
    }

    override fun onSimCompleted() {
        isSimming = false
        repository.fetchData()
    }

    override fun onSeasonCompleted(conferenceTournamentIsFinished: Boolean) {
        if (!conferenceTournamentIsFinished) {
            repository.fetchData()
        } else {
            view?.hideDialog()
        }
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        view?.hideDialog()
        view?.startGameFragment(gameId, homeName, awayName, userIsHomeTeam)
    }

    override fun onFABClicked() {
        isSimming = true
        view?.showDialog()
        gameSimRepository.startNextGame()
    }

    override fun onCancelSim() {
        gameSimRepository.cancelSim()
    }

    override fun onViewAttached(view: TournamentContract.View) {
        this.view = view
        repository.fetchData()
        state?.let { view.setDialogState(it) }
    }

    override fun onViewDetached() {
        view = null
    }
}