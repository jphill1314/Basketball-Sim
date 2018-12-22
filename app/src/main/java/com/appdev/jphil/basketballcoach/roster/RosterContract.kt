package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.MVPContract

interface RosterContract {

    interface View : MVPContract.View {
        fun displayData(players: MutableList<Player>)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onDataFetched(team: Team)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
    }
}