package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.teams.Team
import kotlin.random.Random

abstract class BasketballPlay(val game: Game) {
    var homeTeamHasBall = game.homeTeamHasBall
    var timeRemaining = game.timeRemaining
    var shotClock = game.shotClock
    val homeTeam = game.homeTeam
    val awayTeam = game.awayTeam
    var playerWithBall = game.playerWithBall
    var location = game.location

    lateinit var type: Plays // what kind of play? Pass, turnover, shot, foul, etc
    var points = 0 // were points scored on this play?
    var playAsString = ""
    val r = Random
    val homeTeamStartsWithBall = homeTeamHasBall

    val offense: Team = if (homeTeamHasBall) homeTeam else awayTeam
    val defense: Team = if (homeTeamHasBall) awayTeam else homeTeam

    var leadToFastBreak = false

    lateinit var foul: Foul

    abstract fun generatePlay(): Int

    fun getTimeChangePaceDependent(maxChange: Int, minChange: Int): Int {
        val timeChange = (maxChange + 1 - (offense.pace / 90.0) * r.nextInt(maxChange + 1 - minChange)).toInt()
        return smartTimeChange(timeChange)
    }

    fun getTimeChangePaceIndependent(maxChange: Int, minChange: Int): Int {
        return smartTimeChange(r.nextInt(minChange, maxChange + 1))
    }

    private fun smartTimeChange(timeChange: Int): Int {
        return if (timeChange > shotClock) {
            shotClock
        } else {
            timeChange
        }
    }

    companion object {
        const val randomBound = 30
    }
}
