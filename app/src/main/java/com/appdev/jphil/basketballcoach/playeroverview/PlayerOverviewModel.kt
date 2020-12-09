package com.appdev.jphil.basketballcoach.playeroverview

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity

data class PlayerOverviewModel(
    val player: Player,
    val stats: List<GameStatsEntity>
)
