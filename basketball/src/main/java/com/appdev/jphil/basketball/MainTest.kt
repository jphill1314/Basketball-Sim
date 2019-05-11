package com.appdev.jphil.basketball

import com.appdev.jphil.basketball.factories.TeamFactory
import com.appdev.jphil.basketball.game.Game
import java.lang.Math.*

fun main(){
//    val homeTeam = Team(1, "Home", 70, true)
//    val awayTeam = Team(2, "Away", 70, true)
//    println("Home team: ${homeTeam.teamRating} vs. Away team: ${awayTeam.teamRating}")
//
//    val game = Game(homeTeam, awayTeam, true)
//    game.simulateFullGame()
//
//    println(game.getAsString())
//    println("Number of plays:${game.gamePlays.size}")
//    println("Possessions: ${game.possessions/2}")
//    println("\n${game.homeTeam.getStatsAsString()}")
//    println("\n${game.awayTeam.getStatsAsString()}")
//    for(player in homeTeam.roster){
//        println(player.getStatsAsString()+"\n")
//    }

    val homeRating = 70
    val awayRating = 70

    var homeWins = 0
    var otGames = 0
    var totalMargin = 0
    var totalScore = 0
    var highScore: Game? = null
    var lowScore: Game? = null
    var maxMargin: Game? = null
    var minMargin: Game? = null
    val totalGames = 500

    for(i in 1..totalGames) {
        val homeTeam = TeamFactory.generateTeam(1, "home", "team","home", homeRating, 1, false, listOf("first"), listOf("last"))
        val awayTeam = TeamFactory.generateTeam(2, "away", "team", "away", awayRating, 1, false, listOf("first"), listOf("last"))
        val game = Game(homeTeam, awayTeam, true, 1)
        if(highScore == null){
            highScore = game
            lowScore = game
            maxMargin = game
            minMargin = game
        }

        game.simulateFullGame()

        if(game.homeScore > game.awayScore){
            homeWins++
        }
        if(game.half > 2){
            otGames++
        }

        if(max(game.homeScore, game.awayScore) > max(highScore.homeScore, highScore.awayScore)){
            highScore = game
        }

        if(min(game.homeScore, game.awayScore) < min(lowScore!!.homeScore, lowScore.awayScore)){
            lowScore = game
        }

        if(abs(game.homeScore - game.awayScore) > abs(maxMargin!!.homeScore - maxMargin.awayScore)){
            maxMargin = game
        }

        if(abs(game.homeScore - game.awayScore) < abs(minMargin!!.homeScore - minMargin.awayScore)){
            minMargin = game
        }

        totalMargin += abs(game.homeScore - game.awayScore)
        totalScore += game.homeScore
        totalScore += game.awayScore
    }

    println("Neutral Court")
    println("home wins:$homeWins-${homeWins.toDouble()/totalGames}")
    println("ot games:$otGames")
    println("average score:${totalScore/(totalGames * 2.0)}")
    println("average margin:${totalMargin/(totalGames.toDouble())}")
    println("max margin:${maxMargin!!.getAsString()}")
    println("min margin:${minMargin!!.getAsString()}")
    println("high score:${highScore!!.getAsString()}")
    println("low score:${lowScore!!.getAsString()}")
    println("-----------------------------------------------------------------")

    homeWins = 0
    otGames = 0
    totalMargin = 0
    totalScore = 0
    highScore = null
    lowScore = null
    maxMargin = null
    minMargin = null

    for(i in 1..totalGames) {
        val homeTeam = TeamFactory.generateTeam(1, "home", "team","home", homeRating, 1, false, listOf("first"), listOf("last"))
        val awayTeam = TeamFactory.generateTeam(2, "away", "team", "away", awayRating, 1, false, listOf("first"), listOf("last"))
        val game = Game(homeTeam, awayTeam, false, 1)
        if(highScore == null){
            highScore = game
            lowScore = game
            maxMargin = game
            minMargin = game
        }

        game.simulateFullGame()

        if(game.homeScore > game.awayScore){
            homeWins++
        }
        if(game.half > 2){
            otGames++
        }

        if(max(game.homeScore, game.awayScore) > max(highScore.homeScore, highScore.awayScore)){
            highScore = game
        }

        if(min(game.homeScore, game.awayScore) < min(lowScore!!.homeScore, lowScore.awayScore)){
            lowScore = game
        }

        if(abs(game.homeScore - game.awayScore) > abs(maxMargin!!.homeScore - maxMargin.awayScore)){
            maxMargin = game
        }

        if(abs(game.homeScore - game.awayScore) < abs(minMargin!!.homeScore - minMargin.awayScore)){
            minMargin = game
        }

        totalMargin += abs(game.homeScore - game.awayScore)
        totalScore += game.homeScore
        totalScore += game.awayScore
    }

    println("Home court adv")
    println("home wins:$homeWins-${homeWins.toDouble()/totalGames}")
    println("ot games:$otGames")
    println("average score:${totalScore/(totalGames * 2.0)}")
    println("average margin:${totalMargin/(totalGames.toDouble())}")
    println("max margin:${maxMargin!!.getAsString()}")
    println("min margin:${minMargin!!.getAsString()}")
    println("high score:${highScore!!.getAsString()}")
    println("low score:${lowScore!!.getAsString()}")
}