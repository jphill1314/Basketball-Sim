package com.appdev.jphil.basketballcoach.database.player

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerProgression

@Entity
data class PlayerProgressionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val playerId: Int,
    val closeRangeShot: Double,
    val midRangeShot: Double,
    val longRangeShot: Double,
    val freeThrowShot: Double,
    val postMove: Double,
    val ballHandling: Double,
    val passing: Double,
    val offBallMovement: Double,
    val postDefense: Double,
    val perimeterDefense: Double,
    val onBallDefense: Double,
    val offBallDefense: Double,
    val stealing: Double,
    val rebounding: Double,
    val stamina: Double
) {

    fun createProgression(player: Player): PlayerProgression {
        return PlayerProgression(
            id,
            player,
            closeRangeShot,
            midRangeShot,
            longRangeShot,
            freeThrowShot,
            postMove,
            ballHandling,
            passing,
            offBallMovement,
            postDefense,
            perimeterDefense,
            onBallDefense,
            offBallDefense,
            stealing,
            rebounding,
            stamina
        )
    }

    companion object {
        fun from(progression: PlayerProgression?): PlayerProgressionEntity {
            return PlayerProgressionEntity(
                progression?.id,
                progression?.player?.id ?: 0,
                progression?.closeRangeShot ?: 0.0,
                progression?.midRangeShot ?: 0.0,
                progression?.longRangeShot ?: 0.0,
                progression?.freeThrowShot ?: 0.0,
                progression?.postMove ?: 0.0,
                progression?.ballHandling ?: 0.0,
                progression?.passing ?: 0.0,
                progression?.offBallMovement ?: 0.0,
                progression?.postDefense ?: 0.0,
                progression?.perimeterDefense ?: 0.0,
                progression?.onBallDefense ?: 0.0,
                progression?.offBallDefense ?: 0.0,
                progression?.stealing ?: 0.0,
                progression?.rebounding ?: 0.0,
                progression?.stamina ?: 0.0
            )
        }
    }
}