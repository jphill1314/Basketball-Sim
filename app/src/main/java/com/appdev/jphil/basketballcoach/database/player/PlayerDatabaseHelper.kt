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

    fun deletePlayer(player: Player, database: BasketballDatabase) {
        database.playerDao().deleteGameStatsForPlayer(player.id!!)
        database.playerDao().deletePlayer(PlayerEntity.from(player))
    }
}