package com.appdev.jphil.basketballcoach.advancedmetrics

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

class TeamStatsDataModel(
    val teamName: String,
    val teamId: Int,
    val confId: Int,
    val isUser: Boolean,
    val color: TeamColor
) {
    var rawTempo = 0.0
    var rawOffEff = 0.0
    var rawDefEff = 0.0

    var adjTempo = 0.0
    var adjOffEff = 0.0
    var adjDefEff = 0.0

    constructor(team: TeamEntity) : this(
        team.schoolName,
        team.teamId,
        team.conferenceId,
        team.isUser,
        TeamColor.fromInt(team.color)
    )

    fun getAdjEff() = adjOffEff - adjDefEff

    fun getRawEff() = rawOffEff - rawDefEff

    fun calculateRawStats(allGames: List<GameEntity>) {
        val games = allGames.filter { it.isFinal && (it.homeTeamId == teamId || it.awayTeamId == teamId) }
        if (games.isEmpty()) {
            return
        }

        rawTempo = calcRawTempo(games)
        rawOffEff = calcRawOffEff(games)
        rawDefEff = calcRawDefEff(games)
    }

    fun calculateAdjStats(aveTempo: Double, aveEff: Double, allGames: List<GameEntity>, allTeams: Map<Int, TeamStatsDataModel>) {
        val gamesAndOpponents = mutableListOf<Pair<GameEntity, TeamStatsDataModel>>()
        allGames.filter { it.isFinal }.forEach { game ->
            if (game.homeTeamId == teamId) {
                gamesAndOpponents.add(Pair(game, allTeams[game.awayTeamId] ?: error("Team missing - id: ${game.awayTeamId}")))
            } else if (game.awayTeamId == teamId) {
                gamesAndOpponents.add(Pair(game, allTeams[game.homeTeamId] ?: error("Team missing - id: ${game.homeTeamId}")))
            }
        }
        if (gamesAndOpponents.isEmpty()) {
            return
        }

        adjTempo = calcAdjTempo(aveTempo, gamesAndOpponents)
        adjOffEff = calcAdjOffEff(aveEff, gamesAndOpponents)
        adjDefEff = calcAdjDefEff(aveEff, gamesAndOpponents)
    }

    private fun calcRawTempo(games: List<GameEntity>): Double {
        var total = 0.0
        games.forEach { game ->
            total += getPossessions(game) * getPossessionsAdjustment(game)
        }
        return total / games.size
    }

    private fun calcRawOffEff(games: List<GameEntity>): Double {
        var total = 0.0
        games.forEach { game ->
            total += getPointsScored(game) / getPossessions(game)
        }
        return total / games.size * 100
    }

    private fun calcRawDefEff(games: List<GameEntity>): Double {
        var total = 0.0
        games.forEach { game ->
            total += getPointsAllowed(game) / getPossessions(game)
        }
        return total / games.size * 100
    }

    private fun calcAdjTempo(aveTempo: Double, gamesAndOpponents: List<Pair<GameEntity, TeamStatsDataModel>>): Double {
        var total = 0.0
        gamesAndOpponents.forEach { (game, opponent) ->
            val expected = aveTempo + (opponent.rawTempo - aveTempo) + (rawTempo - aveTempo)
            val tempo = getPossessions(game) * getPossessionsAdjustment(game)
            val diff = (tempo / expected) - 1
            total += tempo * diff + rawTempo
        }
        return total / gamesAndOpponents.size
    }

    private fun calcAdjOffEff(aveEff: Double, gamesAndOpponents: List<Pair<GameEntity, TeamStatsDataModel>>): Double {
        var total = 0.0
        gamesAndOpponents.forEach { (game, opponent) ->
            val expected = rawOffEff + (opponent.rawDefEff - aveEff)
            val result = getPointsScored(game) / getPossessions(game) * 100.0
            val diff = (result / expected) - 1
            total += expected * diff + rawOffEff
        }
        return total / gamesAndOpponents.size
    }

    private fun calcAdjDefEff(aveEff: Double, gamesAndOpponents: List<Pair<GameEntity, TeamStatsDataModel>>): Double {
        var total = 0.0
        gamesAndOpponents.forEach { (game, opponent) ->
            val expected = rawDefEff + (opponent.rawOffEff - aveEff)
            val result = getPointsAllowed(game) / getPossessions(game) * 100.0
            val diff = (result / expected) - 1
            total += expected * diff + rawDefEff
        }
        return total / gamesAndOpponents.size
    }

    private fun getPointsScored(game: GameEntity): Int {
        return if (game.homeTeamId == teamId) {
            game.homeScore
        } else {
            game.awayScore
        }
    }

    private fun getPointsAllowed(game: GameEntity): Int {
        return if (game.homeTeamId != teamId) {
            game.homeScore
        } else {
            game.awayScore
        }
    }

    private fun getPossessions(gameEntity: GameEntity): Double {
        return gameEntity.possessions / 2.0
    }

    private fun getPossessionsAdjustment(gameEntity: GameEntity): Double {
        return (Game.lengthOfHalf * 2.0) / getMinutes(gameEntity)
    }

    private fun getMinutes(gameEntity: GameEntity): Double {
        return (Game.lengthOfHalf * 2.0) + (Game.lengthOfOvertime * (gameEntity.half - 2))
    }
}
