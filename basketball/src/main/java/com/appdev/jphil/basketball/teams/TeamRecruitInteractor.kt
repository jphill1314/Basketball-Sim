package com.appdev.jphil.basketball.teams

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitInterest
import com.appdev.jphil.basketball.recruits.RecruitingEvent

object TeamRecruitInteractor {

    private const val GAMES_BETWEEN_INTERACTIONS = 0

    fun interactWithRecruits(team: Team, recruits: List<Recruit>, gameNumber: Int) {
        val teamRating = team.teamRating
        for (position in 1..5) {
            if (hasNeedAtPosition(position, team, recruits)) {
                recruits.filter { it.position == position }
                    .filter { !it.isCommitted }
                    .filter { teamRating - 10 <= it.rating && teamRating + 10 >= it.rating }
                    .forEach { recruit ->
                        if (hasNeedAtPosition(position, team, recruits)) {
                            interactWithRecruit(recruit, team, gameNumber)
                        }
                    }
            }
        }
    }

    fun hasNeedAtPosition(position: Int, team: Team, recruits: List<Recruit>): Boolean {
        var players = team.roster.filter { it.position == position && it.year != 3 }.size
        players += recruits.filter { it.isCommitted && it.teamCommittedTo == team.teamId && it.position == position }.size
        return 3 - players > 0
    }

    private fun interactWithRecruit(recruit: Recruit, team: Team, gameNumber: Int) {
        val interestInTeam = recruit.interestInTeams.filter { it.teamId == team.teamId }
        val interest = if (interestInTeam.isEmpty()) {
            recruit.generateInitialInterest(team)
            recruit.interestInTeams.filter { it.teamId == team.teamId }.first()
        } else {
            interestInTeam.first()
        }
        if (interest.lastInteractionGame + GAMES_BETWEEN_INTERACTIONS <= gameNumber) {
            when {
                !interest.isScouted -> {
                    if (doInteraction(interest, RecruitingEvent.SCOUT)) {
                        recruit.updateInterest(
                            team,
                            RecruitingEvent.SCOUT,
                            gameNumber
                        )
                    }
                }
                !interest.isContacted -> {
                    if (doInteraction(interest, RecruitingEvent.COACH_CONTACT)) {
                        recruit.updateInterest(
                            team,
                            RecruitingEvent.COACH_CONTACT,
                            gameNumber
                        )
                    }
                }
                !interest.isOfferedScholarship -> {
                    if (doInteraction(interest, RecruitingEvent.OFFER_SCHOLARSHIP)) {
                        recruit.updateInterest(
                            team,
                            RecruitingEvent.OFFER_SCHOLARSHIP,
                            gameNumber
                        )
                    }
                }
                !interest.isOfficialVisitDone -> {
                    if (doInteraction(interest, RecruitingEvent.OFFICIAL_VISIT)) {
                        recruit.updateInterest(
                            team,
                            RecruitingEvent.OFFICIAL_VISIT,
                            gameNumber
                        )
                    }
                }
                else -> {
                    recruit.considerScholarship(team)
                }
            }
        }
    }

    private fun doInteraction(interest: RecruitInterest, event: RecruitingEvent): Boolean {
        return when (event) {
            RecruitingEvent.SCOUT -> interest.interest >= 0
            RecruitingEvent.COACH_CONTACT -> interest.interest >= 20
            RecruitingEvent.OFFER_SCHOLARSHIP -> interest.interest >= 50
            RecruitingEvent.OFFICIAL_VISIT -> interest.interest >= 50
        }
    }
}