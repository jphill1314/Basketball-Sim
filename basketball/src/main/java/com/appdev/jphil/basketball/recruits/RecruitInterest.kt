package com.appdev.jphil.basketball.recruits

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class RecruitInterest(
    val id: Int?,
    val recruitId: Int,
    val teamId: Int,
    var interest: Int,
    var isScouted: Boolean,
    var isContacted: Boolean,
    var isOfferedScholarship: Boolean,
    var isOfficialVisitDone: Boolean
) {

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
        if (isContacted) {
            return
        }
        interest += ((2 * Random.nextInt(MAX_CHANGE) - MAX_CHANGE) * multiplier).toInt()
        isContacted = true
    }

    private fun offerScholarship(multiplier: Double) {
        if (isOfferedScholarship) {
            return
        }
        interest += ((2 * Random.nextInt(MAX_CHANGE) - MAX_CHANGE) * multiplier).toInt()
        isOfferedScholarship = true
    }

    private fun doOfficialVisit(multiplier: Double) {
        if (isOfficialVisitDone) {
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