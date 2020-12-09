package com.appdev.jphil.basketballcoach.rankings

import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel

interface RankingsContract {

    interface View : MVPContract.View {
        fun displayData(teams: List<TeamStatsDataModel>)
    }

    interface Presenter : MVPContract.Presenter<View>

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun fetchData(): List<TeamStatsDataModel>
    }
}
