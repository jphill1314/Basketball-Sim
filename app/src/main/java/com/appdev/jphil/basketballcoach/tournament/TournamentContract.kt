package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.simulation.SimulationContract

interface TournamentContract {

    interface View : MVPContract.View {
        fun onTournamentLoaded(dataModels: MutableList<TournamentDataModel>)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
    }

    interface Presenter : MVPContract.Presenter<View>, SimulationContract.GameSimPresenter {
        fun onTournamentLoaded(games: MutableList<TournamentDataModel>)
        fun onFABClicked()
        fun simToGame(gameId: Int)
        fun simGame(gameId: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
    }
}