package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerPositionWeights.ballWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.closeWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.ftWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.longWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.midWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.offBallWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.offMoveWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.onBallWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.passWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.perimDefWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.postDefWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.postOffWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.reboundWeight
import com.appdev.jphil.basketball.players.PlayerPositionWeights.stealWeight
import java.util.*

object PlayerFactory {

    private const val ratingVariability = 10

    fun generateBalancedPlayer(
        firstName: String,
        lastName: String,
        position: Int,
        year: Int,
        teamId: Int,
        rating: Int,
        index: Int
    ): Player {
        // TODO: instead of making worse players worse across the board, they should be good at a couple of things, and worse at others
        val r = Random()
        val newRating = rating + 10

        // Offensive
        val closeRangeShot =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * closeWeight[position - 1]).toInt()
        val midRangeShot =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * midWeight[position - 1]).toInt()
        val longRangeShot =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * longWeight[position - 1]).toInt()
        val freeThrowShot =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * ftWeight[position - 1]).toInt()
        val postMove =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * postOffWeight[position - 1]).toInt()
        val ballHandling =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * ballWeight[position - 1]).toInt()
        val passing =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * passWeight[position - 1]).toInt()
        val offBallMovement =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * offMoveWeight[position - 1]).toInt()

        // Defensive
        val postDefense =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * postDefWeight[position - 1]).toInt()
        val perimeterDefense =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * perimDefWeight[position - 1]).toInt()
        val onBallDefense =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * onBallWeight[position - 1]).toInt()
        val offBallDefense =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * offBallWeight[position - 1]).toInt()
        val stealing =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * stealWeight[position - 1]).toInt()
        val rebounding =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * reboundWeight[position - 1]).toInt()

        // Physical
        val stamina = r.nextInt(60) + 40
        val aggressiveness = r.nextInt(100)

        return Player(
            null,
            teamId,
            firstName,
            lastName,
            position,
            year,
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
            aggressiveness,
            index,
            index
        )
    }
}