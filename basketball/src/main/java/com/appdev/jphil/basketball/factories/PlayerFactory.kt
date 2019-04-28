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
import com.appdev.jphil.basketball.players.PlayerType
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
        val i = position - 1
        val type = getPlayerType(i)
        val pType = type.type

        // Offensive
        val closeRangeShot = generateAttribute(newRating, closeWeight[i] + PlayerType.closeWeight[pType])
        val midRangeShot = generateAttribute(newRating, midWeight[i] + PlayerType.midWeight[pType])
        val longRangeShot = generateAttribute(newRating, longWeight[i] + PlayerType.longWeight[pType])
        val freeThrowShot = generateAttribute(newRating, ftWeight[i] + PlayerType.ftWeight[pType])
        val postMove = generateAttribute(newRating, postOffWeight[i] + PlayerType.postOffWeight[pType])
        val ballHandling = generateAttribute(newRating, ballWeight[i] + PlayerType.ballWeight[pType])
        val passing = generateAttribute(newRating, passWeight[i] + PlayerType.passWeight[pType])
        val offBallMovement = generateAttribute(newRating, offMoveWeight[i] + PlayerType.offMoveWeight[pType])

        // Defensive
        val postDefense = generateAttribute(newRating, postDefWeight[i] + PlayerType.postDefWeight[pType])
        val perimeterDefense = generateAttribute(newRating, perimDefWeight[i] + PlayerType.perimDefWeight[pType])
        val onBallDefense = generateAttribute(newRating, onBallWeight[i] + PlayerType.onBallWeight[pType])
        val offBallDefense = generateAttribute(newRating, offBallWeight[i] + PlayerType.offBallWeight[pType])
        val stealing = generateAttribute(newRating, stealWeight[i] + PlayerType.stealWeight[pType])
        val rebounding = generateAttribute(newRating, reboundWeight[i] + PlayerType.reboundWeight[pType])

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
            type,
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

    private fun generateAttribute(rating: Int, weight: Double): Int {
        val r = Random()
        return ((rating + 2 * r.nextInt(ratingVariability) - ratingVariability) * weight).toInt()
    }

    private fun getPlayerType(position: Int): PlayerType {
        val r = Random()
        val num = r.nextInt(100)
        return when (num) {
            in PlayerType.positionWeights[position][0]..PlayerType.positionWeights[position][1] -> PlayerType.SHOOTER
            in PlayerType.positionWeights[position][1]..PlayerType.positionWeights[position][2] -> PlayerType.DISTRIBUTOR
            in PlayerType.positionWeights[position][2]..PlayerType.positionWeights[position][3] -> PlayerType.REBOUNDER
            in PlayerType.positionWeights[position][3]..100 -> PlayerType.DEFENDER
            else -> PlayerType.BALANCED
        }
    }
}