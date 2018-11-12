package plays

import Team
import java.util.*

abstract class BasketballPlay(var homeTeamHasBall: Boolean, // did the home team have the ball at the start of the play
                              var timeRemaining: Int,
                              var shotClock: Int,
                              val homeTeam: Team,
                              val awayTeam: Team,
                              var playerWithBall: Int,
                              var location: Int) {

    lateinit var type: Plays // what kind of play? Pass, turnover, shot, foul, etc
    var points = 0// were points scored on this play?
    val randomBound = 30
    val r = Random()
    val timeUtil = TimeUtil()

    val offense: Team = if(homeTeamHasBall) homeTeam else awayTeam
    val defense: Team = if(homeTeamHasBall) awayTeam else homeTeam

    lateinit var foul: Foul

    abstract fun getPlayAsString(): String
    abstract fun generatePlay(): Int
}