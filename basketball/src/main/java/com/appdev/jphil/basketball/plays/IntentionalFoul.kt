package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.enums.FoulType

class IntentionalFoul(game: Game) : BasketballPlay(game) {

    init {
        generatePlay()
    }

    override fun generatePlay(): Int {
        foul = Foul(game, FoulType.INTENTIONAL)

        val timeChange = getTimeChangePaceIndependent(6, 1)
        timeRemaining -= timeChange
        shotClock -= timeChange
        playAsString = foul.playAsString

        return 0
    }
}
