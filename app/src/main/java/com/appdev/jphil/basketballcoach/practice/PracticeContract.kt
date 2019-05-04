package com.appdev.jphil.basketballcoach.practice

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.MVPContract

interface PracticeContract {

    interface View : MVPContract.View {
        fun displayPracticeInfo(team: Team)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onTeamLoaded(team: Team)
        fun onPracticeTypeChanged(type: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadTeam()
        fun saveTeam(team: Team)
    }
}