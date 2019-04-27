package com.appdev.jphil.basketball.players

class Player(
    val id: Int?,
    val teamId: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    var year: Int,
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
    var stamina: Int,
    var aggressiveness: Int,
    var rosterIndex: Int,
    var courtIndex: Int
) {

    var offensiveStatMod = 0
    var defensiveStatMod = 0
    var fatigue = 0.0
    var timePlayed = 0
    var inGame = false
    var subPosition = courtIndex
    val fullName = "$firstName $lastName"

    var progression: PlayerProgression? = null

    var twoPointAttempts = 0
    var twoPointMakes = 0
    var threePointAttempts = 0
    var threePointMakes = 0
    var assists = 0
    var offensiveRebounds = 0
    var defensiveRebounds = 0
    var turnovers = 0
    var steals = 0
    var fouls = 0
    var freeThrowShots = 0
    var freeThrowMakes = 0

    // offense
    var closeRangeShot: Int by PlayerAttributeDelegate(closeRangeShot)
    var midRangeShot: Int by PlayerAttributeDelegate(midRangeShot)
    var longRangeShot: Int by PlayerAttributeDelegate(longRangeShot)
    var freeThrowShot: Int by PlayerAttributeDelegate(freeThrowShot)
    var postMove: Int by PlayerAttributeDelegate(postMove)
    var ballHandling: Int by PlayerAttributeDelegate(ballHandling)
    var passing: Int by PlayerAttributeDelegate(passing)
    var offBallMovement: Int by PlayerAttributeDelegate(offBallMovement)

    // Defense
    var postDefense: Int by PlayerAttributeDelegate(postDefense)
    var perimeterDefense: Int by PlayerAttributeDelegate(perimeterDefense)
    var onBallDefense: Int by PlayerAttributeDelegate(onBallDefense)
    var offBallDefense: Int by PlayerAttributeDelegate(offBallDefense)
    var stealing: Int by PlayerAttributeDelegate(stealing)
    var rebounding: Int by PlayerAttributeDelegate(rebounding)

    fun addTimePlayed(time: Int, isHalftime: Boolean, isTimeout: Boolean) {
        timePlayed += time
        if (time > 0) {
            fatigue += 1.2 - (stamina / 100.0)
            if (fatigue > 100) {
                fatigue = 100.0
            }
        } else {
            fatigue -= (stamina / 200.0)
            if (fatigue < 0) {
                fatigue = 0.0
            }
        }

        if (isHalftime) {
            fatigue *= .85
        }
        if (isTimeout) {
            fatigue *= .9
        }
    }

    fun addFatigueFromPress() {
        fatigue += 1.5 - (stamina / 100.0)
    }

    fun getOverallRating(): Int {
        return getRatingAtPosition(position)
    }

    fun getRatingAtPositionNoFatigue(position: Int): Int {
        val currentlyInGame = inGame
        inGame = false
        val rating = getRatingAtPosition(position)
        inGame = currentlyInGame
        return rating
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
                4 -> 10
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
        assists =0
        offensiveRebounds = 0
        defensiveRebounds = 0
        turnovers = 0
        steals = 0
        fouls = 0
        freeThrowShots = 0
        freeThrowMakes = 0

        offensiveStatMod = 0
        defensiveStatMod = 0
        fatigue = 0.0
        timePlayed = 0

        courtIndex = rosterIndex
        subPosition = courtIndex
    }

    fun pauseGame() {
        inGame = false
    }

    fun resumeGame() {
        inGame = true
    }

    fun runPractice(practiceType: PracticeType) {
        progression?.runPractice(practiceType)
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