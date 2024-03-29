package com.appdev.jphil.basketballcoach.schedule.data

data class ScheduleDataModel(
    val gameId: Int,
    val topTeamId: Int,
    val bottomTeamId: Int,
    val topTeamName: String,
    val bottomTeamName: String,
    val topTeamScore: Int,
    val bottomTeamScore: Int,
    val isConferenceGame: Boolean,
    val isInProgress: Boolean,
    val isFinal: Boolean,
    val isHomeTeamUser: Boolean,
    val tournamentId: Int?
)
