package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.Player
import java.util.*

object PlayerFactory {

    private const val ratingVariability = 10

    private val closeWeight = doubleArrayOf(.6, .7, .8, 1.0, 1.0)
    private val midWeight = doubleArrayOf(.8, .8, .8, .7, .7)
    private val longWeight = doubleArrayOf(.8, 1.0, .8, .5, .5)
    private val ftWeight = doubleArrayOf(.8, .8, .8, .6, .6)
    private val postOffWeight = doubleArrayOf(.2, .2, .5, 1.3, 1.3)
    private val ballWeight = doubleArrayOf(1.1, .8, .8, .6, .5)
    private val passWeight = doubleArrayOf(1.1, .8, .8, .6, .5)
    private val offMoveWeight = doubleArrayOf(.8, 1.0, .8, .7, .7)

    private val postDefWeight = doubleArrayOf(.4, .5, .6, .9, 1.0)
    private val perimDefWeight = doubleArrayOf(1.0, 1.0, .9, .5, .5)
    private val onBallWeight = doubleArrayOf(1.0, .8, .8, .9, 1.0)
    private val offBallWeight = doubleArrayOf(.8, .8, .8, .7, .6)
    private val stealWeight = doubleArrayOf(.8, .8, .8, .5, .5)
    private val reboundWeight = doubleArrayOf(.4, .5, .7, 1.2, 1.2)

    fun generateBalancedPlayer(
        firstName: String,
        lastName: String,
        position: Int,
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