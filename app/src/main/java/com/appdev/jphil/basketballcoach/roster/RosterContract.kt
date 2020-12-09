package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.MVPContract

interface RosterContract {

    interface View : MVPContract.View {
        fun displayData(players: MutableList<RosterDataModel>, team: Team)
        fun updateTeamAndConference(teamId: Int, conferenceId: Int)
        fun gotoPlayerOverview(playerId: Int)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onPlayerSelected(player: Player)
        fun onPlayerLongPressed(player: Player)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun fetchData(): Team
        suspend fun saveTeam(team: Team)
    }
}
