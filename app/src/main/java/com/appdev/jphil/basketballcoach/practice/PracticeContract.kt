package com.appdev.jphil.basketballcoach.practice

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.MVPContract

interface PracticeContract {

    interface View : MVPContract.View {
        fun displayPracticeInfo(team: Team)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onPracticeTypeChanged(type: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun loadTeam(): Team
        suspend fun saveTeam(team: Team)
    }
}
