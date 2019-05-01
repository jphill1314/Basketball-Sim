package com.appdev.jphil.basketballcoach.main

interface TeamManager {
    fun changeTeam(teamId: Int)
    fun changeConference(conferenceId: Int, teamId: Int)
    fun getTeamId(): Int
    fun getConferenceId(): Int
}