package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.game.Game
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class RecruitInterest(
    val id: Int?,
    val recruitId: Int,
    val teamId: Int,
    val teamName: String,
    interest: Int,
    var ratingRange: Int,
    var ratingOffset: Int,
    var isOfferedScholarship: Boolean,
    var lastInteractionGame: Int,
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
            interest += getInterestIncrease(multiplier, teamMultiplier)
        } else {
            val multiplier = when {
                teamRating - otherRating > 10 -> ((teamRating - otherRating) / 10.0) / teamMultiplier
                otherRating - teamRating > 10 -> -1.0 / ((otherRating - teamRating) / 10.0) * teamMultiplier
                else -> 1.0
            }
            interest -= getInterestDecrease(multiplier, teamMultiplier)
        }
    }

    private fun getInterestIncrease(multiplier: Double, teamMultiplier: Double): Int {
        return max(5, min((Random.nextInt(MAX_INCREASE) * multiplier * teamMultiplier).toInt(), MAX_INCREASE))
    }

    private fun getInterestDecrease(multiplier: Double, teamMultiplier: Double): Int {
        return max(5, min((Random.nextInt(MAX_DECREASE) * multiplier / teamMultiplier).toInt(), MAX_DECREASE))
    }

    fun updateInterest(multiplier: Double, event: RecruitingEvent, gameNumber: Int): Boolean {
        if (isScholarshipRevoked) {
            return false
        }

        when (event) {
            RecruitingEvent.SCOUT -> doScoutingEvent(multiplier)
            RecruitingEvent.OFFER_SCHOLARSHIP -> offerScholarship(multiplier)
        }
        lastInteractionGame = gameNumber
        interest = max(0, min(interest, MAX_INTEREST))

        return considerScholarship()
    }

    fun considerScholarship(): Boolean {
        if (isOfferedScholarship && interest >= 50) {
            return Random.nextInt(100) < interest
        }
        return false
    }

    private fun doScoutingEvent(multiplier: Double) {
        interest += getInterestChange(multiplier)
        if (ratingRange > 0) {
            ratingRange = Random.nextInt(ratingRange)

            if (ratingOffset > ratingRange / 2) {
                ratingOffset = ratingRange / 2
            } else if (-ratingOffset > ratingRange / 2) {
                ratingOffset = -(ratingRange / 2)
            }
        }
    }

    private fun offerScholarship(multiplier: Double) {
        if (isOfferedScholarship) {
            return
        }
        interest += getInterestChange(multiplier)
        isOfferedScholarship = true
    }

    private fun getInterestChange(multiplier: Double) = ((Random.nextInt(MAX_INCREASE) - MAX_DECREASE) * multiplier).toInt()

    fun getMinRating(rating: Int) = max(0, min(100, rating - (ratingRange / 2) + ratingOffset))

    fun getMaxRating(rating: Int) = max(0, min(100, rating + (ratingRange / 2) + ratingOffset))

    companion object {
        const val MAX_INCREASE = 40
        const val MAX_DECREASE = 20
        const val MAX_INTEREST = 100
    }
}