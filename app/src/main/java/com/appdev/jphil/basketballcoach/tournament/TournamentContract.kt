package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.simdialog.SimDialogState
import com.appdev.jphil.basketballcoach.simulation.SimulationContract

interface TournamentContract {

    interface View : MVPContract.View {
        fun onTournamentLoaded(dataModels: MutableList<TournamentDataModel>)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
        fun showDialog()
        fun hideDialog()
        fun setDialogState(state: SimDialogState)
    }

    interface Presenter : MVPContract.Presenter<View>, SimulationContract.GameSimPresenter {
        fun onTournamentLoaded(games: MutableList<TournamentDataModel>)
        fun playGame(gameId: Int)
        fun simGame(gameId: Int)
        fun onCancelSim()
        fun onTournamentSetupComplete()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
    }
}