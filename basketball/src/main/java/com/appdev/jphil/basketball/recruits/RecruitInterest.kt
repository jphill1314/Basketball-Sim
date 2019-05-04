package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.game.Game
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class RecruitInterest(
    val id: Int?,
    val recruitId: Int,
    val teamId: Int,
    val teamName: String,
    interest: Int,
    var isScouted: Boolean,
    var isContacted: Boolean,
    var isOfferedScholarship: Boolean,
    var isOfficialVisitDone: Boolean,
    var lastInteractionGame: Int,
    var timesScouted: Int,
    var timesContacted: Int,
    var isScholarshipRevoked: Boolean
) {

    var interest = interest
        set(value) { field = max(0, min(100, value)) }

    fun onTeamGameCompleted(game: Game, multiplier: Double) {
        if (game.homeTeam.teamId == teamId) {
            with (game) {
                updateInterestAfterGame(homeScore, homeTeam.teamRating, awayScore, awayTeam.teamRating, multiplier)
            }
        } else {
            with (game) {
                updateInterestAfterGame(awayScore, awayTeam.teamRating, homeScore, homeTeam.teamRating, multiplier)
            }
        }
    }

    fun revokeScholarshipOffer() {
        isScholarshipRevoked = true
        interest = 0
    }

    private fun updateInterestAfterGame(teamScore: Int, teamRating: Int, otherScore: Int, otherRating: Int, teamMultiplier: Double) {
        if (teamScore > otherScore) {
            val multiplier = when {
                teamRating - otherRating > 10 -> 1.0 / ((teamRating - otherRating) / 10.0) * teamMultiplier
                otherRating - teamRating > 10 -> ((otherRating - teamRating) / 10.0) * teamMultiplier
                else -> 1.0
            }
            interest += (Random.nextInt(MAX_INCREASE) * multiplier * teamMultiplier).toInt()
        } else {
            val multiplier = when {
                teamRating - otherRating > 10 -> ((teamRating - otherRating) / 10.0) / teamMultiplier
                otherRating - teamRating > 10 -> -1.0 / ((otherRating - teamRating) / 10.0) * teamMultiplier
                else -> 1.0
            }
            interest -= (Random.nextInt(MAX_DECREASE) * multiplier / teamMultiplier).toInt()
        }
    }

    fun updateInterest(multiplier: Double, event: RecruitingEvent, gameNumber: Int): Boolean {
        if (isScholarshipRevoked) {
            return false
        }

        when (event) {
            RecruitingEvent.SCOUT -> doScoutingEvent(multiplier)
            RecruitingEvent.COACH_CONTACT -> doInitialContact(multiplier)
            RecruitingEvent.OFFER_SCHOLARSHIP -> offerScholarship(multiplier)
            RecruitingEvent.OFFICIAL_VISIT -> doOfficialVisit(multiplier)
        }
        lastInteractionGame = gameNumber
        interest = max(0, min(interest, MAX_INTEREST))

        return considerScholarship()
    }

    fun considerScholarship(): Boolean {
        if (isOfficialVisitDone && interest > 50) {
            return Random.nextInt(100) < interest
        }
        return false
    }

    private fun doScoutingEvent(multiplier: Double) {
        interest += (getInterestChange(multiplier) * getRecurrentPenalty(true)).toInt()
        timesScouted++
        isScouted = true
    }

    private fun doInitialContact(multiplier: Double) {
        interest += (getInterestChange(multiplier) * getRecurrentPenalty(false)).toInt()
        timesContacted++
        isContacted = true
    }

    private fun offerScholarship(multiplier: Double) {
        if (isOfferedScholarship || !isScouted || !isContacted) {
            return
        }
        interest += getInterestChange(multiplier)
        isOfferedScholarship = true
    }

    private fun doOfficialVisit(multiplier: Double) {
        if (isOfficialVisitDone|| !isScouted || !isContacted || !isOfferedScholarship) {
            return
        }
        interest += getInterestChange(multiplier)
        isOfficialVisitDone = true
    }

    private fun getInterestChange(multiplier: Double) = ((Random.nextInt(MAX_INCREASE) - MAX_DECREASE) * multiplier).toInt()

    private fun getRecurrentPenalty(isScouting: Boolean): Double {
        return if (isScouting) {
            when (timesScouted) {
                in 0..1 -> 1.0
                in 2..3 -> .5
                else -> 1.0 / timesScouted
            }
        } else {
            when (timesContacted) {
                in 0..3 -> 1.0
                in 4..6 -> .5
                else -> 1.0 / timesScouted
            }
        }
    }

    companion object {
        const val MAX_INCREASE = 40
        const val MAX_DECREASE = 10
        const val MAX_INTEREST = 100
    }
}