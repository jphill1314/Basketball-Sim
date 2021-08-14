package com.appdev.jphil.basketballcoach.util

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.game.GameEntity

object RecordUtil {

    fun getRecordAsPair(games: List<GameEntity>, teamId: Int): Pair<Int, Int> {
        var wins = 0
        var loses = 0
        games.forEach { game ->
            if (game.isFinal) {
                when (teamId) {
                    game.homeTeamId -> {
                        if (game.homeScore > game.awayScore) {
                            wins++
                        } else {
                            loses++
                        }
                    }
                    game.awayTeamId -> {
                        if (game.awayScore > game.homeScore) {
                            wins++
                        } else {
                            loses++
                        }
                    }
                }
            }
        }
        return Pair(wins, loses)
    }

    fun getRecord(games: List<GameEntity>, team: Team): StandingsDataModel {
        var wins = 0
        var loses = 0
        var confWins = 0
        var confLoses = 0
        games.forEach { game ->
            if (game.isFinal) {
                when (team.teamId) {
                    game.homeTeamId -> {
                        if (game.homeScore > game.awayScore) {
                            wins++
                            if (game.isConferenceGame) {
                                confWins++
                            }
                        } else {
                            loses++
                            if (game.isConferenceGame) {
                                confLoses++
                            }
                        }
                    }
                    game.awayTeamId -> {
                        if (game.awayScore > game.homeScore) {
                            wins++
                            if (game.isConferenceGame) {
                                confWins++
                            }
                        } else {
                            loses++
                            if (game.isConferenceGame) {
                                confLoses++
                            }
                        }
                    }
                }
            }
        }
        return StandingsDataModel(
            team.teamId,
            team.conferenceId,
            team.name,
            confWins,
            confLoses,
            wins,
            loses,
            team.color
        )
    }
}
