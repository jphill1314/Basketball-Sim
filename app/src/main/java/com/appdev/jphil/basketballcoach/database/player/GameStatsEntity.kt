package com.appdev.jphil.basketballcoach.database.player

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.players.Player

@Entity
class GameStatsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val playerId: Int,
    val gameId: Int,
    val season: Int,
    val opponent: String,
    val isHomeGame: Boolean,
    val homeScore: Int,
    val awayScore: Int,
    val timePlayed: Int,
    val twoPointAttempts: Int,
    val twoPointMakes: Int,
    val threePointAttempts: Int,
    val threePointMakes: Int,
    val assists: Int,
    val offensiveRebounds: Int,
    val defensiveRebounds: Int,
    val turnovers: Int,
    val steals: Int,
    val fouls: Int,
    val freeThrowShots: Int,
    val freeThrowMakes: Int
) {

    companion object {
        fun generate(player: Player, season: Int, opponent: String, isHomeGame: Boolean, homeScore: Int, awayScore: Int, gameId: Int): GameStatsEntity {
            return GameStatsEntity(
                null,
                player.id!!,
                gameId,
                season,
                opponent,
                isHomeGame,
                homeScore,
                awayScore,
                player.timePlayed,
                player.twoPointAttempts,
                player.twoPointMakes,
                player.threePointAttempts,
                player.threePointMakes,
                player.assists,
                player.offensiveRebounds,
                player.defensiveRebounds,
                player.turnovers,
                player.steals,
                player.fouls,
                player.freeThrowShots,
                player.freeThrowMakes
            )
        }
    }
}