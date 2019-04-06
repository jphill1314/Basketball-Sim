package com.appdev.jphil.basketball.datamodels

import com.appdev.jphil.basketball.game.Game

data class ScheduleDataModel(
    var gameId: Int,
    var homeTeamName: String,
    var homeTeamRecord: String,
    var homeTeamScore: Int,
    var awayTeamName: String,
    var awayTeamRecord: String,
    var awayTeamScore: Int,
    var isFinal: Boolean,
    var inProgress: Boolean
) {

    companion object {
        fun from(game: Game, homeTeamRecord: String, awayTeamRecord: String): ScheduleDataModel {
            return ScheduleDataModel(
                game.id ?: 0,
                game.homeTeam.name,
                homeTeamRecord,
                game.homeScore,
                game.awayTeam.name,
                awayTeamRecord,
                game.awayScore,
                game.isFinal,
                game.inProgress
            )
        }

        fun emptyDataModel(): ScheduleDataModel {
            return ScheduleDataModel(
                0,
                "",
                "",
                0,
                "",
                "",
                0,
                false,
                false
            )
        }
    }
}