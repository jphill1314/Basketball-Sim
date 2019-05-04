package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.game.GameEntity

interface StandingsContract {

    interface View : MVPContract.View {
        fun addTeams(standingsDataModels: List<StandingsDataModel>)
        fun changeTeamAndConference(teamId: Int, conferenceId: Int)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onData(teams: List<Team>, games: List<GameEntity>)
        fun onTeamSelected(standingsDataModel: StandingsDataModel)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
    }
}