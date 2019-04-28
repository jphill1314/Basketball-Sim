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
import java.util.*

class PlayerProgression(
    val id: Int?,
    val player: Player,
    var closeRangeShot: Double = 0.0,
    var midRangeShot: Double = 0.0,
    var longRangeShot: Double = 0.0,
    var freeThrowShot: Double = 0.0,
    var postMove: Double = 0.0,
    var ballHandling: Double = 0.0,
    var passing: Double = 0.0,
    var offBallMovement: Double = 0.0,
    var postDefense: Double = 0.0,
    var perimeterDefense: Double = 0.0,
    var onBallDefense: Double = 0.0,
    var offBallDefense: Double = 0.0,
    var stealing: Double = 0.0,
    var rebounding: Double = 0.0,
    var stamina: Double = 0.0
) {
    private val r = Random()

    // TODO: allow attributes to decrease too
    fun runPractice(practiceType: PracticeType, coaches: List<Coach>) {
        setCoachAttributes(coaches)
        when (practiceType) {
            PracticeType.NO_FOCUS -> generalPractice()
            PracticeType.OFFENSE -> offensePractice()
            PracticeType.DEFENSE -> defensePractice()
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

    private fun generalPractice() {
        val practiceMax = MAX_INCREASE
        val position = player.position - 1
        closeRangeShot += r.nextInt(practiceMax) * closeWeight[position] + teachShooting
        midRangeShot += r.nextInt(practiceMax) * midWeight[position] + teachShooting
        longRangeShot += r.nextInt(practiceMax) * longWeight[position] + teachShooting
        freeThrowShot += r.nextInt(practiceMax) * ftWeight[position] + teachShooting
        postMove += r.nextInt(practiceMax) * postOffWeight[position] + teachPostMoves
        ballHandling += r.nextInt(practiceMax) * ballWeight[position] + teachBallControl
        passing += r.nextInt(practiceMax) * passWeight[position] + teachBallControl
        offBallMovement += r.nextInt(practiceMax) * offMoveWeight[position] + teachPositioning

        postDefense += r.nextInt(practiceMax) * postDefWeight[position] + teachPostDefense
        perimeterDefense += r.nextInt(practiceMax) * perimDefWeight[position] + teachPerimeterDefense
        onBallDefense += r.nextInt(practiceMax) * onBallWeight[position] + teachPositioning
        offBallDefense += r.nextInt(practiceMax) * offBallWeight[position] + teachPositioning
        stealing += r.nextInt(practiceMax) * stealWeight[position] + (teachPerimeterDefense + teachPostDefense) / 2
        rebounding += r.nextInt(practiceMax) * reboundWeight[position] + teachRebounding

        stamina += r.nextInt(MAX_INCREASE) + teachConditioning
    }

    private fun offensePractice() {
        val practiceMax = MAX_INCREASE * 2
        val position = player.position - 1
        closeRangeShot += r.nextInt(practiceMax) * closeWeight[position] + teachShooting
        midRangeShot += r.nextInt(practiceMax) * midWeight[position] + teachShooting
        longRangeShot += r.nextInt(practiceMax) * longWeight[position] + teachShooting
        freeThrowShot += r.nextInt(practiceMax) * ftWeight[position] + teachShooting
        postMove += r.nextInt(practiceMax) * postOffWeight[position] + teachPostMoves
        ballHandling += r.nextInt(practiceMax) * ballWeight[position] + teachBallControl
        passing += r.nextInt(practiceMax) * passWeight[position] + teachBallControl
        offBallMovement += r.nextInt(practiceMax) * offMoveWeight[position] + teachPositioning

        postDefense += r.nextInt(practiceMax / 4) * postDefWeight[position] + teachPostDefense
        perimeterDefense += r.nextInt(practiceMax / 4) * perimDefWeight[position] + teachPerimeterDefense
        onBallDefense += r.nextInt(practiceMax / 4) * onBallWeight[position] + teachPositioning
        offBallDefense += r.nextInt(practiceMax / 4) * offBallWeight[position] + teachPositioning
        stealing += r.nextInt(practiceMax / 4) * stealWeight[position] / 2 + (teachPerimeterDefense + teachPostDefense) / 2
        rebounding += r.nextInt(practiceMax / 4) * reboundWeight[position] + teachRebounding

        stamina += r.nextInt(MAX_INCREASE) + teachConditioning
    }

    private fun defensePractice() {
        val practiceMax = MAX_INCREASE * 2
        val position = player.position - 1
        closeRangeShot += r.nextInt(practiceMax / 4) * closeWeight[position] + teachShooting
        midRangeShot += r.nextInt(practiceMax / 4) * midWeight[position] + teachShooting
        longRangeShot += r.nextInt(practiceMax / 4) * longWeight[position] + teachShooting
        freeThrowShot += r.nextInt(practiceMax / 4) * ftWeight[position] + teachShooting
        postMove += r.nextInt(practiceMax / 4) * postOffWeight[position] + teachPostMoves
        ballHandling += r.nextInt(practiceMax / 4) * ballWeight[position] + teachBallControl
        passing += r.nextInt(practiceMax / 4) * passWeight[position] + teachBallControl
        offBallMovement += r.nextInt(practiceMax / 4) * offMoveWeight[position] + teachPositioning

        postDefense += r.nextInt(practiceMax) * postDefWeight[position] + teachPostDefense
        perimeterDefense += r.nextInt(practiceMax) * perimDefWeight[position] + teachPerimeterDefense
        onBallDefense += r.nextInt(practiceMax) * onBallWeight[position] + teachPositioning
        offBallDefense += r.nextInt(practiceMax) * offBallWeight[position] + teachPositioning
        stealing += r.nextInt(practiceMax) * stealWeight[position] / 2 + (teachPerimeterDefense + teachPostDefense) / 2
        rebounding += r.nextInt(practiceMax) * reboundWeight[position] + teachRebounding

        stamina += r.nextInt(MAX_INCREASE) + teachConditioning
    }

    private fun conditioning() {
        stamina += r.nextInt(MAX_INCREASE * 4) + teachConditioning
    }

    private fun incrementAttributes() {
        if (closeRangeShot > POINTS_TO_INCREMENT) {
            player.closeRangeShot++
            closeRangeShot = 0.0
        } else if (closeRangeShot < -POINTS_TO_INCREMENT) {
            player.closeRangeShot--
            closeRangeShot = 0.0
        }

        if (midRangeShot > POINTS_TO_INCREMENT) {
            player.midRangeShot++
            midRangeShot = 0.0
        } else if (midRangeShot < -POINTS_TO_INCREMENT) {
            player.midRangeShot--
            midRangeShot = 0.0
        }

        if (longRangeShot > POINTS_TO_INCREMENT) {
            player.longRangeShot++
            longRangeShot = 0.0
        } else if (longRangeShot < -POINTS_TO_INCREMENT) {
            player.longRangeShot--
            longRangeShot = 0.0
        }

        if (freeThrowShot> POINTS_TO_INCREMENT) {
            player.freeThrowShot++
            freeThrowShot = 0.0
        } else if (freeThrowShot < -POINTS_TO_INCREMENT) {
            player.freeThrowShot--
            freeThrowShot = 0.0
        }

        if (postMove > POINTS_TO_INCREMENT) {
            player.postMove++
            postMove = 0.0
        } else if (postMove < -POINTS_TO_INCREMENT) {
            player.postMove--
            postMove = 0.0
        }

        if (ballHandling > POINTS_TO_INCREMENT) {
            player.ballHandling++
            ballHandling = 0.0
        } else if (ballHandling < -POINTS_TO_INCREMENT) {
            player.ballHandling--
            ballHandling = 0.0
        }

        if (passing > POINTS_TO_INCREMENT) {
            player.passing++
            passing = 0.0
        } else if (passing < -POINTS_TO_INCREMENT) {
            player.passing--
            passing = 0.0
        }

        if (offBallMovement > POINTS_TO_INCREMENT) {
            player.offBallMovement++
            offBallMovement = 0.0
        } else if (offBallMovement < -POINTS_TO_INCREMENT) {
            player.offBallMovement--
            offBallMovement = 0.0
        }

        if (postDefense > POINTS_TO_INCREMENT) {
            player.postDefense++
            postDefense = 0.0
        } else if (postDefense < -POINTS_TO_INCREMENT) {
            player.postDefense--
            postDefense = 0.0
        }

        if (perimeterDefense > POINTS_TO_INCREMENT) {
            player.perimeterDefense++
            perimeterDefense = 0.0
        } else if (perimeterDefense < -POINTS_TO_INCREMENT) {
            player.perimeterDefense--
            perimeterDefense = 0.0
        }

        if (onBallDefense > POINTS_TO_INCREMENT) {
            player.onBallDefense++
            onBallDefense = 0.0
        } else if (onBallDefense < -POINTS_TO_INCREMENT) {
            player.onBallDefense--
            onBallDefense = 0.0
        }

        if (offBallDefense > POINTS_TO_INCREMENT) {
            player.offBallDefense++
            offBallDefense = 0.0
        } else if (offBallDefense < -POINTS_TO_INCREMENT) {
            player.offBallDefense--
            offBallDefense = 0.0
        }

        if (stealing > POINTS_TO_INCREMENT) {
            player.stealing++
            stealing = 0.0
        } else if (stealing < -POINTS_TO_INCREMENT) {
            player.stealing--
            stealing = 0.0
        }

        if (rebounding > POINTS_TO_INCREMENT) {
            player.rebounding++
            rebounding = 0.0
        } else if (rebounding < -POINTS_TO_INCREMENT) {
            player.rebounding--
            rebounding = 0.0
        }

        if (stamina > POINTS_TO_INCREMENT) {
            player.stamina++
            stamina = 0.0
        } else if (stamina < -POINTS_TO_INCREMENT) {
            player.stamina--
            stamina = 0.0
        }
    }

    companion object {
        const val POINTS_TO_INCREMENT = 100
        private const val MAX_INCREASE = 10
    }
}