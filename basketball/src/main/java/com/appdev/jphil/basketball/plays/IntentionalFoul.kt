package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.FoulTextContract

class IntentionalFoul(game: Game) : BasketballPlay(game) {

    init {
        generatePlay()
    }

    override fun generatePlay(): Int {
        foul = Foul(game, FoulType.INTENTIONAL)

        val timeChange = timeUtil.smartTimeChange(r.nextInt(5), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange
        playAsString = foul.playAsString

        return 0
    }
}