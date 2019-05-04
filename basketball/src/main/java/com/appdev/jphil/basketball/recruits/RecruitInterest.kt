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
    interest: Int,
    var isScouted: Boolean,
    var isContacted: Boolean,
    var isOfferedScholarship: Boolean,
    var isOfficialVisitDone: Boolean
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

    private fun updateInterestAfterGame(teamScore: Int, teamRating: Int, otherScore: Int, otherRating: Int, teamMultiplier: Double) {
        if (teamScore > otherScore) {
            val multiplier = when {
                teamRating - otherRating > 10 -> 1.0 / ((teamRating - otherRating) / 10.0) * teamMultiplier
                otherRating - teamRating > 10 -> ((otherRating - teamRating) / 10.0) * teamMultiplier
                else -> 1.0
            }
            interest += (Random.nextInt(MAX_CHANGE) * multiplier * teamMultiplier).toInt()
        } else {
            val multiplier = when {
                teamRating - otherRating > 10 -> ((teamRating - otherRating) / 10.0) / teamMultiplier
                otherRating - teamRating > 10 -> -1.0 / ((otherRating - teamRating) / 10.0) * teamMultiplier
                else -> 1.0
            }
            interest -= (Random.nextInt(MAX_CHANGE) * multiplier / teamMultiplier).toInt()
        }
    }

    fun updateInterest(multiplier: Double, event: RecruitingEvent) {
        when (event) {
            RecruitingEvent.SCOUT -> doScoutingEvent(multiplier)
            RecruitingEvent.COACH_CONTACT -> doInitialContact(multiplier)
            RecruitingEvent.OFFER_SCHOLARSHIP -> offerScholarship(multiplier)
            RecruitingEvent.OFFICIAL_VISIT -> doOfficialVisit(multiplier)
        }

        interest = max(0, min(interest, MAX_INTEREST))
    }

    private fun doScoutingEvent(multiplier: Double) {
        if (isScouted) {
            return
        }
        interest += ((2 * Random.nextInt(MAX_CHANGE) - MAX_CHANGE) * multiplier).toInt()
        isScouted = true
    }

    private fun doInitialContact(multiplier: Double) {
        if (isContacted || !isScouted) {
            return
        }
        interest += ((2 * Random.nextInt(MAX_CHANGE) - MAX_CHANGE) * multiplier).toInt()
        isContacted = true
    }

    private fun offerScholarship(multiplier: Double) {
        if (isOfferedScholarship || !isScouted || !isContacted) {
            return
        }
        interest += ((2 * Random.nextInt(MAX_CHANGE) - MAX_CHANGE) * multiplier).toInt()
        isOfferedScholarship = true
    }

    private fun doOfficialVisit(multiplier: Double) {
        if (isOfficialVisitDone|| !isScouted || !isContacted || !isOfferedScholarship) {
            return
        }
        interest += ((2 * Random.nextInt(MAX_CHANGE) - MAX_CHANGE) * multiplier).toInt()
        isOfficialVisitDone = true
    }

    companion object {
        const val MAX_CHANGE = 20
        const val MAX_INTEREST = 100
    }
}