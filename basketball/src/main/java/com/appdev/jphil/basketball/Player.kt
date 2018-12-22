package com.appdev.jphil.basketball

import java.util.*
import kotlin.math.max

class Player(
    val firstName: String,
    val lastName: String,
    val position: Int,
    val teamId: Int,
    generateAttributes: Boolean,
    rating: Int
) {
    var id: Int? = null

    var offensiveStatMod = 0
    var defensiveStatMod = 0
    var fatigue = 0.0
    var timePlayed = 0
    var inGame = false
    val fullName = "$firstName $lastName"

    var twoPointAttempts = 0
    var twoPointMakes = 0
    var threePointAttempts = 0
    var threePointMakes = 0
    var offensiveRebounds = 0
    var defensiveRebounds = 0
    var turnovers = 0
    var fouls = 0
    var freeThrowShots = 0
    var freeThrowMakes = 0

    init {
        if (generateAttributes) {
            generateAttributes(rating)
        }
    }

    constructor(
        id: Int,
        teamId: Int,
        firstName: String,
        lastName: String,
        position: Int,
        closeRangeShot: Int,
        midRangeShot: Int,
        longRangeShot: Int,
        freeThrowShot: Int,
        postMove: Int,
        ballHandling: Int,
        passing: Int,
        offBallMovement: Int,
        postDefense: Int,
        perimeterDefense: Int,
        onBallDefense: Int,
        offBallDefense: Int,
        stealing: Int,
        rebounding: Int,
        stamina: Int,
        aggressiveness: Int
    ) : this(firstName, lastName, position, teamId, false, 0) {
        this.id = id
        this.closeRangeShot = closeRangeShot
        this.midRangeShot = midRangeShot
        this.longRangeShot = longRangeShot
        this.freeThrowShot = freeThrowShot
        this.postMove = postMove
        this.ballHandling = ballHandling
        this.passing = passing
        this.offBallMovement = offBallMovement
        this.postDefense = postDefense
        this.perimeterDefense = perimeterDefense
        this.onBallDefense = onBallDefense
        this.offBallDefense = offBallDefense
        this.stealing = stealing
        this.rebounding = rebounding
        this.stamina = stamina
        this.aggressiveness = aggressiveness
    }

    // offense
    var closeRangeShot: Int = 0
        get() = if (inGame) max(((field + offensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var midRangeShot: Int = 0
        get() = if (inGame) max(((field + offensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var longRangeShot: Int = 0
        get() = if (inGame) max(((field + offensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var freeThrowShot: Int = 0
        get() = if (inGame) max(((field + offensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var postMove: Int = 0
        get() = if (inGame) max(((field + offensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var ballHandling: Int = 0
        get() = if (inGame) max(((field + offensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var passing: Int = 0
        get() = if (inGame) max(((field + offensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var offBallMovement: Int = 0
        get() = if (inGame) max(((field + offensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    // Defense
    var postDefense: Int = 0
        get() = if (inGame) max(((field + defensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var perimeterDefense: Int = 0
        get() = if (inGame) max(((field + defensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var onBallDefense: Int = 0
        get() = if (inGame) max(((field + defensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var offBallDefense: Int = 0
        get() = if (inGame) max(((field + defensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var stealing: Int = 0
        get() = if (inGame) max(((field + defensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var rebounding: Int = 0
        get() = if (inGame) max(((field + defensiveStatMod) * getFatigueFactor()).toInt(), 20) else field

    var stamina: Int = 0
    var aggressiveness: Int = 0

    private fun generateAttributes(rating: Int) {
        // TODO: instead of making worse players worse across the board, they should be good at a couple of things, and worse at others
        val r = Random()
        val newRating = rating + 10
        val ratingVariability = 10

        val closeWeight = doubleArrayOf(.6, .7, .8, 1.0, 1.0)
        val midWeight = doubleArrayOf(.8, .8, .8, .7, .7)
        val longWeight = doubleArrayOf(.8, 1.0, .8, .5, .5)
        val ftWeight = doubleArrayOf(.8, .8, .8, .6, .6)
        val postOffWeight = doubleArrayOf(.2, .2, .5, 1.3, 1.3)
        val ballWeight = doubleArrayOf(1.1, .8, .8, .6, .5)
        val passWeight = doubleArrayOf(1.1, .8, .8, .6, .5)
        val offMoveWeight = doubleArrayOf(.8, 1.0, .8, .7, .7)

        val postDefWeight = doubleArrayOf(.4, .5, .6, .9, 1.0)
        val perimDefWeight = doubleArrayOf(1.0, 1.0, .9, .5, .5)
        val onBallWeight = doubleArrayOf(1.0, .8, .8, .9, 1.0)
        val offBallWeight = doubleArrayOf(.8, .8, .8, .7, .6)
        val stealWeight = doubleArrayOf(.8, .8, .8, .5, .5)
        val reboundWeight = doubleArrayOf(.4, .5, .7, 1.2, 1.2)

        // Offensive
        closeRangeShot =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * closeWeight[position - 1]).toInt()
        midRangeShot =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * midWeight[position - 1]).toInt()
        longRangeShot =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * longWeight[position - 1]).toInt()
        freeThrowShot =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * ftWeight[position - 1]).toInt()
        postMove =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * postOffWeight[position - 1]).toInt()
        ballHandling =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * ballWeight[position - 1]).toInt()
        passing =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * passWeight[position - 1]).toInt()
        offBallMovement =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * offMoveWeight[position - 1]).toInt()

        // Defensive
        postDefense =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * postDefWeight[position - 1]).toInt()
        perimeterDefense =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * perimDefWeight[position - 1]).toInt()
        onBallDefense =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * onBallWeight[position - 1]).toInt()
        offBallDefense =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * offBallWeight[position - 1]).toInt()
        stealing =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * stealWeight[position - 1]).toInt()
        rebounding =
                ((newRating + 2 * r.nextInt(ratingVariability) - ratingVariability) * reboundWeight[position - 1]).toInt()

        // Physical
        stamina = r.nextInt(60) + 40
        aggressiveness = r.nextInt(100)
    }

    fun addTimePlayed(time: Int, isHalftime: Boolean, isTimeout: Boolean) {
        timePlayed += time
        if (time > 0) {
            fatigue += 2.8 - (stamina / 100.0)
            if (fatigue > 100) {
                fatigue = 100.0
            }
        } else {
            fatigue *= .9
        }

        if (isHalftime) {
            fatigue *= .5
        }
        if (isTimeout) {
            fatigue *= .8
        }
    }

    fun addFatigueFromPress() {
        fatigue += 1.5 - (stamina / 100.0)
    }

    private fun getFatigueFactor(): Double {
        return Math.max(1 - (Math.exp(fatigue / 10.0) / 100.0), .5)
    }

    fun getOverallRating(): Int {
        return getRatingAtPosition(position)
    }

    fun getRatingAtPosition(position: Int): Int {
        val closeWeight = doubleArrayOf(.6, .7, .8, 1.0, 1.0)
        val midWeight = doubleArrayOf(.8, .8, .8, .7, .7)
        val longWeight = doubleArrayOf(.8, 1.0, .8, .5, .5)
        val ftWeight = doubleArrayOf(.8, .8, .8, .6, .6)
        val postOffWeight = doubleArrayOf(.2, .2, .5, 1.3, 1.3)
        val ballWeight = doubleArrayOf(1.1, .8, .8, .6, .5)
        val passWeight = doubleArrayOf(1.1, .8, .8, .6, .5)
        val offMoveWeight = doubleArrayOf(.8, 1.0, .8, .7, .7)

        val postDefWeight = doubleArrayOf(.4, .5, .6, .9, 1.0)
        val perimDefWeight = doubleArrayOf(1.0, 1.0, .9, .5, .5)
        val onBallWeight = doubleArrayOf(1.0, .8, .8, .9, 1.0)
        val offBallWeight = doubleArrayOf(.8, .8, .8, .7, .6)
        val stealWeight = doubleArrayOf(.8, .8, .8, .5, .5)
        val reboundWeight = doubleArrayOf(.4, .5, .7, 1.2, 1.2)

        val overallRating = (closeRangeShot * closeWeight[position - 1] +
                midRangeShot * midWeight[position - 1] + longRangeShot * longWeight[position - 1] +
                freeThrowShot * ftWeight[position - 1] + postMove * postOffWeight[position - 1] +
                ballHandling * ballWeight[position - 1] + passing * passWeight[position - 1] +
                offBallMovement * offMoveWeight[position - 1] +
                postDefense * postDefWeight[position - 1] + perimeterDefense * perimDefWeight[position - 1] +
                onBallDefense * onBallWeight[position - 1] + offBallDefense * offBallWeight[position - 1] +
                stealing * stealWeight[position - 1] + rebounding * reboundWeight[position - 1]).toInt()

        val div = closeWeight[position - 1] + midWeight[position - 1] + longWeight[position - 1] +
                ftWeight[position - 1] + postOffWeight[position - 1] + ballWeight[position - 1] +
                passWeight[position - 1] + offMoveWeight[position - 1] +
                postDefWeight[position - 1] + perimDefWeight[position - 1] + onBallWeight[position - 1] +
                offBallWeight[position - 1] + stealWeight[position - 1] + reboundWeight[position - 1]

        return (overallRating / div).toInt() - getOutOfPositionMalus(position)
    }

    private fun getOutOfPositionMalus(currentPosition: Int): Int {
        return when (position) {
            1 -> when (currentPosition) {
                1 -> 0
                2 -> 5
                3 -> 10
                4 -> 20
                else -> 20
            }
            2 -> when (currentPosition) {
                1 -> 5
                2 -> 0
                3 -> 5
                4 -> 20
                else -> 20
            }
            3 -> when (currentPosition) {
                1 -> 10
                2 -> 5
                3 -> 0
                4 -> 20
                else -> 20
            }
            4 -> when (currentPosition) {
                1 -> 20
                2 -> 15
                3 -> 10
                4 -> 0
                else -> 5
            }
            else -> when (currentPosition) {
                1 -> 20
                2 -> 20
                3 -> 15
                4 -> 5
                else -> 0
            }
        }
    }

    fun startGame() {
        inGame = true
        twoPointAttempts = 0
        twoPointMakes = 0
        threePointAttempts = 0
        threePointMakes = 0
        offensiveRebounds = 0
        defensiveRebounds = 0
        turnovers = 0
        fouls = 0
        freeThrowShots = 0
        freeThrowMakes = 0

        offensiveStatMod = 0
        defensiveStatMod = 0
        fatigue = 0.0
        timePlayed = 0
    }

    fun isInFoulTrouble(half: Int, timeRemaining: Int): Boolean {
        return if (half == 1) {
            fouls > 1
        } else {
            if (timeRemaining > 12 * 60 && fouls > 2) {
                true
            } else {
                timeRemaining > 5 * 60 && fouls > 3
            }
        }
    }

    fun getStatsAsString(): String {
        return "$fullName\n" +
                "Minutes: ${timePlayed / 60}\n" +
                "2FG:$twoPointMakes/$twoPointAttempts - ${twoPointMakes / (twoPointAttempts * 1.0)}\n" +
                "3FG:$threePointMakes/$threePointAttempts - ${threePointMakes / (threePointAttempts * 1.0)}\n" +
                "Rebounds:$offensiveRebounds/$defensiveRebounds - ${offensiveRebounds + defensiveRebounds}\n" +
                "TO:$turnovers\nFouls:$fouls\n" +
                "FT:$freeThrowMakes/$freeThrowShots - ${freeThrowMakes / (freeThrowShots * 1.0)}"
    }
}