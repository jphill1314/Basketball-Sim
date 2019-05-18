package com.appdev.jphil.basketballcoach.database.player

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerProgression
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object PlayerDatabaseHelper {

    fun loadPlayerById(id: Int, database: BasketballDatabase): Player {
        return createPlayer(database.playerDao().getPlayerById(id), database)
    }

    fun loadGameStatsForPlayer(playerId: Int, database: BasketballDatabase): List<GameStatsEntity> {
        return database.playerDao().getAllGamesForPlayer(playerId)
    }

    fun loadAllPlayersOnTeam(teamId: Int, database: BasketballDatabase): MutableList<Player> {
        val players = mutableListOf<Player>()
        database.playerDao().getPlayersOnTeam(teamId).forEach {
            players.add(createPlayer(it, database))
        }
        return players
    }

    private fun createPlayer(playerEntity: PlayerEntity, database: BasketballDatabase): Player {
        val player = playerEntity.createPlayer()
        val progressions = database.playerDao().getProgressForPlayer(playerEntity.id!!)
        progressions.sortedBy { it.progressionNumber }.forEach {
            player.progression.add(it.createProgression(player))
        }
        return player
    }

    fun deletePlayer(player: Player, database: BasketballDatabase) {
        database.playerDao().deleteGameStatsForPlayer(player.id!!)
        database.playerDao().deleteProgressionForPlayer(player.id!!)
        database.playerDao().deletePlayer(PlayerEntity.from(player))
    }
}