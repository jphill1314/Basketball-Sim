package com.appdev.jphil.basketballcoach.database.player

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerProgression

@Entity
data class PlayerProgressionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val playerId: Int,
    val progressionNumber: Int,
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
            progressionNumber,
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
        fun from(progression: PlayerProgression): PlayerProgressionEntity {
            return PlayerProgressionEntity(
                progression.id,
                progression.player.id ?: 0,
                progression.progressionNumber,
                progression.closeRangeShot,
                progression.midRangeShot,
                progression.longRangeShot,
                progression.freeThrowShot,
                progression.postMove,
                progression.ballHandling,
                progression.passing,
                progression.offBallMovement,
                progression.postDefense,
                progression.perimeterDefense,
                progression.onBallDefense,
                progression.offBallDefense,
                progression.stealing,
                progression.rebounding,
                progression.stamina
            )
        }
    }
}
