package com.appdev.jphil.basketball

import com.appdev.jphil.basketball.factories.TeamFactory
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.IntentionalFoul
import com.appdev.jphil.basketball.plays.PostMove
import com.appdev.jphil.basketball.teams.TeamColor
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    simulateGames(true)
    println("-----------------------------------------------------------------")
    simulateGames(false)
}

private fun Game.getAsString(): String {
    return "Half:$half \t ${homeTeam.name}:$homeScore - ${awayTeam.name}:$awayScore"
}

private fun simulateGames(isNeutralCourt: Boolean, totalGames: Int = 500) {
    val homeRating = 50
    val awayRating = 50

    var homeWins = 0
    var otGames = 0
    var totalMargin = 0
    var totalScore = 0
    var postMoves = 0
    var intentionalFouls = 0
    var gamesWithIntentionalFoul = 0
    var ftAttempts = 0
    var ftMakes = 0
    var twoShots = 0
    var threeShots = 0
    var twoMakes = 0
    var threeMakes = 0
    var possessions = 0
    var turnovers = 0
    var defFouls = 0
    var offFouls = 0
    var highScore: Game? = null
    var lowScore: Game? = null
    var maxMargin: Game? = null
    var minMargin: Game? = null

    for (i in 1..totalGames) {
        val homeTeam = TeamFactory.generateTeam(1, "home", "team", TeamColor.Red, "home", homeRating, 1, false, listOf("first"), listOf("last"))
        val awayTeam = TeamFactory.generateTeam(2, "away", "team", TeamColor.Red, "away", awayRating, 1, false, listOf("first"), listOf("last"))
        val game = Game(homeTeam, awayTeam, isNeutralCourt, 1, false)
        if (highScore == null) {
            highScore = game
            lowScore = game
            maxMargin = game
            minMargin = game
        }

        game.simulateFullGame()

        if (game.homeScore > game.awayScore) {
            homeWins++
        }
        if (game.half > 2) {
            otGames++
        }

        if (max(game.homeScore, game.awayScore) > max(highScore.homeScore, highScore.awayScore)) {
            highScore = game
        }

        if (min(game.homeScore, game.awayScore) < min(lowScore!!.homeScore, lowScore.awayScore)) {
            lowScore = game
        }

        if (abs(game.homeScore - game.awayScore) > abs(maxMargin!!.homeScore - maxMargin.awayScore)) {
            maxMargin = game
        }

        if (abs(game.homeScore - game.awayScore) < abs(minMargin!!.homeScore - minMargin.awayScore)) {
            minMargin = game
        }

        totalMargin += abs(game.homeScore - game.awayScore)
        totalScore += game.homeScore
        totalScore += game.awayScore
        turnovers += game.homeTeam.turnovers + game.awayTeam.turnovers
        postMoves += game.gamePlays.filterIsInstance<PostMove>().size
        twoShots += game.homeTeam.twoPointAttempts + game.awayTeam.twoPointAttempts
        threeShots += game.homeTeam.threePointAttempts + game.awayTeam.threePointAttempts
        twoMakes += game.homeTeam.twoPointMakes + game.awayTeam.twoPointMakes
        threeMakes += game.homeTeam.threePointMakes + game.awayTeam.threePointMakes
        defFouls += game.homeTeam.defensiveFouls + game.awayTeam.defensiveFouls
        offFouls += game.homeTeam.offensiveFouls + game.awayTeam.offensiveFouls
        val intFouls = game.gamePlays.filterIsInstance<IntentionalFoul>().size
        intentionalFouls += defFouls
        if (intFouls != 0) {
            gamesWithIntentionalFoul++
        }

        ftAttempts += game.homeTeam.freeThrowShots + game.awayTeam.freeThrowShots
        ftMakes += game.homeTeam.freeThrowMakes + game.awayTeam.freeThrowMakes

        possessions += game.possessions / 2
    }

    if (isNeutralCourt) {
        println("Neutral Court")
    } else {
        println("Home Court")
    }
    println("home wins: $homeWins-${homeWins.toDouble() / totalGames}")
    println("ot games: $otGames")

    val doubleGames = totalGames * 2.0
    println("average score: ${totalScore / doubleGames}")
    println("average tempo: ${possessions / totalGames}")
    println("eff: ${(totalScore / possessions.toDouble()) * 50}")

    println("average margin: ${totalMargin / (totalGames.toDouble())}")
    println("max margin: ${maxMargin!!.getAsString()}")
    println("min margin: ${minMargin!!.getAsString()}")
    println("high score: ${highScore!!.getAsString()}")
    println("low score: ${lowScore!!.getAsString()}")
    println("post moves: $postMoves -- ${postMoves / totalGames}")
    val shots = twoShots + threeShots
    println("shots: $shots -- ${shots / totalGames}")
    println("2fg: ${(twoShots / totalGames) / 2} - ${twoMakes / twoShots.toDouble()}")
    println("3fg: ${(threeShots / totalGames) / 2} - ${threeMakes / threeShots.toDouble()}")
    println("3PA/FGA: ${threeShots.toDouble() / shots}")
    println("EFG: ${(twoMakes + threeMakes * 1.5) / shots}")
    if (gamesWithIntentionalFoul != 0) {
        println("intentional fouls: $intentionalFouls -- $gamesWithIntentionalFoul -- ${intentionalFouls / gamesWithIntentionalFoul}")
    }
    println("FTA: ${ftAttempts / doubleGames}")
    println("FTA/FGA: ${ftAttempts.toDouble() / shots}") // TODO: need this be like ~31%
    println("ft %: ${ftMakes / (1.0 * ftAttempts)}")
    println("Fouls: ${(offFouls + defFouls) / doubleGames}")
    println("Off Fouls: ${offFouls / doubleGames}")
    println("Def Fouls: ${defFouls / doubleGames}")
    println("TO: ${turnovers / doubleGames}")
    println("TO%: ${turnovers / (possessions * 2.0)}")
}
