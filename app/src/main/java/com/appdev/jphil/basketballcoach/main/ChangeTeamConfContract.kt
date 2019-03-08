package com.appdev.jphil.basketballcoach.main

interface ChangeTeamConfContract {

    interface Listener {
        fun changeTeam(teamId: Int)
        fun changeConference(conferenceId: Int, teamId: Int)
    }
}