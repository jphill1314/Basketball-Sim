package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.MVPContract

interface RosterContract {

    interface View : MVPContract.View {
        fun displayData(players: MutableList<RosterDataModel>, isUsersTeam: Boolean)
        fun updateTeamAndConference(teamId: Int, conferenceId: Int)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onDataFetched(team: Team)
        fun onPlayerSelected(player: Player)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchData()
        fun saveTeam(team: Team)
    }
}