package com.appdev.jphil.basketballcoach.rankings

import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel

interface RankingsContract {

    interface View : MVPContract.View {
        fun displayData(teams: List<TeamStatsDataModel>)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun onData(teams: List<TeamStatsDataModel>)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
    }
}