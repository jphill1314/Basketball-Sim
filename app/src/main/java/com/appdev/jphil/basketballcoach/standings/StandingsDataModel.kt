package com.appdev.jphil.basketballcoach.standings

data class StandingsDataModel(
    val teamId: Int,
    val conferenceId: Int,
    val teamName: String,
    val conferenceWins: Int,
    val conferenceLoses: Int,
    val totalWins: Int,
    val totalLoses: Int
)