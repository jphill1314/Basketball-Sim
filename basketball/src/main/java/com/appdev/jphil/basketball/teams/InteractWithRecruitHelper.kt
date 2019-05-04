package com.appdev.jphil.basketball.teams

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitInterest
import com.appdev.jphil.basketball.recruits.RecruitingEvent
import kotlin.random.Random

object InteractWithRecruitHelper {

    fun notScouted(recruit: Recruit, interest: RecruitInterest, team: Team) {
        if (doInteraction(interest, RecruitingEvent.SCOUT)) {
            recruit.updateInterest(team, RecruitingEvent.SCOUT, team.gamesPlayed)
        }
    }

    fun notContacted(recruit: Recruit, interest: RecruitInterest, team: Team) {
        if (doInteraction(interest, RecruitingEvent.COACH_CONTACT)) {
            recruit.updateInterest(team, RecruitingEvent.COACH_CONTACT, team.gamesPlayed)
        }
        notScouted(recruit, interest, team)
    }

    fun noScholarshipOffer(recruit: Recruit, interest: RecruitInterest, team: Team) {
        if (doInteraction(interest, RecruitingEvent.OFFER_SCHOLARSHIP)) {
            recruit.updateInterest(team, RecruitingEvent.OFFER_SCHOLARSHIP, team.gamesPlayed)
        }
        notContacted(recruit, interest, team)
    }

    fun noOfficialVisit(recruit: Recruit, interest: RecruitInterest, team: Team) {
        if (doInteraction(interest, RecruitingEvent.OFFICIAL_VISIT)) {
            recruit.updateInterest(team, RecruitingEvent.OFFICIAL_VISIT, team.gamesPlayed)
        }
        notContacted(recruit, interest, team)
    }

    fun notCommitted(recruit: Recruit, interest: RecruitInterest, team: Team) {
        recruit.considerScholarship(team)
        notContacted(recruit, interest, team)
    }

    private fun doInteraction(interest: RecruitInterest, event: RecruitingEvent): Boolean {
        return when (event) {
            RecruitingEvent.SCOUT -> interest.interest >= 0 && Random.nextDouble() > 0.5
            RecruitingEvent.COACH_CONTACT -> interest.interest >= 20 && Random.nextDouble() > 0.3
            RecruitingEvent.OFFER_SCHOLARSHIP -> interest.interest >= 50
            RecruitingEvent.OFFICIAL_VISIT -> interest.interest >= 50
        }
    }
}