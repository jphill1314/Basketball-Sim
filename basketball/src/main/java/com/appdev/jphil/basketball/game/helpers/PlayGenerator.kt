package com.appdev.jphil.basketball.game.helpers

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay

object PlayGenerator {

    fun getNextPlay(game: Game): List<BasketballPlay> = when {
        isShootingFreeThrows(game) -> MiscPlays.getFreeThrows(game)
        isIntentionalFoul(game) -> MiscPlays.getIntentionalFoul(game)
        isInFastBreak(game) -> FastBreakPlays.getFastBreakPlay(game)
        isInBackCourt(game) -> BackCourtPlays.getBackCourtPlay(game)
        else -> FrontCourtPlays.getFrontCourtPlay(game)
    }

    private fun isShootingFreeThrows(game: Game) = game.shootFreeThrows
    private fun isIntentionalFoul(game: Game) = MiscPlays.canIntentionalFoul(game)
    private fun isInFastBreak(game: Game) = game.gamePlays.lastOrNull()?.leadToFastBreak == true
    private fun isInBackCourt(game: Game) = game.location == -1
}