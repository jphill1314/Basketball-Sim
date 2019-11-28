package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketball.datamodels.ScheduleDataModel
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.simdialog.SimDialogDataModel
import com.appdev.jphil.basketballcoach.simdialog.SimDialogState
import com.appdev.jphil.basketballcoach.simulation.SimulationContract

interface ScheduleContract {

    interface View : MVPContract.View {
        fun displaySchedule(games: List<ScheduleDataModel>, isUsersSchedule: Boolean)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
        fun goToConferenceTournament()
        fun showProgressBar()
        fun setDialogState(state: SimDialogState)
        fun hideProgressBar()
    }

    interface Presenter : MVPContract.Presenter<View>, SimulationContract.GameSimPresenter {
        fun fetchSchedule()
        fun onScheduleLoaded(games: List<GameEntity>, isUsersSchedule: Boolean)
        fun playGame(gameId: Int)
        fun simulateGame(gameId: Int)
        fun goToConferenceTournament()
        fun finishSeason()
        fun cancelSim()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchSchedule()
        suspend fun tournamentIsOver(confId: Int): Boolean
    }
}