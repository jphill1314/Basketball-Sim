package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.plays.utils.TimeUtil
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import java.util.*

abstract class BasketballPlay(
    var homeTeamHasBall: Boolean, // did the home team have the ball at the start of the play
    var timeRemaining: Int,
    var shotClock: Int,
    val homeTeam: Team,
    val awayTeam: Team,
    var playerWithBall: Int,
    var location: Int,
    val foulText: FoulTextContract
) {

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

    /**
     * returns the number of points scored in the play
     * should be 0 for everything that isn't a shot or free throws
     */
    abstract fun generatePlay(): Int

    companion object {
        const val randomBound = 30
        const val homeTeamBonus = 10
    }
}