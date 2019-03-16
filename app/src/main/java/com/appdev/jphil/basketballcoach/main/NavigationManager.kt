package com.appdev.jphil.basketballcoach.main

interface NavigationManager {

    fun enableNavigation()
    fun disableNavigation()
    fun navigateToHomePage()

    fun changeTeam(teamId: Int)
    fun changeConference(conferenceId: Int, teamId: Int)
}