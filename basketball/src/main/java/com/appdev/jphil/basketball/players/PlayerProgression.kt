package com.appdev.jphil.basketball.players

import com.appdev.jphil.basketball.coaches.Coach
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
import kotlin.random.Random

class PlayerProgression(
    val id: Int?,
    val player: Player,
    val progressionNumber: Int,
    var closeRangeShot: Double = (player.closeRangeShot * POINTS_TO_INCREMENT).toDouble(),
    var midRangeShot: Double = (player.midRangeShot * POINTS_TO_INCREMENT).toDouble(),
    var longRangeShot: Double = (player.longRangeShot * POINTS_TO_INCREMENT).toDouble(),
    var freeThrowShot: Double = (player.freeThrowShot * POINTS_TO_INCREMENT).toDouble(),
    var postMove: Double = (player.postMove * POINTS_TO_INCREMENT).toDouble(),
    var ballHandling: Double = (player.ballHandling * POINTS_TO_INCREMENT).toDouble(),
    var passing: Double = (player.passing * POINTS_TO_INCREMENT).toDouble(),
    var offBallMovement: Double = (player.offBallMovement * POINTS_TO_INCREMENT).toDouble(),
    var postDefense: Double = (player.postDefense * POINTS_TO_INCREMENT).toDouble(),
    var perimeterDefense: Double = (player.perimeterDefense * POINTS_TO_INCREMENT).toDouble(),
    var onBallDefense: Double = (player.onBallDefense * POINTS_TO_INCREMENT).toDouble(),
    var offBallDefense: Double = (player.offBallDefense * POINTS_TO_INCREMENT).toDouble(),
    var stealing: Double = (player.stealing * POINTS_TO_INCREMENT).toDouble(),
    var rebounding: Double = (player.rebounding * POINTS_TO_INCREMENT).toDouble(),
    var stamina: Double = (player.stamina * POINTS_TO_INCREMENT).toDouble()
) {

    // TODO: allow attributes to decrease too?
    fun runPractice(practiceType: PracticeType, coaches: List<Coach>, minutes: Int) {
        setCoachAttributes(coaches)
        val practiceMax = getPracticeMax(minutes)
        when (practiceType) {
            PracticeType.NO_FOCUS -> runPractice(practiceMax, practiceMax)
            PracticeType.OFFENSE -> runPractice(practiceMax * 2, practiceMax / 2)
            PracticeType.DEFENSE -> runPractice(practiceMax / 2, practiceMax * 2)
            PracticeType.CONDITIONING -> conditioning()
        }
        incrementAttributes()
    }

    private var teachShooting: Double = 1.0
    private var teachPostMoves: Double = 1.0
    private var teachBallControl: Double = 1.0
    private var teachPostDefense: Double = 1.0
    private var teachPerimeterDefense: Double = 1.0
    private var teachPositioning: Double = 1.0
    private var teachRebounding: Double = 1.0
    private var teachConditioning: Double = 1.0

    private fun setCoachAttributes(coaches: List<Coach>) {
        val denominator = coaches.size * 10.0
        coaches.forEach {
            teachShooting += it.teachShooting
            teachPostMoves += it.teachPostMoves
            teachBallControl += it.teachBallControl
            teachPostDefense += it.teachPostDefense
            teachPerimeterDefense += it.teachPerimeterDefense
            teachPositioning += it.teachPositioning
            teachRebounding += it.teachRebounding
            teachConditioning += it.teachConditioning
        }

        teachShooting /= denominator
        teachPostMoves /= denominator
        teachBallControl /= denominator
        teachPostDefense /= denominator
        teachPerimeterDefense /= denominator
        teachPositioning /= denominator
        teachRebounding /= denominator
        teachConditioning /= denominator
    }

    private fun runPractice(offenseMax: Int, defenseMax: Int) {
        val position = player.position - 1
        closeRangeShot += Random.nextInt(offenseMax) * closeWeight[position] + teachShooting
        midRangeShot += Random.nextInt(offenseMax) * midWeight[position] + teachShooting
        longRangeShot += Random.nextInt(offenseMax) * longWeight[position] + teachShooting
        freeThrowShot += Random.nextInt(offenseMax) * ftWeight[position] + teachShooting
        postMove += Random.nextInt(offenseMax) * postOffWeight[position] + teachPostMoves
        ballHandling += Random.nextInt(offenseMax) * ballWeight[position] + teachBallControl
        passing += Random.nextInt(offenseMax) * passWeight[position] + teachBallControl
        offBallMovement += Random.nextInt(offenseMax) * offMoveWeight[position] + teachPositioning

        postDefense += Random.nextInt(defenseMax) * postDefWeight[position] + teachPostDefense
        perimeterDefense += Random.nextInt(defenseMax) * perimDefWeight[position] + teachPerimeterDefense
        onBallDefense += Random.nextInt(defenseMax) * onBallWeight[position] + teachPositioning
        offBallDefense += Random.nextInt(defenseMax) * offBallWeight[position] + teachPositioning
        stealing += Random.nextInt(defenseMax) * stealWeight[position] / 2 + (teachPerimeterDefense + teachPostDefense) / 2
        rebounding += Random.nextInt(defenseMax) * reboundWeight[position] + teachRebounding

        stamina += Random.nextInt(MAX_INCREASE) + teachConditioning
    }

    private fun conditioning() {
        stamina += Random.nextInt(MAX_INCREASE * 4) + teachConditioning
    }

    private fun getPracticeMax(minutes: Int): Int {
        val initMax = MAX_INCREASE * player.potential / 100.0
        return when {
            minutes in 1..4 -> (initMax * 1.25).toInt()
            minutes in 5..10 -> (initMax * 1.5).toInt()
            minutes > 10 -> (initMax * 2).toInt()
            else -> initMax.toInt()
        }
    }

    private fun incrementAttributes() {
        if (closeRangeShot.toInt() > player.closeRangeShot * POINTS_TO_INCREMENT) {
            player.closeRangeShot++
        } else if (closeRangeShot.toInt() < player.closeRangeShot * POINTS_TO_INCREMENT) {
            player.closeRangeShot--
        }

        if (midRangeShot > player.midRangeShot * POINTS_TO_INCREMENT) {
            player.midRangeShot++
        } else if (midRangeShot < player.midRangeShot * POINTS_TO_INCREMENT) {
            player.midRangeShot--
        }

        if (longRangeShot > player.longRangeShot * POINTS_TO_INCREMENT) {
            player.longRangeShot++
        } else if (longRangeShot < player.longRangeShot * POINTS_TO_INCREMENT) {
            player.longRangeShot--
        }

        if (freeThrowShot > player.freeThrowShot * POINTS_TO_INCREMENT) {
            player.freeThrowShot++
        } else if (freeThrowShot < player.freeThrowShot * POINTS_TO_INCREMENT) {
            player.freeThrowShot--
        }

        if (postMove > player.postMove * POINTS_TO_INCREMENT) {
            player.postMove++
        } else if (postMove < player.postMove * POINTS_TO_INCREMENT) {
            player.postMove--
        }

        if (ballHandling > player.ballHandling * POINTS_TO_INCREMENT) {
            player.ballHandling++
        } else if (ballHandling < player.ballHandling * POINTS_TO_INCREMENT) {
            player.ballHandling--
        }

        if (passing > player.passing * POINTS_TO_INCREMENT) {
            player.passing++
        } else if (passing < player.passing * POINTS_TO_INCREMENT) {
            player.passing--
        }

        if (offBallMovement > player.offBallMovement * POINTS_TO_INCREMENT) {
            player.offBallMovement++
        } else if (offBallMovement < player.offBallMovement * POINTS_TO_INCREMENT) {
            player.offBallMovement--
        }

        if (postDefense > player.postDefense * POINTS_TO_INCREMENT) {
            player.postDefense++
        } else if (postDefense < player.postDefense * POINTS_TO_INCREMENT) {
            player.postDefense--
        }

        if (perimeterDefense > player.perimeterDefense * POINTS_TO_INCREMENT) {
            player.perimeterDefense++
        } else if (perimeterDefense < player.perimeterDefense * POINTS_TO_INCREMENT) {
            player.perimeterDefense--
        }

        if (onBallDefense > player.onBallDefense * POINTS_TO_INCREMENT) {
            player.onBallDefense++
        } else if (onBallDefense < player.onBallDefense * POINTS_TO_INCREMENT) {
            player.onBallDefense--
        }

        if (offBallDefense > player.offBallDefense * POINTS_TO_INCREMENT) {
            player.offBallDefense++
        } else if (offBallDefense < player.offBallDefense * POINTS_TO_INCREMENT) {
            player.offBallDefense--
        }

        if (stealing > player.stealing * POINTS_TO_INCREMENT) {
            player.stealing++
        } else if (stealing < player.stealing * POINTS_TO_INCREMENT) {
            player.stealing--
        }

        if (rebounding > player.rebounding * POINTS_TO_INCREMENT) {
            player.rebounding++
        } else if (rebounding < player.rebounding * POINTS_TO_INCREMENT) {
            player.rebounding--
        }

        if (stamina > player.stamina * POINTS_TO_INCREMENT) {
            player.stamina++
        } else if (stamina < player.stamina * POINTS_TO_INCREMENT) {
            player.stamina--
        }
    }

    fun copy(): PlayerProgression {
        return PlayerProgression(
            null,
            player,
            progressionNumber + 1,
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

    fun getProgressAsList(): List<Double> {
        val progress = mutableListOf<Double>()
        progress.add(closeRangeShot)
        progress.add(midRangeShot)
        progress.add(longRangeShot)
        progress.add(freeThrowShot)
        progress.add(postMove)
        progress.add(ballHandling)
        progress.add(passing)
        progress.add(offBallMovement)
        progress.add(postDefense)
        progress.add(perimeterDefense)
        progress.add(onBallDefense)
        progress.add(offBallDefense)
        progress.add(stealing)
        progress.add(rebounding)
        progress.add(stamina)
        return progress
    }

    companion object {
        const val POINTS_TO_INCREMENT = 100
        private const val MAX_INCREASE = 10
    }
}
