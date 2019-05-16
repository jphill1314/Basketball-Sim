package com.appdev.jphil.basketballcoach.database.player

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerProgression
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object PlayerDatabaseHelper {

    fun loadPlayerById(id: Int, database: BasketballDatabase): Player {
        val player = database.playerDao().getPlayerById(id).createPlayer()
        val progressions = database.playerDao().getProgressForPlayer(player.id!!)
        progressions.sortedBy { it.progressionNumber }.forEach {
            player.progression.add(it.createProgression(player))
        }
        return player
    }

    fun loadGameStatsForPlayer(playerId: Int, database: BasketballDatabase): List<GameStatsEntity> {
        return database.playerDao().getAllGamesForPlayer(playerId)
    }

    fun deletePlayer(player: Player, database: BasketballDatabase) {
        database.playerDao().deleteGameStatsForPlayer(player.id!!)
        database.playerDao().deleteProgressionForPlayer(player.id!!)
        database.playerDao().deletePlayer(PlayerEntity.from(player))
    }
}