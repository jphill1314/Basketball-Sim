package com.appdev.jphil.basketball.datamodels

import com.appdev.jphil.basketball.teams.TeamColor

data class StandingsDataModel(
    val teamId: Int,
    val conferenceId: Int,
    val teamName: String,
    val conferenceWins: Int,
    val conferenceLoses: Int,
    val totalWins: Int,
    val totalLoses: Int,
    val teamColor: TeamColor
) {
    fun getWinPercentage(): Double {
        return if (totalLoses == 0) {
            1.0
        } else {
            totalWins / (totalWins + totalLoses * 1.0)
        }
    }

    fun getConferenceWinPercentage(): Double {
        return if (conferenceLoses == 0) {
            1.0
        } else {
            conferenceWins / (conferenceWins + conferenceLoses * 1.0)
        }
    }
}