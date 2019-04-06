package com.appdev.jphil.basketball.datamodels

import com.appdev.jphil.basketball.game.Game

data class TournamentDataModel(
    var gameId: Int,
    var homeTeamName: String,
    var awayTeamName: String,
    var homeTeamScore: Int,
    var awayTeamScore: Int,
    var isFinal: Boolean,
    var inProgress: Boolean,
    val round: Int
) {

    companion object {
        const val NO_GAME_ID = -1

        fun emptyDataModel(round: Int): TournamentDataModel {
            return TournamentDataModel(
                NO_GAME_ID,
                "",
                "",
                0,
                0,
                false,
                false,
                round
            )
        }

        fun from(game: Game, round: Int): TournamentDataModel {
            return TournamentDataModel(
                game.id ?: NO_GAME_ID,
                game.homeTeam.abbreviation,
                game.awayTeam.abbreviation,
                game.homeScore,
                game.awayScore,
                game.isFinal,
                game.inProgress,
                round
            )
        }
    }
}