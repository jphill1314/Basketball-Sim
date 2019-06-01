package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.textcontracts.FastBreakTextContract
import com.appdev.jphil.basketball.textcontracts.FoulTextContract

class FastBreak(game: Game): BasketballPlay(game) {

    private val fastBreakText = game.fastBreakText

    init {
        type = Plays.SHOT
        foul = Foul(game, FoulType.CLEAN)
        points = generatePlay()
    }

    // TODO: add chance for player to shoot a 3, or get fouled, or blocked, etc.
    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        val timeChange = timeUtil.smartTimeChange(r.nextInt(5), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange
        return if (r.nextInt(shooter.closeRangeShot) > 3) {
            playAsString = fastBreakText.madeShot(shooter)
            homeTeamHasBall = !homeTeamHasBall
            2
        } else {
            playAsString = fastBreakText.missedShot(shooter)
            0
        }
    }
}