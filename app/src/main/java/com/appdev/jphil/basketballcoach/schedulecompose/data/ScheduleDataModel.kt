package com.appdev.jphil.basketballcoach.schedulecompose.data

data class ScheduleDataModel(
    val gameId: Int,
    val topTeamId: Int,
    val bottomTeamId: Int,
    val topTeamName: String,
    val bottomTeamName: String,
    val topTeamScore: Int,
    val bottomTeamScore: Int,
    val isInProgress: Boolean,
    val isFinal: Boolean
)