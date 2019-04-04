package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketball.TenTeamTournament
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.schedule.ScheduleDataModel

interface TournamentContract {

    interface View : MVPContract.View {
        fun onTournamentLoaded(dataModels: List<ScheduleDataModel>)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun onTournamentLoaded(tournament: TenTeamTournament, allGames: List<GameEntity>)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
        fun onFABClicked()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
        fun simToGame()
    }
}