package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game

class EndOfHalf(game: Game, gameOver: Boolean) : BasketballPlay(game) {

    init {
        val realHalf = if (gameOver) game.half else game.half - 1
        playAsString = game.miscText.endOfHalf(realHalf, gameOver)
    }

    override fun generatePlay(): Int {
        return 0
    }
}