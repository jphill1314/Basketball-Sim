package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team


class Press(homeTeamHasBall: Boolean,
            timeRemaining: Int,
            shotClock: Int,
            homeTeam: Team,
            awayTeam: Team,
            playerWithBall: Int,
            location: Int,
            val deadBall: Boolean
) : BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    private var playerStartsWithBall = playerWithBall
    private lateinit var passer: Player
    private lateinit var passDefender: Player
    private lateinit var target: Player
    private lateinit var targetDefender: Player

    private val passingUtils = PassingUtils(randomBound)

    init {
        type = Plays.PRESS
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        if (deadBall) {
            playerStartsWithBall = passingUtils.getInbounder(offense)
        }
        passer = offense.getPlayerAtPosition(playerStartsWithBall)
        passDefender = defense.getPlayerAtPosition(playerStartsWithBall)

        val targetPos = passingUtils.getTarget(offense, playerWithBall)
        target = offense.getPlayerAtPosition(targetPos)
        targetDefender = defense.getPlayerAtPosition(targetPos)

        return 0
    }

}