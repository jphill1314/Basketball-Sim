package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.enums.Plays

class TipOff(game: Game) : BasketballPlay(game) {

    private val tipOffText = game.tipOffText

    init {
        type = Plays.TIP_OFF

        generatePlay()
    }

    override fun generatePlay(): Int {
        val homeCenter = homeTeam.getPlayerAtPosition(5)
        val awayCenter = awayTeam.getPlayerAtPosition(5)

        homeTeamHasBall = homeCenter.rebounding + r.nextInt(randomBound) > awayCenter.rebounding + r.nextInt(randomBound)
        playerWithBall = r.nextInt(4) + 1
        playAsString = tipOffText.tipOffText(homeTeam, awayTeam, homeTeamHasBall)

        return 0
    }
}