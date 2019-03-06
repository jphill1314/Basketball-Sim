package com.appdev.jphil.basketballcoach.standings

data class StandingsDataModel(
    val teamName: String,
    val conferenceWins: Int,
    val conferenceLoses: Int,
    val totalWins: Int,
    val totalLoses: Int
)