package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketball.datamodels.ScheduleDataModel
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.simulation.SimulationContract

interface ScheduleContract {

    interface View : MVPContract.View {
        fun displaySchedule(games: List<ScheduleDataModel>, isUsersSchedule: Boolean)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
        fun goToConferenceTournament()
        fun showProgressBar()
        fun hideProgressBar()
        fun disableFab()
        fun enableFab()
    }

    interface Presenter : MVPContract.Presenter<View>, SimulationContract.GameSimPresenter {
        fun fetchSchedule()
        fun onScheduleLoaded(games: List<GameEntity>, isUsersSchedule: Boolean)
        fun onFABClicked()
        fun simulateToGame(gameId: Int)
        fun simulateGame(gameId: Int)
        fun goToConferenceTournament()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchSchedule()
    }
}