package com.appdev.jphil.basketballcoach.database.player

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.Player

@Entity
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val teamId: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val closeRangeShot: Int,
    val midRangeShot: Int,
    val longRangeShot: Int,
    val freeThrowShot: Int,
    val postMove: Int,
    val ballHandling: Int,
    val passing: Int,
    val offBallMovement: Int,
    val postDefense: Int,
    val perimeterDefense: Int,
    val onBallDefense: Int,
    val offBallDefense: Int,
    val stealing: Int,
    val rebounding: Int,
    val stamina: Int,
    val aggressiveness: Int
) {
    constructor(player: Player): this(
        player.id,
        player.teamId,
        player.firstName,
        player.lastName,
        player.position,
        player.closeRangeShot,
        player.midRangeShot,
        player.longRangeShot,
        player.freeThrowShot,
        player.postMove,
        player.ballHandling,
        player.passing,
        player.offBallMovement,
        player.postDefense,
        player.perimeterDefense,
        player.onBallDefense,
        player.offBallDefense,
        player.stealing,
        player.rebounding,
        player.stamina,
        player.aggressiveness
    )

    fun createPlayer(): Player {
        return Player(
            id!!,
            teamId,
            firstName,
            lastName,
            position,
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
            stamina,
            aggressiveness
        )
    }
}