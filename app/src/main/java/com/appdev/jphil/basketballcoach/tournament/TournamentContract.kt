package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketball.TenTeamTournament
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.schedule.ScheduleDataModel
import com.appdev.jphil.basketballcoach.simulation.SimulationContract

interface TournamentContract {

    interface View : MVPContract.View {
        fun onTournamentLoaded(dataModels: MutableList<ScheduleDataModel>)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
    }

    interface Presenter : MVPContract.Presenter<View>, SimulationContract.GameSimPresenter {
        fun onTournamentLoaded(tournament: TenTeamTournament, allGames: MutableList<GameEntity>)
        fun onFABClicked()
        fun simToGame(gameId: Int)
        fun simGame(gameId: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
    }
}