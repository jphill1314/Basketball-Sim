package com.appdev.jphil.basketballcoach.database.player

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object PlayerDatabaseHelper {

    fun loadPlayerById(id: Int, database: BasketballDatabase): Player {
        val player = database.playerDao().getPlayerById(id)
        return player.createPlayer()
    }

    fun loadGameStatsForPlayer(playerId: Int, database: BasketballDatabase): List<GameStatsEntity> {
        return database.playerDao().getAllGamesForPlayer(playerId)
    }
}