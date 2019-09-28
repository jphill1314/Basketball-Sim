package com.appdev.jphil.basketballcoach.main

interface NavigationManager {

    fun enableNavigation()
    fun disableNavigation()
    fun navigateToHomePage()

    fun setToolbarTitle(title: String)
    fun setTeamNameAndRating(name: String, rating: Int, isUser: Boolean)
}