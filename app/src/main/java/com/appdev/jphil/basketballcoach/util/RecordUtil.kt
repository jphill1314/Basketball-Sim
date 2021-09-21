package com.appdev.jphil.basketballcoach.util

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity
import com.appdev.jphil.basketballcoach.stats.TeamStandingModel

object RecordUtil {

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

    fun getTeamRecord(games: List<GameEntity>, team: TeamEntity): TeamStandingModel {
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
        return TeamStandingModel(
            teamId = team.teamId,
            teamName = team.schoolName,
            confWins = confWins,
            confLoses = confLoses,
            wins = wins,
            loses = loses
        )
    }
}
