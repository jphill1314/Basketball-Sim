package com.appdev.jphil.basketballcoach.util

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketball.StandingsDataModel

object RecordUtil {

    fun getRecordAsPair(games: List<GameEntity>, teamId: Int): Pair<Int, Int> {
        var wins = 0
        var loses = 0 // TODO: handle conference games separately
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

    fun getRecordAsPair(games: List<GameEntity>, team: Team): StandingsDataModel {
        var wins = 0
        var loses = 0 // TODO: handle conference games separately
        games.forEach { game ->
            if (game.isFinal) {
                when (team.teamId) {
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
        return StandingsDataModel(
            team.teamId,
            team.conferenceId,
            team.name,
            wins,
            loses,
            wins,
            loses
        )
    }
}