package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameEntity

interface StandingsContract {

    interface View : MVPContract.View {
        fun addConferenceNames(names: List<String>)
        fun addTeams(standingsDataModels: List<StandingsDataModel>)
        fun changeTeamAndConference(standingsDataModel: StandingsDataModel)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onTeamSelected(standingsDataModel: StandingsDataModel)
        fun onConferenceChanged(confId: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun fetchData(): StandingsModel
        suspend fun onConferenceIdChanged(confId: Int): StandingsModel
    }
}
