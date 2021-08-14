package com.appdev.jphil.basketballcoach.game.sim.boxscore

import com.appdev.jphil.basketball.players.Player

interface BoxScoreContract {

    fun getPlayers(): List<Player>
    fun getTextColor(player: Player, position: Int): Int

    fun onPlayerLongPressed(player: Player)
    fun onPlayerSelected(player: Player)
}
