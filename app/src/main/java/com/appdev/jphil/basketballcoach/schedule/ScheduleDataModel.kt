package com.appdev.jphil.basketballcoach.schedule

class ScheduleDataModel(
    val gameId: Int,
    val homeTeamName: String,
    val homeTeamRecord: String,
    val homeTeamScore: Int,
    val awayTeamName: String,
    val awayTeamRecord: String,
    val awayTeamScore: Int,
    val isFinal: Boolean
)