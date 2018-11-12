import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>){
    val homeTeam = Team("Home", 50)
    val awayTeam = Team("Away", 50)
    println("Home team: ${homeTeam.getTeamRating()} vs. Away team: ${awayTeam.getTeamRating()}")

    val game = Game(homeTeam, awayTeam, true)
    game.simulateFullGame()

    println(game.getAsString())
    println("Number of plays:${game.gamePlays.size}")
    println("\n${game.homeTeam.getStatsAsString()}")
    println("\n${game.awayTeam.getStatsAsString()}")

//    var homeWins = 0
//    var otGames = 0
//    var totalMargin = 0
//    var totalScore = 0
//    var highScore: Game? = null
//    var lowScore: Game? = null
//    var maxMargin: Game? = null
//    var minMargin: Game? = null
//    val totalGames = 500
//
//    for(i in 1..totalGames) {
//        val game = Game(homeTeam, awayTeam, true)
//        if(highScore == null){
//            highScore = game
//            lowScore = game
//            maxMargin = game
//            minMargin = game
//        }
//
//        game.simulateFullGame()
//
//        if(game.homeScore > game.awayScore){
//            homeWins++
//        }
//        if(game.half > 2){
//            otGames++
//        }
//
//        if(max(game.homeScore, game.awayScore) > max(highScore.homeScore, highScore.awayScore)){
//            highScore = game
//        }
//
//        if(min(game.homeScore, game.awayScore) < min(lowScore!!.homeScore, lowScore.awayScore)){
//            lowScore = game
//        }
//
//        if(abs(game.homeScore - game.awayScore) > abs(maxMargin!!.homeScore - maxMargin.awayScore)){
//            maxMargin = game
//        }
//
//        if(abs(game.homeScore - game.awayScore) < abs(minMargin!!.homeScore - minMargin.awayScore)){
//            minMargin = game
//        }
//
//        totalMargin += game.homeScore - game.awayScore
//        totalScore += game.homeScore
//        totalScore += game.awayScore
//    }
//
//    println("Neutral Court")
//    println("home wins:$homeWins-${homeWins.toDouble()/totalGames}")
//    println("ot games:$otGames")
//    println("average score:${totalScore/(totalGames * 2.0)}")
//    println("average margin:${totalMargin/(totalGames.toDouble())}")
//    println("max margin:${maxMargin!!.getAsString()}")
//    println("min margin:${minMargin!!.getAsString()}")
//    println("high score:${highScore!!.getAsString()}")
//    println("low score:${lowScore!!.getAsString()}")
//    println("-----------------------------------------------------------------")
//
//    homeWins = 0
//    otGames = 0
//    totalMargin = 0
//    totalScore = 0
//    highScore = null
//    lowScore = null
//    maxMargin = null
//    minMargin = null
//
//    for(i in 1..totalGames) {
//        val game = Game(homeTeam, awayTeam, false)
//        if(highScore == null){
//            highScore = game
//            lowScore = game
//            maxMargin = game
//            minMargin = game
//        }
//
//        game.simulateFullGame()
//
//        if(game.homeScore > game.awayScore){
//            homeWins++
//        }
//        if(game.half > 2){
//            otGames++
//        }
//
//        if(max(game.homeScore, game.awayScore) > max(highScore.homeScore, highScore.awayScore)){
//            highScore = game
//        }
//
//        if(min(game.homeScore, game.awayScore) < min(lowScore!!.homeScore, lowScore.awayScore)){
//            lowScore = game
//        }
//
//        if(abs(game.homeScore - game.awayScore) > abs(maxMargin!!.homeScore - maxMargin.awayScore)){
//            maxMargin = game
//        }
//
//        if(abs(game.homeScore - game.awayScore) < abs(minMargin!!.homeScore - minMargin.awayScore)){
//            minMargin = game
//        }
//
//        totalMargin += game.homeScore - game.awayScore
//        totalScore += game.homeScore
//        totalScore += game.awayScore
//    }
//
//    println("Home court adv")
//    println("home wins:$homeWins")
//    println("ot games:$otGames")
//    println("average score:${totalScore/(totalGames * 2.0)}")
//    println("average margin:${totalMargin/(totalGames.toDouble())}")
//    println("max margin:${maxMargin!!.getAsString()}")
//    println("min margin:${minMargin!!.getAsString()}")
//    println("high score:${highScore!!.getAsString()}")
//    println("low score:${lowScore!!.getAsString()}")
}