package com.appdev.jphil.basketball.players

import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.coaches.Coach

class Player(
    val id: Int?,
    val teamId: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    var year: Int,
    val type: PlayerType,
    val isOnScholarship: Boolean,
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
    val potential: Int,
    var rosterIndex: Int,
    var courtIndex: Int,
    val pronouns: Pronouns
) {

    var offensiveStatMod = 0
    var defensiveStatMod = 0
    var fatigue = 0.0
    var timePlayed = 0
    var inGame = false
    var subPosition = courtIndex
    val fullName = "$firstName $lastName"
    val firstInitialLastName = "${firstName[0]}. $lastName"

    val progression = mutableListOf<PlayerProgression>()

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

    fun addTimePlayed(time: Int, isHurrying: Boolean, isHalftime: Boolean, isTimeout: Boolean) {
        timePlayed += time
        val fatigueIncrease = if (isHurrying) 5.0 else 1.2
        if (time > 0) {
            fatigue += fatigueIncrease - (stamina / 100.0)
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
        return getRatingAtPositionNoFatigue(position)
    }

    fun getRatingAtPositionNoFatigue(position: Int): Int {
        val currentlyInGame = inGame
        inGame = false
        val rating = getRatingAtPosition(position)
        inGame = currentlyInGame
        return rating
    }

    fun getRatingAtPosition(position: Int): Int {
        val ratings = mutableListOf(
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
            rebounding
        )

        var ratingTotal = 0.0
        ratings.sortedByDescending { it }.subList(0, ratings.size - 4).forEach { rating ->
            ratingTotal += rating
        }

        return (ratingTotal / (ratings.size - 4)).toInt() - getOutOfPositionMalus(position)
    }

    private fun getOutOfPositionMalus(currentPosition: Int): Int {
        return when (position) {
            1 -> when (currentPosition) {
                1 -> 0
                2 -> 10
                3 -> 15
                4 -> 25
                else -> 25
            }
            2 -> when (currentPosition) {
                1 -> 10
                2 -> 0
                3 -> 10
                4 -> 25
                else -> 25
            }
            3 -> when (currentPosition) {
                1 -> 15
                2 -> 10
                3 -> 0
                4 -> 15
                else -> 25
            }
            4 -> when (currentPosition) {
                1 -> 25
                2 -> 20
                3 -> 15
                4 -> 0
                else -> 10
            }
            else -> when (currentPosition) {
                1 -> 25
                2 -> 25
                3 -> 20
                4 -> 10
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
        assists = 0
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

    fun runPractice(practiceType: PracticeType, coaches: List<Coach>) {
        if (progression.isEmpty()) {
            progression.add(PlayerProgression(null, this, 0))
        } else {
            progression.add(
                progression.last().copy().also { it.runPractice(practiceType, coaches, timePlayed / 60) }
            )
        }
    }

    fun isInFoulTrouble(half: Int, timeRemaining: Int): Boolean {
        if (!isEligible()) {
            return false
        }

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

    fun isEligible(): Boolean = fouls < 5
}
