package com.appdev.jphil.basketballcoach.main

import javax.inject.Inject

class TeamManagerImpl @Inject constructor() : TeamManager {

    private var teamId = -1
    private var conferenceId = 0

    override fun changeTeam(teamId: Int) {
        this.teamId = teamId
    }

    override fun changeConference(conferenceId: Int, teamId: Int) {
        this.conferenceId = conferenceId
        this.teamId = teamId
    }

    override fun getTeamId(): Int = teamId

    override fun getConferenceId(): Int = conferenceId
}