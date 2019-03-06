package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.game.GameEntity

interface StandingsContract {

    interface View : MVPContract.View {
        fun addTeams(standingsDataModels: List<StandingsDataModel>)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onData(teams: List<Team>, games: List<GameEntity>)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
    }
}