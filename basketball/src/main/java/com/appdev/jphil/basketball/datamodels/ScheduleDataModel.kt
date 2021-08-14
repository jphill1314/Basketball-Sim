package com.appdev.jphil.basketball.datamodels

data class ScheduleDataModel(
    var gameId: Int,
    var homeTeamName: String,
    var homeTeamRecord: String,
    var homeTeamScore: Int,
    var awayTeamName: String,
    var awayTeamRecord: String,
    var awayTeamScore: Int,
    var isFinal: Boolean,
    var isVictory: Boolean,
    var inProgress: Boolean
)
