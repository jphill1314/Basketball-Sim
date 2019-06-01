package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.plays.utils.TimeUtil
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import java.util.*

abstract class BasketballPlay(val game: Game) {
    var homeTeamHasBall = game.homeTeamHasBall
    var timeRemaining = game.timeRemaining
    var shotClock = game.shotClock
    val homeTeam = game.homeTeam
    val awayTeam = game.awayTeam
    var playerWithBall = game.playerWithBall
    var location = game.location

    lateinit var type: Plays // what kind of play? Pass, turnover, shot, foul, etc
    var points = 0// were points scored on this play?
    var playAsString = ""
    val r = Random()
    val timeUtil = TimeUtil()
    val homeTeamStartsWithBall = homeTeamHasBall

    val offense: Team = if (homeTeamHasBall) homeTeam else awayTeam
    val defense: Team = if (homeTeamHasBall) awayTeam else homeTeam

    var leadToFastBreak = false

    lateinit var foul: Foul

    abstract fun generatePlay(): Int

    companion object {
        const val randomBound = 30
    }
}